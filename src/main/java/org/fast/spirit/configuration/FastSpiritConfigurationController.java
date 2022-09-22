package org.fast.spirit.configuration;

import com.fasterxml.jackson.annotation.JsonView;
import org.fast.spirit.json.FastSpiritJsonBuilder;
import org.fast.spirit.json.FastSpiritJsonController;
import org.fast.spirit.util.JwtUtil;
import org.fast.spirit.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class FastSpiritConfigurationController {
  private @Autowired FastSpiritJsonBuilder builder;
  private @Autowired JwtUtil jwtUtil;
  private @Value("${fast-spirit.api.controllers.authority:/api/controllers/authority}") String authority;
  private @Value("${fast-spirit.api.controllers.empty:/api/controllers/empty}") String empty;
  @Autowired private SpringTemplateEngine templateEngine;

  @GetMapping(value = "${fast-spirit.api:/api}")
  public String index (Model model) {
    model.addAttribute("controller_authority", authority);
    model.addAttribute("controller_empty", empty);
    model.addAttribute("config", builder.config());
    return "fast-spirit/index";
  }

  @GetMapping("${fast-spirit.api.controllers.authority:/api/controllers/authority}")
  @ResponseBody
  @JsonView(View.Public.class)
  public ResponseEntity<?> controllers (@RequestHeader String password, @RequestHeader String username) {
    try {
      List<FastSpiritJsonController> controllers = builder.authority(username, password);

      Context context = new Context();
      context.setVariable("controllers", controllers);

      return ResponseEntity.ok(Map.of(
          "html", templateEngine.process("fast-spirit/controllers", context),
          "controllers", controllers
      ));
    } catch (UsernameNotFoundException exception) {
      return ResponseEntity.status(403).build();
    }
  }

  @GetMapping("${fast-spirit.api.controllers.empty:/api/controllers/empty}")
  @ResponseBody
  @JsonView(View.Public.class)
  public Map<String, Object> postMapping () {
    List<FastSpiritJsonController> controllers = builder.emptyAuthority();

    Context context = new Context();
    context.setVariable("controllers", controllers);

    return Map.of(
            "html", templateEngine.process("fast-spirit/controllers", context),
            "controllers", controllers
    );
  }
}