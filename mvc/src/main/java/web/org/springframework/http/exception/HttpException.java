package web.org.springframework.http.exception;

public class HttpException extends RuntimeException {

    protected int statusCode;
    protected String message;
}
