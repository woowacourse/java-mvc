package nextstep.mvc.returntype;

import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;

public class JsonReturnTypeResolver implements ReturnTypeResolver{

    @Override
    public boolean supportsReturnType(MethodHandler methodHandler) {
        return methodHandler.isAnnotationPresents(ResponseBody.class);
    }

    @Override
    public ModelAndView resolve(Object returnValue) {
        return new ModelAndView(new JsonView((String) returnValue));
    }
}
