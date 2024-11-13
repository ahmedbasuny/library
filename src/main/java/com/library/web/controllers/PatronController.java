package com.library.web.controllers;

import com.library.domain.patron.models.CreatePatronDto;
import com.library.domain.patron.models.Patron;
import com.library.domain.patron.PatronService;
import com.library.domain.patron.models.UpdatePatronDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    @GetMapping
    ResponseEntity<Page<Patron>> getPatronsWithPagination(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(patronService.getPatronsWithPagination(pageNumber, pageSize));
    }

    @PostMapping
    ResponseEntity<Patron> addPatron(@Valid @RequestBody CreatePatronDto createPatronDto) {
        return new ResponseEntity<>(patronService.addPatron(createPatronDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Patron> getPatron(@PathVariable Long id) {
        return ResponseEntity.ok(patronService.getPatron(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody UpdatePatronDto updatePatronDto) {
        return new ResponseEntity<>(patronService.updatePatron(id, updatePatronDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Patron> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
