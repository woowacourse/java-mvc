package webmvc.org.springframework.web.servlet.mvc;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(Exception e) {
        super(e);
    }
}
