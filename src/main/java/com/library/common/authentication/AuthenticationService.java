package com.library.common.authentication;

import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.models.AccessTokenResponseDto;
import com.library.common.authentication.models.RegisterUserDto;
import com.library.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    User registerUser(@Valid RegisterUserDto registerUserDto);

    AccessTokenResponseDto getAccessToken(@Valid AccessTokenRequestDto accessTokenRequestDto);
}
