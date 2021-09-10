package nextstep.mvc.returntype;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.ModelAndView;

public class ReturnTypeResolverContainer {

    private final List<ReturnTypeResolver> returnTypeResolvers = new ArrayList<>();

    public ReturnTypeResolverContainer() {
        defaultReturnTypeResolvers();
    }

    private void defaultReturnTypeResolvers() {
        returnTypeResolvers.add(new JsonReturnTypeResolver());
        returnTypeResolvers.add(new JspReturnViewResolver());
        returnTypeResolvers.add(new ModelAndViewReturnTypeResolver());
    }

    public ModelAndView resolve(MethodHandler methodHandler, Object returnValue) {
        return returnTypeResolvers.stream()
            .filter(returnTypeResolver -> returnTypeResolver.supportsReturnType(methodHandler))
            .findAny()
            .orElseThrow(() -> new IllegalStateException("not supported return value"))
            .resolve(returnValue);
    }
}
