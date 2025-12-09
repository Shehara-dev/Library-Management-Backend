package com.library.librarymanagement.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Integer userId;
    private Integer bookId;
    private Integer days; // 7, 14, or 21 days
}