package com.github.alfredobaptista.infrastructure.dto.response;

public record ApiResponse<T>(
        boolean success,       // ✅ typo corrigido: "sucess" → "success"
        String message,
        T data
) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
