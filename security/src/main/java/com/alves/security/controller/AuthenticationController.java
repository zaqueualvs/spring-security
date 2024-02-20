package com.alves.security.controller;

import com.alves.security.infra.security.TokenService;
import com.alves.security.model.user.LoginResponse;
import com.alves.security.model.user.User;
import com.alves.security.model.user.UserAuthenticationRequest;
import com.alves.security.model.user.UserRegisterRequest;
import com.alves.security.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserAuthenticationRequest userAuthenticationRequest) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(userAuthenticationRequest.getLogin(), userAuthenticationRequest.getPassword());
        var auth = this.authenticationManager.authenticate(userNamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        if (this.userRepository.findByLogin(userRegisterRequest.getLogin()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterRequest.getPassword());
        User user = new User(
                userRegisterRequest.getLogin(),
                encryptedPassword,
                userRegisterRequest.getRole()
        );

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
