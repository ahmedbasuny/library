package com.library.web.controllers;

import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.AuthenticationService;
import com.library.common.authentication.models.AccessTokenResponseDto;
import com.library.common.authentication.models.RegisterUserDto;
import com.library.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    ResponseEntity<User> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return new ResponseEntity<>(
                authenticationService.registerUser(registerUserDto), HttpStatus.CREATED);
    }

    @PostMapping("/access-token")
    ResponseEntity<AccessTokenResponseDto> getAccessToken(
            @Valid @RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        return new ResponseEntity<>(
                authenticationService.getAccessToken(accessTokenRequestDto), HttpStatus.CREATED);
    }
}
