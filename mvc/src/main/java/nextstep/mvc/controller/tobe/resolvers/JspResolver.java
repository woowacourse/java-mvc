package nextstep.mvc.controller.tobe.resolvers;

import nextstep.mvc.ModelAndViewResolver;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class JspResolver implements ModelAndViewResolver {
    @Override
    public boolean supports(Object object) {
        return object instanceof String;
    }

    @Override
    public ModelAndView resolve(Object object) {
        String viewName = (String) object;
        return new ModelAndView(new JspView(viewName));
    }
}
