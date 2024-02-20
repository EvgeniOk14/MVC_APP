package com.example.daocrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception
  {
      http.authorizeHttpRequests((authorize) -> authorize
                      .requestMatchers("/index").hasAnyRole("USER")
                      .requestMatchers("/people/refuse").hasAnyRole("ADMIN")
              .requestMatchers("/people").hasAnyRole("ADMIN")
              .requestMatchers("/people/{id}").hasAnyRole("ADMIN")
                      .requestMatchers("/people/new").hasAnyRole("ADMIN")
                      .requestMatchers("/people/{id}/edit").hasAnyRole("ADMIN")
                      .requestMatchers("people/{id}/delete").hasAnyRole("ADMIN")
              .anyRequest().authenticated())
              .formLogin(login -> login
                      .defaultSuccessUrl("/")
                      .permitAll())
              .logout(logout -> logout
                      .logoutSuccessUrl("/"));
      return http.build();
  }


    @Bean
  PasswordEncoder passwordEncoder()
  {
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  UserDetailsManager InMemoryUserDetailManager()
  {

      var user1 = User.withUsername("u").password(passwordEncoder().encode("u")).roles("USER").build();
      var user2 = User.withUsername("a").password(passwordEncoder().encode("a")).roles("USER", "ADMIN").build();
      return new InMemoryUserDetailsManager(user1, user2);
  }
}


//      var user1 = User.withUsername("user").password("password").roles("USER").build();
//      var user2 = User.withUsername("admin").password("password").roles("USER", "ADMIN").build();