package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    public static final String JSP_FILE_TYPE = ".jsp";

    private final HandlerMethod handlerMethod;

    public HandlerExecution(final HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        if (isJspResourceReturn(result)) {
            JspView jspView = new JspView((String) result);
            ModelAndView modelAndView = new ModelAndView(jspView);
            setAttribute(request, modelAndView);
            return modelAndView;
        }
        throw new RuntimeException("[ERROR] HandlerAdapter 가 처리할 수 없는 Handler 의 결과입니다.");
    }

    private boolean isJspResourceReturn(final Object result) {
        return result instanceof String && ((String) result).contains(JSP_FILE_TYPE);
    }

    private void setAttribute(final HttpServletRequest request, final ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            modelAndView.addObject(attributeName, request.getAttribute(attributeName));
        }
    }
}
