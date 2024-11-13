package com.library.domain.patron;

import com.library.domain.patron.models.CreatePatronDto;
import com.library.domain.patron.models.Patron;
import com.library.domain.patron.models.UpdatePatronDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PatronService {

    Page<Patron> getPatronsWithPagination(int pageNumber, int pageSize);

    Patron getPatron(Long id);

    Patron addPatron(@Valid CreatePatronDto createPatronDto);

    Patron updatePatron(Long id, @Valid UpdatePatronDto updatePatronDto);

    void deletePatron(Long id);

    PatronEntity checkPatronIsAllowedToBorrow(Long patronId);
}
