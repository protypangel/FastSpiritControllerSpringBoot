package org.fast.spirit.configuration;

import lombok.Getter;
import org.fast.spirit.info.ApiInfo;
import org.fast.spirit.info.ApiInfoBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class FastSpiritConfig {
  protected ApiInfo api = new ApiInfoBuilder();
  protected Map<String, String> httpColors = new HashMap<>(){{
      put("GET", "#3caab5");
      put("POST", "#78bc61");
      put("DELETE", "#a0213e");
      put("PUT", "#ff8a2c");
      put("PATCH", "#645dad");
      put("TRACE", "#664449");
      put("HEAD", "#798eb3");
      put("OPTIONS", "#ffa500");
    }};
}
