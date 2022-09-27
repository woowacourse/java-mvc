package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();


    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            Entry<String, ?> entry = new ArrayList<Entry<String, ?>>(model.entrySet()).get(0);
            String writeValueAsString = MAPPER.writeValueAsString(entry.getValue());
            log.info(writeValueAsString);
            writer.write(writeValueAsString);
            return;
        }
        writer.write(MAPPER.writeValueAsString(model));
    }

    @Override
    public void show(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
