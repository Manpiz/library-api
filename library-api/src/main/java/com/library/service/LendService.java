package com.library.service;

import com.library.dto.LendResponse;
import com.library.exception.BadRequestException;
import com.library.exception.NotFoundException;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Lend;
import com.library.repository.LendRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Minimal service to borrow and return a specific book copy.
@Service
@RequiredArgsConstructor
public class LendService {

    private final LendRepository lendRepository;
    private final BorrowerService borrowerService;
    private final BookService bookService;

    @Transactional
    public LendResponse borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerService.getBorrower(borrowerId);
        Book book = bookService.getBook(bookId);

        lendRepository.findByBookIdAndReturnedAtIsNull(bookId).ifPresent(existing -> {
            throw new BadRequestException("Book is already borrowed");
        });

        Lend lend = new Lend();
        lend.setBorrower(borrower);
        lend.setBook(book);
        lend.setBorrowedAt(LocalDateTime.now());

        Lend saved = lendRepository.save(lend);
        return toResponse(saved);
    }

    @Transactional
    public LendResponse returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerService.getBorrower(borrowerId);
        bookService.getBook(bookId); // ensure book exists

        Lend lend = lendRepository.findByBookIdAndBorrowerIdAndReturnedAtIsNull(bookId, borrower.getId())
            .orElseThrow(() -> new NotFoundException("Active lend not found for this borrower and book"));

        lend.setReturnedAt(LocalDateTime.now());
        Lend saved = lendRepository.save(lend);
        return toResponse(saved);
    }

    private LendResponse toResponse(Lend lend) {
        return new LendResponse(
            lend.getId(),
            lend.getBorrower().getId(),
            lend.getBook().getId(),
            lend.getBorrowedAt(),
            lend.getReturnedAt()
        );
    }
}

