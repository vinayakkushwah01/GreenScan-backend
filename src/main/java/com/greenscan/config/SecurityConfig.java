package com.greenscan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenscan.dto.response.ApiResponse;
import com.greenscan.security.JwtAuthenticationEntryPoint;
import com.greenscan.security.JwtAuthenticationFilter;
import com.greenscan.security.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler())
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/auth/**", "/public/**", "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Vendor endpoints
                .requestMatchers("/vendor/**").hasAnyRole("VENDOR", "ADMIN")

                // Pickup Assistant endpoints
                .requestMatchers("/pickup-assistant/**").hasAnyRole("PICKUP_ASSISTANT", "VENDOR", "ADMIN")

                // NGO endpoints
                .requestMatchers("/ngo/**").hasAnyRole("NGO", "ADMIN")

                // Ads Company endpoints
                .requestMatchers("/ads/**").hasAnyRole("ADS_COMPANY", "ADMIN")

                // End User profile endpoints
                .requestMatchers("/end_users/**").hasAnyRole("END_USER", "ADMIN")
                
                 // Rewards endpoints
                .requestMatchers(HttpMethod.GET, "/users/profile")
                    .hasAnyRole("END_USER", "VENDOR", "PICKUP_ASSISTANT", "ADMIN", "NGO", "ADS_COMPANY")
                .requestMatchers(HttpMethod.PUT, "/users/profile")
                    .hasAnyRole("END_USER", "VENDOR", "PICKUP_ASSISTANT", "ADMIN", "NGO", "ADS_COMPANY")

                // Cart endpoints
                .requestMatchers("/carts/**").hasAnyRole("END_USER", "VENDOR", "PICKUP_ASSISTANT", "ADMIN")

                // All other requests need authentication
                .anyRequest().authenticated()
            );

        // Add authentication provider and JWT filter
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Debug filter: log URI and authorities
        http.addFilterAfter((request, response, chain) -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                System.out.println("Request URI: " + ((HttpServletRequest)request).getRequestURI());
                System.out.println("Authenticated user: " + auth.getName());
                System.out.println("Authorities: " + auth.getAuthorities());
            }
            chain.doFilter(request, response);
        }, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ApiResponse<Object> error = ApiResponse.error(
                "Access denied – either your JWT is invalid or your role does not allow access to this resource."
            );
            writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, error);
        };
    }

    private void writeJsonResponse(HttpServletResponse response, int status, ApiResponse<Object> body) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "https://*.greenscan.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
