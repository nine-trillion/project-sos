package team.project.sos.common.response;

import lombok.Getter;

/**
 * 공통 응답 포맷
 * ex)
 * {
 * "message" : "",
 * "data" : {...}
 * }
 * @param <T>
 */
@Getter
public class ApiResponse<T> {

    private final String message;
    private final T data;

    private ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(String message, T data){
        return new ApiResponse<>(message,data);
    }

}
