package webmvc.org.springframework.web.servlet.mvc.asis.servlet;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(final Exception e) {
        super(e);
    }
}
