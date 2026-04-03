package com.Ecom.Dtos;


public record ValidationErrorResponse(
        String field,
        String message
) {
}