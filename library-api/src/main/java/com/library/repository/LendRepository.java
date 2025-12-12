package com.library.repository;

import com.library.model.Lend;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Simple repository for lend records (one row per borrowed book copy).
public interface LendRepository extends JpaRepository<Lend, Long> {
    Optional<Lend> findByBookIdAndReturnedAtIsNull(Long bookId);
    Optional<Lend> findByBookIdAndBorrowerIdAndReturnedAtIsNull(Long bookId, Long borrowerId);
}

