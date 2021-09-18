package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewResolver {

    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private ViewResolver() {}

    public static void resolve(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    public static void resolveJsp(String viewName, HttpServletRequest request, HttpServletResponse response) {
        try {
            JspView jspView = new JspView(viewName);
            jspView.render(Collections.emptyMap(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}
