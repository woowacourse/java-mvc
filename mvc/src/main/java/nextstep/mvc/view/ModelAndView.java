package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final String viewPath;
    private final Map<String, Object> model;

    public ModelAndView(final String viewPath) {
        this.viewPath = viewPath;
        this.model = new HashMap<>();
    }

    public void addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public String getViewPath() {
        return viewPath;
    }
}
