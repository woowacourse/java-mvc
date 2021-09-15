package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    public JsonView() {
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 응답의 Content-Type을 지정한다.
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // PrintWriter는 웹으로 데이터를 출력하기 위해 사용된다.
        final PrintWriter out = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
        log.debug("Render To Json\n {}", json);

        out.print(json);
    }
}
