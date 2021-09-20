package nextstep.mvc.view.viewresolver;

import java.util.Map;
import nextstep.mvc.controller.ResponseEntity;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;

public class JsonViewResolver implements ViewResolver {

    @Override
    public boolean support(Class<?> returnType) {
        return returnType.equals(ResponseEntity.class);
    }

    @Override
    public View resolve(Object object) {
        ResponseEntity responseEntity = (ResponseEntity) object;
        return new JsonView(Map.of("data", responseEntity.getValue()));
    }
}
