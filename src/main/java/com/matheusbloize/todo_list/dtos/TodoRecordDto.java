package com.matheusbloize.todo_list.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoRecordDto(@NotBlank String name, @NotBlank String description, @NotNull boolean done, @Min(value = 1) @Max(value = 3) int priority) {

}
