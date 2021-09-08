package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewResolver {

    private ViewResolver() {}

    public static void resolve(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
