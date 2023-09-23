package webmvc.org.springframework.web.servlet.exception;

public class HandlerNotFoundException extends RuntimeException{

    public HandlerNotFoundException(String e) {
        super(e);
    }
}
