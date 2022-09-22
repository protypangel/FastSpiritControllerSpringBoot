package org.fast.spirit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafTemplateConfig {

  @Bean
  public SpringTemplateEngine springTemplateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    return templateEngine;
  }
  @Bean
  public SpringResourceTemplateResolver htmlTemplateResolver(){
    SpringResourceTemplateResolver template = new SpringResourceTemplateResolver();
    template.setPrefix("classpath:/templates/");
    template.setSuffix(".html");
    template.setTemplateMode(TemplateMode.HTML);
    template.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return template;
  }
}
