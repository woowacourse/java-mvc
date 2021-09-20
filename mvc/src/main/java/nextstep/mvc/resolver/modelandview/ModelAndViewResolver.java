package nextstep.mvc.resolver.modelandview;

import nextstep.mvc.view.ModelAndView;

public interface ModelAndViewResolver {

    boolean isSupport(Class<?> returnType);

    ModelAndView chooseProperModelAndView(Object obj);
}
