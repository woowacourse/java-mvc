package nextstep.mvc.returntype;

import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.ModelAndView;

public class ModelAndViewReturnTypeResolver implements ReturnTypeResolver {

    @Override
    public boolean supportsReturnType(MethodHandler methodHandler) {
        return methodHandler.sameReturnTypeWith(ModelAndView.class);
    }

    @Override
    public ModelAndView resolve(Object returnValue) {
        return (ModelAndView) returnValue;
    }
}
