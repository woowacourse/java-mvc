package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

import com.sun.jdi.InternalException;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.ExceptionHandler;

public class ExceptionResolver {

    private final List<ExceptionHandler> handlers;

    public ExceptionResolver(List<ExceptionHandler> handlers) {
        this.handlers = handlers;
    }

    public ModelAndView handle(Throwable ex) {
        ExceptionHandler handler = handlers.stream()
            .filter(it -> it.support(ex))
            .findFirst()
            .orElseThrow(() -> new InternalException());
        return handler.handle();
    }
}

