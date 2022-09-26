package nextstep.mvc;

import nextstep.mvc.view.ModelAndView;

public interface ModelAndViewResolver {
    boolean supports(Object object);

    ModelAndView resolve(Object object);
}
