package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final String viewName) {
        this.view = getView(viewName);
        this.model = new HashMap<>();
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public void render(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        view.render(model, request, response);
    }

    private static View getView(final String viewName) {
        if (viewName.endsWith(".jsp")) {
            return new JspView(viewName);
        }
        return new JsonView(viewName);
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }
}
