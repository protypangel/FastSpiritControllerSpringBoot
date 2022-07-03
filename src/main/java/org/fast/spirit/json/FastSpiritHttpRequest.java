package org.fast.spirit.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.fast.spirit.annotation.custom.FastSpiritExample;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class FastSpiritHttpRequest {
  private String surClass;
  private String className;
  private boolean required;
  private FastSpiritExample example;

  public void test () {
//    Boolean assigned = false;
//    if (List.class.isAssignableFrom(parameter.getType())) {
//      model.setSurClass(List.class.getName());
//      assigned = true;
//    } else if (Set.class.isAssignableFrom(parameter.getType())) {
//      model.setSurClass(Set.class.getName());
//      assigned = true;
//    } else {
//      model.setClassName(parameter.getParameterizedType().getTypeName());
//    }
//    if (assigned) model.setClassName(parameter.getParameterizedType().getTypeName().replaceAll("[\\w*|\\.?]*<", "").replaceAll(">", ""));
  }
}
