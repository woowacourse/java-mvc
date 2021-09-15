package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.PrintWriter;
import java.util.Map;

public class TextView implements View {

    private final String text;

    public TextView(String text) {
        this.text = text;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.TEXT_PLAIN);
        final PrintWriter writer = response.getWriter();
        writer.write(text);
    }
}
