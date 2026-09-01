package com.ipauno.api;

import jakarta.validation.constraints.NotBlank;

public record PlayCardRequest(@NotBlank(message = "cardId is required") String cardId) {
}
