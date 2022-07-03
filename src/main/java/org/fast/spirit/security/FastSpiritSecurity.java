package org.fast.spirit.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class FastSpiritSecurity {
  private HttpMethod http;
  private String[] authorizations, paths;

  public FastSpiritSecurity(HttpMethod http, String[] roles, String[] paths) {
    this.http = http;
    this.authorizations = roles;
    this.paths = Set.of(paths).toArray(new String[]{});
  }

  public void registry(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
    if (authorizations.length == 0) registry.antMatchers(http, paths).permitAll();
    else registry.antMatchers(http, paths).hasAnyAuthority(authorizations);
  }

  public FastSpiritSecurity addPaths(String[] paths) {
    Set<String> set = new HashSet<>();
    set.addAll(Set.of(paths));
    set.addAll(Set.of(this.paths));

    this.paths = set.toArray(new String[]{});
    return this;
  }

  @AllArgsConstructor
  public class Header {
    private HttpMethod http;
    private String[] roles;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Header header = (Header) o;
      return http == header.http && Arrays.equals(roles, header.roles);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(http);
      result = 31 * result + Arrays.hashCode(roles);
      return result;
    }
  }

  public Header header() {
    return new Header(http, authorizations);
  }
}
