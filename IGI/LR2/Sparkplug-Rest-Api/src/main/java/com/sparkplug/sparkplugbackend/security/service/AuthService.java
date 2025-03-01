package com.sparkplug.sparkplugbackend.security.service;

import com.sparkplug.sparkplugbackend.exception.UserAlreadyExistsException;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.security.dto.AuthResponseDto;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final int ENCODER_STRENGTH = 12;

    private final SparkplugUsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            SparkplugUsersRepository usersRepository,
            AuthenticationManager authManager,
            JwtService jwtService) {
        this.usersRepository = usersRepository;
        this.authenticationManager = authManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDto register(String username, String password) {
        if(usersRepository.findByUsername(username).isPresent())
            throw new UserAlreadyExistsException("User with this name already exists.");

        SparkplugUser user = new SparkplugUser();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder(ENCODER_STRENGTH).encode(password));
        user.setAuthority("USER");

        String token = jwtService.generateToken(username);

        user = usersRepository.save(user);
        return new AuthResponseDto(
                user.getId(), user.getUsername(), user.getAuthority(), token);
    }

    public AuthResponseDto verify(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtService.generateToken(username);
        SparkplugUser user = usersRepository
                .findByUsername(username).orElseThrow(()-> new RuntimeException("User not found."));

        return new AuthResponseDto(
                user.getId(), user.getUsername(), user.getAuthority(), token);
    }

    public AuthResponseDto verify(SparkplugUserDetails userDetails) {
        String username = userDetails.getUsername();
        String token = jwtService.generateToken(username);
        SparkplugUser user = usersRepository
                .findByUsername(username).orElseThrow(()-> new RuntimeException("User not found."));

        return new AuthResponseDto(
                user.getId(), user.getUsername(), user.getAuthority(), token);
    }
}
