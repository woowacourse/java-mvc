package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    public JsonView() {
    }

    @Override
    public void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        String jsonData = new ObjectMapper().writeValueAsString(modelAndView.getModel());
        PrintWriter writer = response.getWriter();

        writer.print(jsonData);
        writer.flush();
    }
}
