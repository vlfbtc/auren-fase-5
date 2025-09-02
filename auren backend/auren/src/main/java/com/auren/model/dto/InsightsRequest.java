package com.auren.model.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class InsightsRequest {
    private LocalDate from; // opcional
    private LocalDate to;   // opcional
}
