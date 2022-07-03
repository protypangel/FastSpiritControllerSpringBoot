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
    super.api = new ApiInfoBuilder().title("FastSpirit").version("1.0-SNAPSHOT").logoUrl("https://speeding-eclipse-384877.postman.co/_ar-assets/images/favicon-1-48.png");
  }
}
  