package org.fast.spirit.json.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class FastSpiritModel {
  public String type, description;
  public List<Model> models = new ArrayList<>();

  @AllArgsConstructor
  public class Model {
    public String type, example, name;
  }
}
