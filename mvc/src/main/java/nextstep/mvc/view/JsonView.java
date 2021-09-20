package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;

public class JsonView implements View {

    @Override
    public void render(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = JsonModelMapper.parse(model);

        response.getWriter().write(json);
    }
}
