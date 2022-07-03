package org.fast.spirit.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.SneakyThrows;
import org.fast.spirit.json.model.FastSpiritTree;
import org.fast.spirit.util.View;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonView(View.Public.class)
@Getter
public class FastSpiritBody {
  private boolean required;
  @JsonView(View.Public.class)
  private List<Object> containers = new ArrayList<>();
  @SneakyThrows
  public FastSpiritBody(String type, boolean required, String packageCaller, List<Class<?>> viewsMethod) {
    this.required = required;

    for (String typeName : getTypesName(type))
      containers.add(FastSpiritTree.convertToChildren(null, typeName, packageCaller, viewsMethod));
  }
  public FastSpiritBody(String type, boolean required, String packageCaller) {
    this.required = required;
    for (String typeName : getTypesName(type))
      containers.add(FastSpiritTree.convertToChildren(null, typeName, packageCaller));
  }
  public static String[] getTypesName(String type) {
    return type.split("[<>]");
  }
  public static Stream<String> getTypesName(Class<?>[] types) {
    return Arrays.stream(types).map(Class::getTypeName);
  }
}
