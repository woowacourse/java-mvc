package nextstep.mvc.resolver.modelandview;

import nextstep.mvc.controller.ResponseEntity;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;

public class ResponseEntityToJsonModelAndViewResolver implements ModelAndViewResolver {

    @Override
    public boolean isSupport(Class<?> returnType) {
        return returnType.equals(ResponseEntity.class);
    }

    @Override
    public ModelAndView chooseProperModelAndView(Object obj) {
        ResponseEntity result = (ResponseEntity) obj;
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("data", result.getValue());
        return modelAndView;
    }
}
