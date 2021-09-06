package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        String jsonData = new ObjectMapper().writeValueAsString(modelAndView.getModel());
        PrintWriter writer = response.getWriter();

        writer.print(jsonData);
        writer.flush();
    }
}
