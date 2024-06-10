package com.dalhousie.servicehub.util;

/**
 * Base response body wrapper to define fixed format for response of an API
 * @param resultType Result type indicating success or failure
 * @param data Data that is sent to frontend
 * @param message Additional message/description indicating result for the API
 * @param <T> Generic type that changes based on the return expectancy
 * Example: <br>
 * <pre> {@code
 * {
 *     "resultType": "SUCCESS",
 *     "data": {
 *         "id": 6,
 *         "name": "Vraj",
 *         "email": "abc@gmail.com",
 *         "age": 24
 *     },
 *     "message": "User added successfully"
 * }
 * } </pre>
 */
public record ResponseBody<T>(ResultType resultType, T data, String message) {
    public enum ResultType {
        SUCCESS,
        FAILURE
    }
}
