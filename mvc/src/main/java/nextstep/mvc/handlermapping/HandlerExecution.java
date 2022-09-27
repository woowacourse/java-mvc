package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final HandlerMethod handlerMethod;

    public HandlerExecution(final HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handlerMethod.invoke(request, response);
    }
}
