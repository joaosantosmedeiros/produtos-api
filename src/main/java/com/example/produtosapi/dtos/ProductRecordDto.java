package com.example.produtosapi.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRecordDto(@NotBlank String name, @Positive BigDecimal value) {
  
}
