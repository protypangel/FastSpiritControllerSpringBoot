package test.configuration;

import org.fast.spirit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import test.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
  List<User> users = new ArrayList<>();
  public MyUserDetailsService() {
    users.add(new User("DEVELOPER", "DEVELOPER", "DEVELOPER"));
    users.add(new User("ADMIN", "ADMIN", "ADMIN"));
    users.add(new User("CLIENT", "CLIENT", "CLIENT"));
  }
  @Autowired JwtUtil jwtUtil;

  @Override public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
    return users.stream().filter(user -> user.getUsername().equals(identification)).findFirst().orElseThrow(() -> new UsernameNotFoundException(identification));
  }
  public void getTokens() {
    for (User user : users)
      System.out.println(user.getUsername() + " -> " + jwtUtil.generateToken(user));
  }
}
