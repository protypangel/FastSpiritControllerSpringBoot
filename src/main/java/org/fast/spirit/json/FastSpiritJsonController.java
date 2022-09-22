package org.fast.spirit.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.fast.spirit.annotation.principal.FastSpiritController;
import org.fast.spirit.security.FastSpiritSecurity;
import org.fast.spirit.util.View;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@JsonView(View.Public.class)
public class FastSpiritJsonController {
  private String title = "";
  private List<FastSpiritJsonMapping> mappings = new ArrayList<>();
  private String description;

  @JsonView(View.Internal.class)
  private Boolean display = true;

  public FastSpiritJsonController(Class<?> c, Map<String, FastSpiritCustomAuthorizationModel> customAuthorizations, String packageCaller) {
    FastSpiritController controller = c.getAnnotation(FastSpiritController.class);
    RequestMapping mapping = c.getAnnotation(RequestMapping.class);

    if (controller != null) {
      if (!controller.display()) {
        display = false;
        return;
      }
      if (controller.value().isEmpty()) this.title = c.getName();
      else {
        this.title = controller.value();
        this.description = controller.description();
      }
    } else {
      this.title = c.getName().replace(c.getPackageName() + ".", "");
    }
    // Ajouter les customAuthorization qui y sont present
    List<FastSpiritCustomAuthorizationModel> customAuthorizationsAccepted = new ArrayList<>();
    for (Annotation annotation : c.getAnnotations()) {
      String name = annotation.getClass().getName();
      Optional<Map.Entry<String, FastSpiritCustomAuthorizationModel>> optional = customAuthorizations.entrySet().stream().filter(entry -> entry.getKey().equals(name)).findFirst();
      optional.ifPresent(stringFastSpiritCustomAuthorizationModelEntry -> customAuthorizationsAccepted.add(stringFastSpiritCustomAuthorizationModelEntry.getValue()));
    }
    // Pour toutes les fonctions de mapping, ajouter dans la liste
    for (Method method : Arrays.stream(c.getDeclaredMethods()).filter(this::isRequest).collect(Collectors.toList()))
      this.mappings.add(new FastSpiritJsonMapping(controller, mapping, method, customAuthorizationsAccepted, customAuthorizations, packageCaller));

    this.mappings = this.mappings.stream().sorted(Comparator.comparing(FastSpiritJsonMapping::getTitle)).collect(Collectors.toList());
  }

  private FastSpiritJsonController(String title, List<FastSpiritJsonMapping> mappings, Boolean display) {
    this.title = title;
    this.mappings = mappings.stream().map(FastSpiritJsonMapping::clone).collect(Collectors.toList());
    this.display = display;
  }

  private boolean isRequest(Method method) {
    return method.getAnnotation(GetMapping.class) != null ||
            method.getAnnotation(PostMapping.class) != null ||
            method.getAnnotation(DeleteMapping.class) != null ||
            method.getAnnotation(PatchMapping.class) != null ||
            method.getAnnotation(PutMapping.class) != null ||
            method.getAnnotation(RequestMapping.class) != null;
  }

  public List<FastSpiritSecurity> security() {
    return mappings.stream().map(FastSpiritJsonMapping::security).collect(Collectors.flatMapping(Collection::stream, Collectors.toList()));
  }

  public FastSpiritJsonController filter(List<String> authorities) {
    this.mappings = this.mappings.stream().filter(mapping -> mapping.filter(authorities)).collect(Collectors.toList());
    return this;
  }
  public FastSpiritJsonController filter() {
    this.mappings = this.mappings.stream().filter(FastSpiritJsonMapping::filter).collect(Collectors.toList());
    return this;
  }

  public FastSpiritJsonController clone () {
    return new FastSpiritJsonController(title, mappings, display);
  }
}