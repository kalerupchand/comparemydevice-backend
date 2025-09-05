// src/main/java/.../config/CorsConfig.java
package com.comparemydevice.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // LOCAL dev
    config.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "http://127.0.0.1:3000"
    ));
    // PRODUCTION (add your real domains)
    config.getAllowedOrigins().addAll(List.of(
            "https://comparemydevice.com",
            "https://www.comparemydevice.com",
            "https://api.comparemydevice.com"
    ));

    config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With"));
    config.setAllowCredentials(true); // if you send cookies/Authorization

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}