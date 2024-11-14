package com.library.common.authentication;

import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.models.AccessTokenResponseDto;
import com.library.common.authentication.models.RegisterUserDto;
import com.library.common.security.jwt.JwtService;
import com.library.domain.user.User;
import com.library.domain.user.UserEntity;
import com.library.domain.user.UserMapper;
import com.library.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public User registerUser(RegisterUserDto registerUserDto) {
        UserEntity userEntity = userRepository.save(
                userMapper.registerUserDtoToUserEntity(registerUserDto));
        return userMapper.userEntityToUSer(userEntity);
    }

    @Override
    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                accessTokenRequestDto.email(), accessTokenRequestDto.password()));

        UserEntity userEntity = userRepository.findByEmail(accessTokenRequestDto.email())
                .orElseThrow(() -> new UsernameNotFoundException("User with username: "
                        + accessTokenRequestDto.email() + " not found."));

        String accessToken = jwtService.generateToken(userEntity);
        return new AccessTokenResponseDto(accessToken);
    }
}
