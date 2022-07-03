package test.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.fast.spirit.annotation.custom.FastSpiritExample;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import test.configuration.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@AllArgsConstructor
@JsonView(View.Public.class)
public class User implements UserDetails {
  @FastSpiritExample(description = "description de base")
  private String username, password, role;

  @JsonView(View.Admin.class)
  private final Integer number = 5;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (username.equals("ADMIN")) return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role), new SimpleGrantedAuthority("ACCESS")));
    return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role)));
  }

 @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
