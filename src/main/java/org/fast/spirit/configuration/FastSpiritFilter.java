package org.fast.spirit.configuration;

import org.fast.spirit.json.FastSpiritJsonBuilder;
import org.fast.spirit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class FastSpiritFilter extends OncePerRequestFilter {
  @Autowired JwtUtil jwtUtil;
  @Autowired private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");
    String identification = null, jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      identification = jwtUtil.extractIdentification(jwt);
    }
    if (identification != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =  userDetailsService.loadUserByUsername(identification);
      if (jwtUtil.validateToken(jwt, userDetails)) {
        String newJwt = jwtUtil.generateToken(userDetails);
        response.addHeader("newJWT", newJwt);
        response.addHeader("Access-Control-Expose-Headers","newJWT");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }
}
