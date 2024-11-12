package com.library.web.controllers;

import com.library.domain.patron.Patron;
import com.library.domain.patron.PatronService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;


    @GetMapping
    ResponseEntity<List<Patron>> getPatrons() {
        return ResponseEntity.ok(patronService.getPatrons());
    }

    @GetMapping("/pagination")
    ResponseEntity<Page<Patron>> getPatronsWithPagination(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(patronService.getPatronsWithPagination(pageNumber, pageSize));
    }

    @PostMapping
    ResponseEntity<Patron> addPatron(@Valid @RequestBody Patron Patron) {
        return new ResponseEntity<>(patronService.addPatron(Patron), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Patron> getPatron(@PathVariable Long id) {
        return ResponseEntity.ok(patronService.getPatron(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron Patron) {
        return new ResponseEntity<>(patronService.updatePatron(id, Patron), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Patron> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
