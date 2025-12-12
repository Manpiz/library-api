package com.library.service;

import com.library.dto.BorrowerResponse;
import com.library.dto.CreateBorrowerRequest;
import com.library.exception.BadRequestException;
import com.library.exception.NotFoundException;
import com.library.model.Borrower;
import com.library.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Transactional
    public BorrowerResponse register(CreateBorrowerRequest request) {
        borrowerRepository.findByEmail(request.getEmail()).ifPresent(b -> {
            throw new BadRequestException("Email already registered");
        });

        Borrower borrower = new Borrower();
        borrower.setName(request.getName());
        borrower.setEmail(request.getEmail());

        Borrower saved = borrowerRepository.save(borrower);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Borrower getBorrower(Long borrowerId) {
        return borrowerRepository.findById(borrowerId)
            .orElseThrow(() -> new NotFoundException("Borrower not found"));
    }

    private BorrowerResponse toResponse(Borrower borrower) {
        return new BorrowerResponse(borrower.getId(), borrower.getName(), borrower.getEmail());
    }
}

