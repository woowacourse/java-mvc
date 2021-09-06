package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.support.JsonParserUtils;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = JsonParserUtils.toJson(model.asMap());

        response.getWriter().write(json);
    }
}
