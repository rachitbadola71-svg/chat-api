package com.RACTEC.CHATAPI.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;                    // lets us switch CORS on with default settings
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;                    // the "guest list" rules
import org.springframework.web.cors.CorsConfigurationSource;              // the type Spring Security looks for
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;      // applies the rules to URL paths

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {this.jwtAuthFilter = jwtAuthFilter;}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // NEW: the guest list. It tells the browser which page is allowed to call this API.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();                                  // an empty rule book
        config.setAllowedOrigins(List.of("http://localhost:3000"));                          // ONLY our frontend may call us, not every website
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));        // OPTIONS is the browser's "may I?" question
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));                  // Authorization carries the JWT, Content-Type is needed for JSON and file upload
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();      // connects the rules to URLs
        source.registerCorsConfiguration("/**", config);                                     // apply the same rules to every endpoint
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())                                                 // NEW: switch CORS on, so the guest list is checked BEFORE the login check
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/users/new", "/error", "/ws/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}