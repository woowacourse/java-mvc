package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView addAttribute(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getAttribute(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }

    public void render(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("view render failed");
        }
    }
}
