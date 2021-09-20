package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.support.JsonParserUtils;

import java.util.stream.Collectors;

public class JsonModelMapper {

    private JsonModelMapper() {
    }

    public static String parse(Model model) {
        try {
            if (model.size() <= 1) {
                return joiningValues(model);
            }
            return JsonParserUtils.toJson(model.asMap());
        } catch (JsonProcessingException e) {
            throw new UnHandledRequestException("Json 파싱 에러");
        }
    }

    private static String joiningValues(Model model) {
        return model.asMap().values().stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
