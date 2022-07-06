package test.configuration;

import lombok.Getter;
import org.fast.spirit.configuration.FastSpiritConfig;
import org.fast.spirit.info.ApiInfo;
import org.fast.spirit.info.ApiInfoBuilder;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FastSpiritCustom extends FastSpiritConfig {
  public FastSpiritCustom() {
    super.api = new ApiInfoBuilder();
  }
}
  