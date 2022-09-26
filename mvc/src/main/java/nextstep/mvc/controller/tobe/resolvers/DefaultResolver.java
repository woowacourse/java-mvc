package nextstep.mvc.controller.tobe.resolvers;

import nextstep.mvc.ModelAndViewResolver;
import nextstep.mvc.view.ModelAndView;

public class DefaultResolver implements ModelAndViewResolver {
    @Override
    public boolean supports(Object instance) {
        return instance instanceof ModelAndView;
    }

    @Override
    public ModelAndView resolve(Object object) {
        return (ModelAndView) object;
    }
}
