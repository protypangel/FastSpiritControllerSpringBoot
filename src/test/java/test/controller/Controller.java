package test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.fast.spirit.annotation.principal.FastSpiritCancelAuthorizationController;
import org.fast.spirit.annotation.principal.FastSpiritController;
import org.fast.spirit.annotation.principal.FastSpiritMapping;
import org.fast.spirit.annotation.secondary.FastSpiritResponse;
import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.configuration.ADMIN;
import test.configuration.view.View;
import test.model.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = {"/api", "/api2"}, method = { RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.TRACE })
public class Controller {
//  @FastSpiritCancelAuthorizationController(api = false)
//  @RequestMapping(value = {"/toto", "/tata"}, method = { RequestMethod.GET, RequestMethod.DELETE })
//  public void post (Principal principal) {
//    System.out.println("here");
//  }

  @FastSpiritCancelAuthorizationController
  @PutMapping("/getAllSet")
  @ADMIN
  @JsonView(View.Admin.class)
  @FastSpiritMapping(responses = {
      @FastSpiritResponse(
          code = 200,
          description = "DESCRIPTION",
          types = {List.class, User.class}
      )
  })
  public User getAllSet(@JsonView(View.Admin.class) @RequestBody Set<User> test) {
    return new User("TEST", "TEST", "TEST");
  }

  @FastSpiritMapping(
      responses = {
          @FastSpiritResponse(
                  code = 200,
                  description = "DESCRIPTION",
                  types = {List.class, User.class}
          )
      },
      roles = @FastSpiritRoleAccess(
          api = "ADMIN"
      )
  )
  @JsonView(View.Public.class)
  @GetMapping("/getAllList")
  public User getAllList(@JsonView(View.Public.class) @RequestBody List<User> test) {
    return new User("TEST", "TEST", "TEST");
  }
  @DeleteMapping("/getAll")
  public User getAll(@RequestBody User test) {
    return new User("TEST", "TEST", "TEST");
  }

  @FastSpiritCancelAuthorizationController
  @PatchMapping("/get2/{id}")
  @ADMIN
  public void getAll2(@PathVariable String id) {
  }

  @FastSpiritCancelAuthorizationController
  @PostMapping("/get3")
  @ADMIN
  public void getAll3(@RequestParam String param) {
  }
}
