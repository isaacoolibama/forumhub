package br.com.alura.forum.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;
    
    // Construtor para sucesso
    public static <T> ApiResponse<T> success(String message, T data, String path) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), path);
    }
    
    // Construtor para sucesso sem dados
    public static <T> ApiResponse<T> success(String message, String path) {
        return new ApiResponse<>(true, message, null, LocalDateTime.now(), path);
    }
    
    // Construtor para erro
    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), path);
    }
    
    // Construtor para erro com dados
    public static <T> ApiResponse<T> error(String message, T data, String path) {
        return new ApiResponse<>(false, message, data, LocalDateTime.now(), path);
    }
}
