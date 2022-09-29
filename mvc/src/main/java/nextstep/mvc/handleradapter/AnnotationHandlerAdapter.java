package nextstep.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private static final String JSP_FILE_TYPE = ".jsp";

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) throws Exception {
        Object result = ((HandlerExecution) handler).handle(request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        if (isJspResource(result)) {
            JspView jspView = new JspView((String) result);
            ModelAndView modelAndView = new ModelAndView(jspView);
            setAttribute(request, modelAndView);
            return modelAndView;
        }
        throw new RuntimeException("[ERROR] HandlerAdapter 가 Handler 의 결과를 처리할 수 없습니다.");
    }

    private boolean isJspResource(final Object result) {
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
