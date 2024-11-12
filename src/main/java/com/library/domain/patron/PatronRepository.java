package com.library.domain.patron;

import org.springframework.data.jpa.repository.JpaRepository;

interface PatronRepository extends JpaRepository<PatronEntity, Long> {
}
