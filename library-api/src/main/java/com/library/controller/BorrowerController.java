package com.library.controller;

import com.library.dto.BorrowBookRequest;
import com.library.dto.BorrowerResponse;
import com.library.dto.CreateBorrowerRequest;
import com.library.dto.LendResponse;
import com.library.dto.ReturnBookRequest;
import com.library.service.BorrowerService;
import com.library.service.LendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final LendService lendService;

    //handle the borrower registration & borrow/return book requests
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowerResponse register(@Valid @RequestBody CreateBorrowerRequest request) {
        return borrowerService.register(request);
    }

    @PostMapping("/{borrowerId}/borrowing")
    @ResponseStatus(HttpStatus.CREATED)
    public LendResponse borrow(
        @PathVariable Long borrowerId,
        @Valid @RequestBody BorrowBookRequest request
    ) {
        return lendService.borrowBook(borrowerId, request.getBookId()); 
    }

    @PostMapping("/{borrowerId}/returns")
    public LendResponse returnBook(
        @PathVariable Long borrowerId,
        @Valid @RequestBody ReturnBookRequest request
    ) {
        return lendService.returnBook(borrowerId, request.getBookId());
    }
}

