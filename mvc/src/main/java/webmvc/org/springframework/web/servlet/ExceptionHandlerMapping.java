package webmvc.org.springframework.web.servlet;

public interface ExceptionHandlerMapping {
    void initialize();

    Object getHandler(final Class<? extends Throwable> request);
}
