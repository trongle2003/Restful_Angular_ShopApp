package com.project.shopapp.domain.response;

public class RestResponse<T> {
    private int statusCode;
    private String error;

    // message có thể là String , arraylist hoặc object
    private Object message;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public static <T> RestResponse<T> success(Object message, T data) {
//        RestResponse<T> response = new RestResponse<>();
//        response.setStatusCode(200);
//        response.setError(null);
//        response.setMessage(message);
//        response.setData(data);
//        return response;
//    }
//
//    public static <T> RestResponse<T> error(String error, Object message, int statusCode) {
//        RestResponse<T> response = new RestResponse<>();
//        response.setStatusCode(statusCode);
//        response.setError(error);
//        response.setMessage(message);
//        response.setData(null);
//        return response;
//    }


}
