package nextstep.mvc.returntype;

import java.util.HashMap;
import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.StatusCode;

public class JsonReturnTypeResolver implements ReturnTypeResolver {

    @Override
    public boolean supportsReturnType(MethodHandler methodHandler) {
        return methodHandler.isAnnotationPresents(ResponseBody.class);
    }

    @Override
    public ModelAndView resolve(Object returnValue) {
        final HashMap<String, Object> model = new HashMap<>();
        StatusCode statusCode = StatusCode.OK;
        model.put("data", returnValue);
        if (returnValue instanceof ResponseEntity) {
            final ResponseEntity responseEntity = (ResponseEntity) returnValue;
            statusCode = responseEntity.getStatusCode();
            model.put("data", responseEntity.getBody());
        }
        return new ModelAndView(new JsonView(model, statusCode));
    }
}
