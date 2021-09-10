package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.exception.PageNotFoundException;
import nextstep.web.support.StatusCode;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;
    private final StatusCode statusCode;

    public ModelAndView(View view) {
        this.view = view;
        this.model = new HashMap<>();
        this.statusCode = StatusCode.OK;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (Exception exception) {
            throw new PageNotFoundException();
        }
    }
}
