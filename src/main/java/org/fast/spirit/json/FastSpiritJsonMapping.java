package org.fast.spirit.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.fast.spirit.annotation.principal.FastSpiritCancelAuthorizationController;
import org.fast.spirit.annotation.principal.FastSpiritController;
import org.fast.spirit.annotation.principal.FastSpiritMapping;
import org.fast.spirit.annotation.secondary.FastSpiritNote;
import org.fast.spirit.annotation.secondary.FastSpiritResponse;
import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;
import org.fast.spirit.json.model.FastSpiritTree;
import org.fast.spirit.security.FastSpiritSecurity;
import org.fast.spirit.util.View;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;import java.util.stream.Collectors;

@Getter
@JsonView(View.Public.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FastSpiritJsonMapping {
  private List<String>  httpMethods = new ArrayList<>(),
                        paths = new ArrayList<>(),
                        tags = new ArrayList<>();

  private List<String>  accessApi = new ArrayList<>();

  private String title = "", description = "";
  private Map<String, String[]> notes = new HashMap<>();

  private Map<String, FastSpiritRequest> pathVariables = new HashMap<>();
  private Map<String, FastSpiritRequest> parameters = new HashMap<>();
  private FastSpiritBody body;
  private List<FastSpiritResponseMapping> responses = new ArrayList<>();


  @JsonView(View.Internal.class)
  private List<String>  accessDoc = new ArrayList<>();

  @JsonView(View.Internal.class)
  private Boolean display = true;

  public FastSpiritJsonMapping(FastSpiritController controller, RequestMapping mapping, Method method, List<FastSpiritCustomAuthorizationModel> customAuthorizationsController, Map<String, FastSpiritCustomAuthorizationModel> customAuthorizationsExists, String packageCaller) {
    if (mapping != null) this.httpMethods.addAll(Arrays.stream(mapping.method()).map(m -> m.name().toUpperCase()).collect(Collectors.toList()));
    setPaths(mapping, method);
    setAuthorization(controller, method, customAuthorizationsController, findCustomAuthorizationAccepted(customAuthorizationsExists, method));
    setRequestDatas(method, packageCaller);
    setResponses(method, packageCaller);
  }

  private void setResponses(Method method, String packageCaller) {
    JsonView jsonView = method.getAnnotation(JsonView.class);
    List<Class<?>> views = jsonView != null ? Arrays.asList(jsonView.value()) : Arrays.asList();

    FastSpiritMapping mapping = method.getAnnotation(FastSpiritMapping.class);
    if (mapping != null) {
      for (FastSpiritResponse response : mapping.responses()) {
        List<Object> trees = FastSpiritBody.getTypesName(response.types()).map(type -> {
          if (FastSpiritTree.staticCanChildren(type, packageCaller)) return new FastSpiritTree(type, packageCaller, views);
          return type;
        }).collect(Collectors.toList());
        responses.add(new FastSpiritResponseMapping(response.code(), response.description(), trees));
      }
    } else this.title = method.getName();
  }

  private List<FastSpiritCustomAuthorizationModel> findCustomAuthorizationAccepted(Map<String, FastSpiritCustomAuthorizationModel> customAuthorizationsExists, Method method) {
    List<FastSpiritCustomAuthorizationModel> customAuthorizationsAccepted = new ArrayList<>();
    for (Annotation annotation : method.getAnnotations()) {
      String name = annotation.annotationType().getName();
      Optional<Map.Entry<String, FastSpiritCustomAuthorizationModel>> optional = customAuthorizationsExists.entrySet().stream().filter(entry -> entry.getKey().equals(name)).findFirst();
      optional.ifPresent(stringFastSpiritCustomAuthorizationModelEntry -> customAuthorizationsAccepted.add(stringFastSpiritCustomAuthorizationModelEntry.getValue()));
    }
    return customAuthorizationsAccepted;
  }

  private void setRequestDatas(Method method, String packageCaller) {
    for (Parameter parameter : method.getParameters()) {
      RequestBody body = parameter.getAnnotation(RequestBody.class);
      PathVariable variable = parameter.getAnnotation(PathVariable.class);
      RequestParam param = parameter.getAnnotation(RequestParam.class);
      if (body != null) createBody(parameter, body, packageCaller);
      if (variable != null) createVariable(parameter, variable);
      if (param != null) createParam(parameter, param);
    }
  }
  private void createParam (Parameter parameter, RequestParam param) {
    String name = parameter.getName();
    if (!param.name().isEmpty()) name = param.name();
    if (!param.value().isEmpty()) name = param.value();
    this.parameters.put(name, new FastSpiritRequest(param.required(), parameter.getType().getName(), ""));
  }
  private void createBody (Parameter parameter, RequestBody body, String packageCaller) {
    JsonView jsonView = parameter.getAnnotation(JsonView.class);
    this.body = jsonView == null || jsonView.value().length == 0
            ? new FastSpiritBody(parameter.getAnnotatedType().getType().getTypeName(), body.required(), packageCaller)
            : new FastSpiritBody(parameter.getAnnotatedType().getType().getTypeName(), body.required(), packageCaller, Arrays.asList(jsonView.value()));
  }
  private void createVariable (Parameter parameter, PathVariable path) {
    String name = parameter.getName();
    if (!path.name().isEmpty()) name = path.name();
    if (!path.value().isEmpty()) name = path.value();
    this.pathVariables.put(name, new FastSpiritRequest(path.required(), parameter.getType().getName(), ""));
  }

  private void setAuthorization(FastSpiritController controller, Method method, List<FastSpiritCustomAuthorizationModel> customAuthorizationsController, List<FastSpiritCustomAuthorizationModel> customAuthorizations) {
    FastSpiritCancelAuthorizationController cancelAuthorizationController = method.getAnnotation(FastSpiritCancelAuthorizationController.class);
    if (controller != null) addAccess(controller.roles(), controller.privileges(), cancelAuthorizationController);
    addAccess(customAuthorizationsController, cancelAuthorizationController);

    addAccess(customAuthorizations, null);
    if (method.getAnnotation(FastSpiritMapping.class) != null) {
      FastSpiritMapping mapping = method.getAnnotation(FastSpiritMapping.class);
      addAccess(mapping.roles(), mapping.privileges(), null);
      display =  mapping.display();
      tags = Arrays.asList(mapping.tags());
      title = mapping.value().isEmpty() ? method.getName() : mapping.value();
      description = mapping.description();
      for (FastSpiritNote note : mapping.notes()) notes.put(note.key(), note.value());
    }
  }
  private void addAccess (List<FastSpiritCustomAuthorizationModel> customAuthorizations, FastSpiritCancelAuthorizationController cancel) {
    if (cancel == null || (!cancel.api() && !cancel.doc())) {
      for (FastSpiritCustomAuthorizationModel customAuthorization : customAuthorizations) {
        this.accessApi.addAll(customAuthorization.getAuthorizationApi());
        this.accessDoc.addAll(customAuthorization.getAuthorizationDoc());
      }
    } else if (!cancel.api())
      for (FastSpiritCustomAuthorizationModel customAuthorization : customAuthorizations) this.accessDoc.addAll(customAuthorization.getAuthorizationDoc());
    else if (!cancel.doc())
      for (FastSpiritCustomAuthorizationModel customAuthorization : customAuthorizations) this.accessApi.addAll(customAuthorization.getAuthorizationApi());
  }
  private void addAccess (FastSpiritRoleAccess roles, FastSpiritRoleAccess privileges, FastSpiritCancelAuthorizationController cancel) {
    if (cancel == null || (!cancel.api() && !cancel.doc())) {
      this.accessApi.addAll(Arrays.stream(roles.api()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.stream(roles.value()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.stream(roles.both()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.asList(privileges.api()));
      this.accessApi.addAll(Arrays.asList(privileges.value()));
      this.accessApi.addAll(Arrays.asList(privileges.both()));

      this.accessDoc.addAll(Arrays.stream(roles.doc()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.stream(roles.value()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.stream(roles.both()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.asList(privileges.doc()));
      this.accessDoc.addAll(Arrays.asList(privileges.value()));
      this.accessDoc.addAll(Arrays.asList(privileges.both()));
    } else if (!cancel.api()) {
      this.accessApi.addAll(Arrays.stream(roles.api()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.stream(roles.value()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.stream(roles.both()).map(this::setRole).collect(Collectors.toList()));
      this.accessApi.addAll(Arrays.asList(privileges.api()));
      this.accessApi.addAll(Arrays.asList(privileges.value()));
      this.accessApi.addAll(Arrays.asList(privileges.both()));
    }
    else if (!cancel.doc()) {
      this.accessDoc.addAll(Arrays.stream(roles.doc()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.stream(roles.value()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.stream(roles.both()).map(this::setRole).collect(Collectors.toList()));
      this.accessDoc.addAll(Arrays.asList(privileges.doc()));
      this.accessDoc.addAll(Arrays.asList(privileges.value()));
      this.accessDoc.addAll(Arrays.asList(privileges.both()));
    }
  }

  private String setRole(String s) {
    return "ROLE_" + s;
  }

  private void setPaths(RequestMapping mapping, Method method) {
    List<String> paths = new ArrayList<>();
    if (mapping != null) {
      paths.addAll(Arrays.asList(mapping.path()));
      paths.addAll(Arrays.asList(mapping.value()));
    }
    if (paths.isEmpty()) paths.add("");

    if(method.getAnnotation(GetMapping.class) != null) {
      this.httpMethods.add("GET");
      GetMapping map = method.getAnnotation(GetMapping.class);
      if (map.value().length == 0 && map.path().length == 0) this.paths.addAll(paths);
      for (String value : map.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : map.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
    else if(method.getAnnotation(PostMapping.class) != null) {
      this.httpMethods.add("POST");
      PostMapping map = method.getAnnotation(PostMapping.class);
      if (map.value().length == 0 && map.path().length == 0) this.paths.addAll(paths);
      for (String value : map.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : map.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
    else if(method.getAnnotation(DeleteMapping.class) != null) {
      this.httpMethods.add("DELETE");
      DeleteMapping map = method.getAnnotation(DeleteMapping.class);
      if (map.value().length == 0 && map.path().length == 0) this.paths.addAll(paths);
      for (String value : map.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : map.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
    else if(method.getAnnotation(PatchMapping.class) != null) {
      this.httpMethods.add("PATCH");
      PatchMapping map = method.getAnnotation(PatchMapping.class);
      if (map.value().length == 0 && map.path().length == 0) this.paths.addAll(paths);
      for (String value : map.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : map.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
    else if(method.getAnnotation(PutMapping.class) != null) {
      this.httpMethods.add("PUT");
      PutMapping map = method.getAnnotation(PutMapping.class);
      if (map.value().length == 0 && map.path().length == 0) this.paths.addAll(paths);
      for (String value : map.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : map.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
    else if(method.getAnnotation(RequestMapping.class) != null) {
      RequestMapping request = method.getAnnotation(RequestMapping.class);
      this.httpMethods.addAll(Arrays.stream(request.method()).map(m -> m.name().toUpperCase()).collect(Collectors.toList()));
      if (request.value().length == 0 && request.path().length == 0) this.paths.addAll(paths);
      for (String value : request.value())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
      for (String value : request.path())
        this.paths.addAll(paths.stream().map(path -> path + value).collect(Collectors.toList()));
    }
  }
  public String[] array(List<String> list) {
    String[] array = new String[]{};
    array = list.toArray(array);
    return array;
  }
  public String toString() {
    return title == null ? "" : title;
  }

  public List<FastSpiritSecurity> security() {
    String[] paths = array(this.paths);
    String[] roles = array(this.accessApi);

    return httpMethods.stream().map(httpMethod -> {
      HttpMethod http = HttpMethod.valueOf(httpMethod);
      return new FastSpiritSecurity(http, roles, paths);
    }).collect(Collectors.toList());
  }

  public boolean filter() {
    return display && accessDoc.size() == 0;
  }
  public boolean filter(List<String> authorities) {
    return display && accessDoc.size() == 0 || accessDoc.stream().anyMatch(access -> authorities.stream().anyMatch(authority -> authority.equals(access)));
  }

  @SneakyThrows
  public FastSpiritJsonMapping clone () {
    return new FastSpiritJsonMapping(this.httpMethods, this.paths, this.tags, this.accessApi, this.title, this.description, this.notes, this.pathVariables, this.parameters, this.body, this.responses, this.accessDoc, this.display);
  }
}
