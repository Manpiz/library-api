package com.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnBookRequest {

    @NotNull(message = "Book id is required")
    private Long bookId;
}

