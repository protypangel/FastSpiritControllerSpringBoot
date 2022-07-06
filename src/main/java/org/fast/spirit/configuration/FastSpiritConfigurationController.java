package org.fast.spirit.configuration;

import org.fast.spirit.json.FastSpiritJsonBuilder;
import org.fast.spirit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Controller
public class FastSpiritConfigurationController {
  private @Autowired FastSpiritJsonBuilder builder;
  private @Autowired JwtUtil jwtUtil;
  private @Value("${fast-spirit.api.controllers:/api/controllers}") String controller;

  @GetMapping("${fast-spirit.api:/api}")
  public String index (Model model) {
    model.addAttribute("controller", controller);
    model.addAttribute("config", builder.config());
    model.addAttribute("controllers", builder.authority("ADMIN", "ADMIN"));
    return "fast-spirit/index";
  }
  @GetMapping("${fast-spirit.api.controllers:/api/controllers}")
  public String controllers (Model model, @RequestHeader String password, @RequestHeader String username) {
    model.addAttribute("controllers", builder.authority(username, password));
    return "fast-spirit/controllers :: body";
  }
}