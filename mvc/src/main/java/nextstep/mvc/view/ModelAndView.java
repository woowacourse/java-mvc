package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView() {
        this.view = new JsonView();
        this.model = new HashMap<>();
    }

    public ModelAndView(String viewName) {
        this.view = new JspView(viewName);
        this.model = new HashMap<>();
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

    public void render(HttpServletRequest request, HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
