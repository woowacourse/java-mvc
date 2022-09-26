package nextstep.mvc.controller.tobe.resolvers;

import java.util.List;
import nextstep.mvc.ModelAndViewResolver;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;

public class JsonResolver implements ModelAndViewResolver {

    private static final String CURRENT_BASE_PACKAGE = "nextstep";

    @Override
    public boolean supports(Object object) {
        return !object.getClass().getPackageName().startsWith(CURRENT_BASE_PACKAGE);
    }

    @Override
    public ModelAndView resolve(Object object) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject(object.getClass().getSimpleName(), object);
        return modelAndView;
    }
}
