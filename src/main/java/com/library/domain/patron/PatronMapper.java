package com.library.domain.patron;

import com.library.domain.common.enums.PatronStatus;
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
}
