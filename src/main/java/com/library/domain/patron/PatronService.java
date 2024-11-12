package com.library.domain.patron;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatronService {

    List<Patron> getPatrons();

    Page<Patron> getPatronsWithPagination(int pageNumber, int pageSize);

    Patron getPatron(Long id);

    Patron addPatron(Patron Patron);

    Patron updatePatron(Long id, Patron patron);

    void deletePatron(Long id);

    PatronEntity checkPatronIsAllowedToBorrow(Long patronId);
}
