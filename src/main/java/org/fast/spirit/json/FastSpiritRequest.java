package org.fast.spirit.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.fast.spirit.util.View;

@AllArgsConstructor
@Getter
@JsonView(View.Public.class)
@ToString
public class FastSpiritRequest {
  private Boolean require;
  private String type;
  // TODO: A remplir
  private String description;
}
