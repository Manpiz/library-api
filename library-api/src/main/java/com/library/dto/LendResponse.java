package com.library.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendResponse {
    private Long id;
    private Long borrowerId;
    private Long bookId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
}

