package org.fast.spirit.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fast.spirit.json.model.FastSpiritTree;
import org.fast.spirit.util.View;

import java.util.List;

@AllArgsConstructor
@Getter
@JsonView(View.Public.class)
public class FastSpiritResponseMapping {
  private Integer code;
  private String description;
  private List<Object> containers;
}
