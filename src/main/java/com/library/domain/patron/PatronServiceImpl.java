package com.library.domain.patron;

import com.library.domain.common.enums.PatronStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Override
    public Page<Patron> getPatronsWithPagination(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return patronRepository.findAll(pageable).map(PatronMapper::patronEntityToPatron);
    }

    @Override
    public List<Patron> getPatrons() {
        return patronRepository.findAll().stream().map(
                PatronMapper::patronEntityToPatron).toList();
    }

    @Override
    public Patron getPatron(Long id) {
        PatronEntity patronEntity = getPatronEntityById(id);
        return PatronMapper.patronEntityToPatron(patronEntity);
    }

    @Override
    @Transactional
    public Patron addPatron(Patron patron) {
        PatronEntity patronEntity = patronRepository.save(PatronMapper.patronToPatronEntity(patron));
        return PatronMapper.patronEntityToPatron(patronEntity);
    }

    @Override
    @Transactional
    @Modifying
    public Patron updatePatron(Long id, Patron patron) {
        PatronEntity patronEntity = getPatronEntityById(id);
        patronEntity.setName(patron.name());
        patronEntity.setMobile(patron.mobile());
        patronEntity.setEmail(patron.email());
        patronEntity.setAddress(patron.address());
        patronEntity.setMembershipDate(patron.membershipDate());
        patronEntity.setStatus(patron.status().name());

        PatronEntity updatePatronEntity = patronRepository.save(patronEntity);
        return PatronMapper.patronEntityToPatron(updatePatronEntity);
    }

    @Override
    @Transactional
    @Modifying
    public void deletePatron(Long id) {
        PatronEntity patronEntity = getPatronEntityById(id);
        patronRepository.delete(patronEntity);
    }

    @Override
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
