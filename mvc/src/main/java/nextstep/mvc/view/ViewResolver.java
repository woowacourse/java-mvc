package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewResolver {

    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private ViewResolver() {}

    public static void resolve(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final ModelAndView modelAndView) {
        View view = modelAndView.getView();
        try {
            view.render(modelAndView.getModel(), request, response);
        }  catch (Throwable e) {
            log.debug("Exception Render : {}", e.getMessage(), e);
        }
    }
}
