package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Map;

public class JsonView implements View {

    static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Writer writer = response.getWriter();
        if(model.size()==1){
            writer.write((String) model.values().toArray()[0]);
            return;
        }
        objectMapper.writeValue(writer, model);
    }

    @Override
    public String getViewName() {
        return "";
    }
}
