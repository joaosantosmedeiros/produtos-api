package com.example.produtosapi.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRecordDto(@NotBlank String name, @Positive @NotNull BigDecimal value) {
  
}
