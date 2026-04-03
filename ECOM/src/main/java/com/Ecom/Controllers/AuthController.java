package com.Ecom.Controllers;

import com.Ecom.Dtos.LoginRequest;
import com.Ecom.Dtos.TokenResponse;
import com.Ecom.Dtos.UserDto;
import com.Ecom.Entities.User;
import com.Ecom.Exceptions.ResourceNotFoundException;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Security.JwtService;
import com.Ecom.Security.ROLE;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;


    private  final ModelMapper modelMapper;

    private  final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User already exists with this email");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setRole(ROLE.NORMAL);
        user.setEnable(true);
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        User saved = userRepository.save(user);

        return new ResponseEntity<>(modelMapper.map(saved, UserDto.class), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> generateToken(
            @RequestBody LoginRequest loginRequest
    ) {

        //email id and password
        try {

            //created the obeject of auhtentication[username, password]
            var authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            //doing authencation
            Authentication authenticatedObject = authenticationManager.authenticate(authentication);


            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            if (!user.isEnable()) {
                throw new DisabledException("User is disabled. Contact to admin");
            }


            // access token
            String accessToken=jwtService.generateAccessToken(user);
            String refreshToken= jwtService.generateRefreshToken(user);
            var tokenResponse= new TokenResponse();

            tokenResponse.setUser(modelMapper.map(user, UserDto.class));
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setRefreshToken(refreshToken);
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);



        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid email or password");
        }


    }

}
