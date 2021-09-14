package nextstep.mvc.returntype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.StatusCode;

public class JsonReturnTypeResolver implements ReturnTypeResolver {

    private final ObjectMapper objectMapper;

    public JsonReturnTypeResolver() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean supportsReturnType(MethodHandler methodHandler) {
        return methodHandler.isAnnotationPresents(ResponseBody.class);
    }

    @Override
    public ModelAndView resolve(Object returnValue) {
        if (returnValue instanceof String) {
            return new ModelAndView(new JsonView((String) returnValue, StatusCode.OK));
        }

        if (returnValue instanceof ResponseEntity) {
            try {
                final ResponseEntity responseEntity = (ResponseEntity) returnValue;
                String body = "";
                if (responseEntity.getBody() != null) {
                    body = objectMapper.writeValueAsString(responseEntity.getBody());
                }
                return new ModelAndView(new JsonView(body, responseEntity.getStatusCode()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("not supported return type for response body");
    }
}
