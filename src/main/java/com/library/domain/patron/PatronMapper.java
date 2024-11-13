package com.library.domain.patron;

import com.library.common.enums.PatronStatus;
import com.library.domain.patron.models.CreatePatronDto;
import com.library.domain.patron.models.Patron;
import org.springframework.stereotype.Component;

@Component
public class PatronMapper {

    public static Patron patronEntityToPatron(PatronEntity patronEntity) {
        return new Patron(
                patronEntity.getId(),
                patronEntity.getName(),
                patronEntity.getMobile(),
                patronEntity.getEmail(),
                patronEntity.getAddress(),
                patronEntity.getMembershipDate(),
                PatronStatus.valueOf(patronEntity.getStatus())
        );
    }

    public static PatronEntity patronToPatronEntity(Patron patron) {
        return PatronEntity.builder()
                .name(patron.name())
                .mobile(patron.mobile())
                .email(patron.email())
                .address(patron.address())
                .membershipDate(patron.membershipDate())
                .status(patron.status().name())
                .build();
    }

    public static PatronEntity createPatronDtoToPatronEntity(CreatePatronDto createPatronDto) {
        return PatronEntity.builder()
                .name(createPatronDto.name())
                .mobile(createPatronDto.mobile())
                .email(createPatronDto.email())
                .address(createPatronDto.address())
                .membershipDate(createPatronDto.membershipDate())
                .status(createPatronDto.status().name())
                .build();
    }
}
