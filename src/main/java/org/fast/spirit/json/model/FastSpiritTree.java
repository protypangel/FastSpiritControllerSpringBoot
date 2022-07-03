package org.fast.spirit.json.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.SneakyThrows;
import org.fast.spirit.annotation.custom.FastSpiritExample;
import org.fast.spirit.json.FastSpiritBody;
import org.fast.spirit.util.View;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@JsonView(View.Public.class)
@Getter
public class FastSpiritTree {
  private String type;
  private Map<String, List<Object>> fields = new HashMap<>();

  @SneakyThrows
  public FastSpiritTree(String type, String packageCaller, List<Class<?>> viewsMethod) {
    // Si le type n'est pas un type créer
    if (cantChildren(type, packageCaller)) return;

    Class<?> c = Class.forName(type);
    JsonView jsonView = c.getAnnotation(JsonView.class);
    List<Class<?>> viewsType = jsonView != null ? Arrays.asList(jsonView.value()) : new ArrayList<>();

    for (Field field : c.getDeclaredFields()) addChildren(field, packageCaller, viewsType, viewsMethod);

  }
  @SneakyThrows
  public FastSpiritTree(String type, String packageCaller) {
    // Si le type n'est pas un type créer
    if (cantChildren(type, packageCaller)) return;

    for (Field field : Class.forName(type).getFields()) addChildren(field, packageCaller);
  }
  public boolean cantChildren (String type, String packageCaller) {
    this.type = type;
    return !staticCanChildren(type, packageCaller);
  }
  public static boolean staticCanChildren (String type, String packageCaller) {
    return type.startsWith(packageCaller);
  }

  private void addChildren(Field field, String packageCaller) {
    fields.put(
        field.getName(),
        Arrays.stream(FastSpiritBody.getTypesName(field.getType().getTypeName())).map(element -> convertToChildren(field.getAnnotation(FastSpiritExample.class), element, packageCaller)).filter(Objects::nonNull).collect(Collectors.toList())
    );
  }
  private void addChildren(Field field, String packageCaller, List<Class<?>> viewsType, List<Class<?>> viewsMethod) {
    JsonView jsonView = field.getAnnotation(JsonView.class);
    if (jsonView != null) viewsType = Arrays.asList(jsonView.value());
    // Si un view est dans la viewMethod alors on l'ajoute
    if (anyMatch(viewsType, viewsMethod)) {
      fields.put(
        field.getName(),
        Arrays.stream(FastSpiritBody.getTypesName(field.getType().getTypeName())).map(element -> convertToChildren(field.getAnnotation(FastSpiritExample.class), element, packageCaller, viewsMethod)).filter(Objects::nonNull).collect(Collectors.toList())
      );
    }
  }

  public static Object convertToChildren(FastSpiritExample example, String typeName, String packageCaller) {
    if (FastSpiritTree.staticCanChildren(typeName, packageCaller)) return new FastSpiritTree(typeName, packageCaller);
    return new FastSpiritLeaf(example, typeName);
  }
  public static Object convertToChildren(FastSpiritExample example, String typeName, String packageCaller, List<Class<?>> viewsMethod) {
    if (FastSpiritTree.staticCanChildren(typeName, packageCaller)) return new FastSpiritTree(typeName, packageCaller, viewsMethod);
    return new FastSpiritLeaf(example, typeName);
  }

  public static boolean anyMatch(List<Class<?>> viewsType, List<Class<?>> viewsMethod) {
    return viewsMethod.stream().anyMatch(viewMethod -> anyMatch(viewsType, viewMethod));
  }
  public static boolean anyMatch(List<Class<?>> viewsType, Class<?> viewMethod) {
    return viewsType.stream().anyMatch(viewType -> viewType.isAssignableFrom(viewMethod));
  }
}
