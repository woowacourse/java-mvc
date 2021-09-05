package nextstep.mvc.returntype;

import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.ModelAndView;

public interface ReturnTypeResolver {

    boolean supportsReturnType(MethodHandler methodHandler);

    ModelAndView resolve(Object returnValue);
}
