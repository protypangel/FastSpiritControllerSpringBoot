package test;

import org.fast.spirit.annotation.custom.FastSpiritCustomAuthorization;
import org.fast.spirit.annotation.principal.FastSpiritEnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import test.configuration.MyUserDetailsService;

@SpringBootApplication
@FastSpiritEnable
public class Launcher implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }

  @Autowired MyUserDetailsService service;
  @Override public void run(String... args) throws Exception {
    service.getTokens();
  }
}
