package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.DataNotExistException;
import nextstep.web.support.MediaType;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (model == null) {
            throw new DataNotExistException();
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final ObjectMapper objectMapper = new ObjectMapper();
        final Object convertedModel = convertToWriteModel(model);
        objectMapper.writeValue(response.getOutputStream(), convertedModel);
    }

    private Object convertToWriteModel(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values()
                    .toArray()[0];
        }
        return model;
    }
}
