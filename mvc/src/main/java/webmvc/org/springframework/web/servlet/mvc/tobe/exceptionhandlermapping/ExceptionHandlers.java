package webmvc.org.springframework.web.servlet.mvc.tobe.exceptionhandlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExceptionHandlers {

    private final List<ExceptionHandler> exceptionHandlers = new ArrayList<>();

    public void initialize() {
        for (ExceptionHandler exceptionHandler : exceptionHandlers) {
            exceptionHandler.initialize();
        }
    }

    public void addExceptionHandlerMapping(final ExceptionHandler exceptionHandler) {
        exceptionHandlers.add(exceptionHandler);
    }

    public Optional<ExceptionHandler> getExceptionHandler(final HttpServletRequest request,
                                                          final HttpServletResponse response) {
        return exceptionHandlers.stream()
            .filter(handlerMapping -> handlerMapping.isHandleable(request, response))
            .findFirst();
    }
}
