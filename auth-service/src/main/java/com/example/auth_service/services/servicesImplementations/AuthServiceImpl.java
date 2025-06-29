package com.example.auth_service.services.servicesImplementations;

import com.example.auth_service.dto.AuthRequestDto;
import com.example.auth_service.dto.AuthResponseDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;
import com.example.auth_service.globalExceptionHandler.customExceptions.InvalidLoginCredentialsException;
import com.example.auth_service.mappers.UserMapper;
import com.example.auth_service.models.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.jwt.JwtService;
import com.example.auth_service.services.serviceInterfaces.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationProvider authenticationProvider,
                           JwtService jwtService
                           ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        if(checkIfUserExists(registerRequestDto.email())){
            throw new IllegalArgumentException("User already exists with email: "+registerRequestDto.email());
        }

        User newUser = UserMapper.toUser(registerRequestDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return UserMapper.toRegisterResponseDto(userRepository.save(newUser));
    }

    @Override
    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        Authentication authentication =  authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password()));
        logger.info("Authenticating: "+requestDto.email());
        if(authentication.isAuthenticated()){
            return AuthResponseDto
                    .builder()
                    .email(requestDto.email())
                    .token(jwtService.generateToken(requestDto.email()))
                    .build();

        }
        throw new InvalidLoginCredentialsException("Invalid Credentials");
    }


    private boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
