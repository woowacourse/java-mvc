package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

public class ViewResolver {

    private ViewResolver() {}

    public static void resolve(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    public static void resolve(String viewName, HttpServletRequest request, HttpServletResponse response) {
        try {
            JspView jspView = new JspView(viewName);
            jspView.render(Collections.emptyMap(), request, response);
        } catch(Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
