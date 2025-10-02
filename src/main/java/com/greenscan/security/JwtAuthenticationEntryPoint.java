package com.greenscan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenscan.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                        AuthenticationException e) throws IOException {

        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

       // ApiResponseDto <Object> response = ApiResponseDTO.error("Unauthorized access - Invalid or expired token");
        ApiResponse <Object> response = ApiResponse.error("Unauthorized access - Invalid or expired token");
        String jsonResponse = objectMapper.writeValueAsString(response);
        httpServletResponse.getWriter().write(jsonResponse);
    }
    
}
