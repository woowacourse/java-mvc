package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final HandlerMethod handlerMethod;

    public HandlerExecution(final HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // 핸들러 메서드의 매개변수의 형태에 따라 invoke() 의 파라미터 구성도 변하기 때문에 상황에 따라 분기하여 적절하게 넣어주는 로직이 필요하다.
        return handlerMethod.invoke(request, response);
    }
}
