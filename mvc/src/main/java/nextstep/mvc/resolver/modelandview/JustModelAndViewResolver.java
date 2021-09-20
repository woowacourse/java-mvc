package nextstep.mvc.resolver.modelandview;

import nextstep.mvc.view.ModelAndView;

public class JustModelAndViewResolver implements ModelAndViewResolver{

    @Override
    public boolean isSupport(Class<?> returnType) {
        return returnType.equals(ModelAndView.class);
    }

    @Override
    public ModelAndView chooseProperModelAndView(Object modelAndView) {
        return (ModelAndView) modelAndView;
    }
}
