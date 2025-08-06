package dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {

    public ApiResponse(T data, boolean success) {
        this(success, "", data);
    }

    public ApiResponse(boolean success) {
        this(success, "", null);
    }

    public ApiResponse(T data) {
        this(false, "", data);
    }
    public ApiResponse(boolean success, String message) {
        this(success, message, null);
    }
}
