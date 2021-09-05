package nextstep.mvc.returntype;

import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class JspReturnViewResolver implements ReturnTypeResolver {

    @Override
    public boolean supportsReturnType(MethodHandler methodHandler) {
        return !methodHandler.isAnnotationPresents(ResponseBody.class) && methodHandler.isReturnType(String.class);
    }

    @Override
    public ModelAndView resolve(Object returnValue) {
        return new ModelAndView(new JspView((String) returnValue));
    }
}
