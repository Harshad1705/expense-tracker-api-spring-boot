  package com.harrySpringBoot.expensetrackerapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.harrySpringBoot.expensetrackerapi.security.CustomUserDetailService;
import com.harrySpringBoot.expensetrackerapi.security.JwtRequestFilter;



@Configuration
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailsService;

    // made "/login"  and "/register" public 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        

        return  http.csrf(csrf->csrf.disable())
            .authorizeHttpRequests(auth->auth.requestMatchers("/login","/register").permitAll().anyRequest().authenticated())
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults())
                .build();
    }

    // use for Inmemory user detail
    // if want to use -
    // then uncomment this code as well as CustomUserDetailService
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user1 = User.withDefaultPasswordEncoder()
    //             .username("harry")
    //             .password("12345")
    //             .authorities("admin")
    //             .build();
    //     UserDetails user2 = User.withDefaultPasswordEncoder()
    //             .username("test")
    //             .password("12345")
    //             .authorities("user")
    //             .build();

    //     return new InMemoryUserDetailsManager(user1, user2);
    // }


    @Bean
    public JwtRequestFilter authenticationJwtTokenFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // use for custom user detail using mysql
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
           DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
           authProvider.setUserDetailsService(userDetailsService);
           authProvider.setPasswordEncoder(passwordEncoder());
           return authProvider;
    }


    // use to initialize AuthenticationManager used in AuthController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
