package test.test;

import org.fast.spirit.configuration.FastSpiritConfigurationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestJson {
  @Autowired FastSpiritConfigurationController controller;

  @Test public void ADMIN() {

  }
}
