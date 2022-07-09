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
}
