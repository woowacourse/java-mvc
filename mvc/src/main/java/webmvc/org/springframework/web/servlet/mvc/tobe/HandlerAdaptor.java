package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class HandlerAdaptor implements Controller {

    private final HandlerExecution handlerExecution;

    public HandlerAdaptor(HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public Object execute(HttpServletRequest req, HttpServletResponse res) {
        return handlerExecution.handle(req, res);
    }
}
