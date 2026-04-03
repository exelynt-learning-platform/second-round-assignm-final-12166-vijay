package com.Ecom.ControllerTest;

import com.Ecom.Controllers.AuthController;
import com.Ecom.Dtos.LoginRequest;
import com.Ecom.Dtos.TokenResponse;
import com.Ecom.Dtos.UserDto;
import com.Ecom.Entities.User;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Security.JwtService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthTest {

    @Test
    void testGenerateToken_Success() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserRepository userRepository = mock(UserRepository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        JwtService jwtService = mock(JwtService.class);

        AuthController authController = new AuthController(authenticationManager, userRepository, modelMapper, jwtService);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("12345");

        User user = new User();
        user.setEnable(true);

        UserDto userDto = new UserDto();
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        ResponseEntity<TokenResponse> response = authController.generateToken(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGenerateToken_InvalidCredentials() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserRepository userRepository = mock(UserRepository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        JwtService jwtService = mock(JwtService.class);

        AuthController authController = new AuthController(authenticationManager, userRepository, modelMapper, jwtService);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("vijay@gmail.com");
        loginRequest.setPassword("Vijay123");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        assertThrows(BadCredentialsException.class, () -> {
            authController.generateToken(loginRequest);
        });
    }
}