package dev.devature.penguin_api.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Used for response to the controller from the service layer.
 * @param <T> Take in a data to return with the API Response
 */
@Setter
@Getter
public class ApiResponse<T> {
    private HttpStatus statusCode;
    private String message;
    private T data;

    public ApiResponse(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public boolean isPresent(){
        return data != null;
    }
}
