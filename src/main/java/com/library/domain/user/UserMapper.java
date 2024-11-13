package com.library.domain.user;

import com.library.common.authentication.models.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity registerUserDtoToUserEntity(RegisterUserDto registerUserDto) {
        return UserEntity.builder()
                .email(registerUserDto.email())
                .firstName(registerUserDto.firstName())
                .lastName(registerUserDto.lastName())
                .password(passwordEncoder.encode(registerUserDto.password()))
                .build();
    }

    public User userEntityToUSer(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName());
    }

}
