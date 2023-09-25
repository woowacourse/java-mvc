package webmvc.org.springframework.web.servlet.view;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = new ObjectMapper().writeValueAsString(model);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);

//        try (PrintWriter writer = response.getWriter()) {
//            writer.write(body);
//            writer.flush();
//        } catch (Exception e) {
//            log.debug("JsonView#render 중 예외 발생 !!");
//        }
    }
}
