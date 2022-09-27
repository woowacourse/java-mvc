package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Object view;
    private final Map<String, Object> model;

    public ModelAndView(final Object view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public boolean hasViewImpl() {
        return view instanceof View;
    }

    public View getView() {
        if (view instanceof View) {
            return (View) view;
        }
        return null;
    }

    public String getViewName() {
        if (view instanceof String) {
            return (String) view;
        }
        return null;
    }
}
