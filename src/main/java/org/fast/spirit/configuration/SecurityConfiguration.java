package org.fast.spirit.configuration;

import org.fast.spirit.configuration.FastSpiritFilter;
import org.fast.spirit.json.FastSpiritJsonBuilder;
import org.fast.spirit.security.MyBasicAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@ComponentScan(basePackages = {"org.fast.spirit.security"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private FastSpiritFilter jwtRequestFilter;
    @Autowired private FastSpiritJsonBuilder builder;

    @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    @Bean @Override public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override protected void configure(HttpSecurity http) throws Exception {
        builder.registry(http.authorizeRequests()).antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
        http.cors();
    }
    @Bean public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
