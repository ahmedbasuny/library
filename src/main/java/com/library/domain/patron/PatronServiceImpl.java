package com.library.domain.patron;

import com.library.common.enums.PatronStatus;
import com.library.domain.patron.exception.PatronNotAllowedToBorrowBooksException;
import com.library.domain.patron.exception.PatronNotFoundException;
import com.library.domain.patron.models.CreatePatronDto;
import com.library.domain.patron.models.Patron;
import com.library.domain.patron.models.UpdatePatronDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Patron> getPatronsWithPagination(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return patronRepository.findAll(pageable).map(PatronMapper::patronEntityToPatron);
    }

    @Override
    @Cacheable(value = "patrons", key = "#id")
    @Transactional(readOnly = true)
    public Patron getPatron(Long id) {
        PatronEntity patronEntity = getPatronEntityById(id);
        return PatronMapper.patronEntityToPatron(patronEntity);
    }

    @Override
    @Transactional
    public Patron addPatron(@Valid CreatePatronDto createPatronDto) {
        PatronEntity patronEntity = patronRepository.save(
                PatronMapper.createPatronDtoToPatronEntity(createPatronDto));
        return PatronMapper.patronEntityToPatron(patronEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "patrons", key = "#id")
    public Patron updatePatron(Long id, @Valid UpdatePatronDto updatePatronDto) {
        PatronEntity patronEntity = getPatronEntityById(id);
        patronEntity.setName(updatePatronDto.name());
        patronEntity.setMobile(updatePatronDto.mobile());
        patronEntity.setEmail(updatePatronDto.email());
        patronEntity.setAddress(updatePatronDto.address());
        patronEntity.setMembershipDate(updatePatronDto.membershipDate());
        patronEntity.setStatus(updatePatronDto.status().name());

        PatronEntity updatePatronEntity = patronRepository.save(patronEntity);
        return PatronMapper.patronEntityToPatron(updatePatronEntity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "patrons", key = "#id")
    public void deletePatron(Long id) {
        PatronEntity patronEntity = getPatronEntityById(id);
        patronRepository.delete(patronEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PatronEntity checkPatronIsAllowedToBorrow(Long patronId) {
        PatronEntity patronEntity = getPatronEntityById(patronId);
        if (PatronStatus.ACTIVE.name().equalsIgnoreCase(patronEntity.getStatus())) {
            return patronEntity;
        } else {
            throw PatronNotAllowedToBorrowBooksException.forId(patronId);
        }
    }

    private PatronEntity getPatronEntityById(Long id) {
        return patronRepository.findById(id).orElseThrow(
                () -> PatronNotFoundException.forId(id));
    }
}
