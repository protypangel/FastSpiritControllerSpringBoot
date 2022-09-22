package org.fast.spirit.json;

import lombok.SneakyThrows;
import org.fast.spirit.annotation.custom.FastSpiritCustomAuthorization;
import org.fast.spirit.configuration.FastSpiritConfig;
import org.fast.spirit.security.FastSpiritSecurity;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FastSpiritJsonBuilder {
  private List<FastSpiritJsonController> controllers = new ArrayList<>();
  private List<FastSpiritSecurity> securities = new ArrayList<>();

  @Autowired private UserDetailsService service;
  @Autowired private FastSpiritConfig config;


  @SneakyThrows
  public FastSpiritJsonBuilder() {
    ClassPathScanningCandidateComponentProvider springBootApplication = new ClassPathScanningCandidateComponentProvider(false);
    springBootApplication.addIncludeFilter(new AnnotationTypeFilter(SpringBootApplication.class));

    Reflections reflections = new Reflections(Class.forName(springBootApplication.findCandidateComponents("").stream().findFirst().get().getBeanClassName()).getPackage().getName());

    Map<String, FastSpiritCustomAuthorizationModel> customAuthorizations = new HashMap();
    for (Class<?> c : reflections.getTypesAnnotatedWith(FastSpiritCustomAuthorization.class)) customAuthorizations.put(c.getName(), new FastSpiritCustomAuthorizationModel(c.getAnnotation(FastSpiritCustomAuthorization.class)));

    String packageCaller = reflections.getTypesAnnotatedWith(SpringBootApplication.class).stream().findFirst().orElseThrow().getPackageName();
    for (Class<?> c : reflections.getTypesAnnotatedWith(RestController.class)) beanDefinitionController(c, customAuthorizations, packageCaller);
    reorganize();
  }

  private void reorganize() {
    securities = securities.stream().collect(Collectors.groupingBy(FastSpiritSecurity::header))
            .values().stream().collect(Collectors.flatMapping(security -> security.stream().reduce((previous, current) -> previous.addPaths(current.getPaths())).stream(), Collectors.toList()));
    controllers = controllers.stream().sorted(Comparator.comparing(FastSpiritJsonController::getTitle)).collect(Collectors.toList());
  }

  @SneakyThrows
  private void beanDefinitionController(Class<?> c, Map<String, FastSpiritCustomAuthorizationModel> customAuthorizations, String packageCaller) {
    // Ne pas prendre en compte nos composants
    if (c.getName().startsWith("org.fast.spirit")) return;
    FastSpiritJsonController controller = new FastSpiritJsonController(c, customAuthorizations, packageCaller);
    this.controllers.add(controller);
    securities.addAll(controller.security());
  }

  public List<FastSpiritJsonController> authority(String username, String password) throws UsernameNotFoundException {
      if (username == null && password == null) throw new UsernameNotFoundException("Principal null");
      UserDetails user = service.loadUserByUsername(username);
      if (!user.getPassword().equals(password)) throw new UsernameNotFoundException("Password not compatible");
      final List<String> authorities = service.loadUserByUsername(username).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
      return this.controllers.stream().map(FastSpiritJsonController::clone).filter(FastSpiritJsonController::getDisplay).map(controller -> controller.filter(authorities)).collect(Collectors.toList());
  }
  public List<FastSpiritJsonController> emptyAuthority () {
    return this.controllers.stream().map(FastSpiritJsonController::clone).filter(FastSpiritJsonController::getDisplay).map(FastSpiritJsonController::filter).collect(Collectors.toList());
  }
  public FastSpiritConfig config () {
    return config;
  }

  public ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
    for (FastSpiritSecurity security : this.securities) security.registry(registry);
    return registry;
  }
}
