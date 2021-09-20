package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Map<String, Object> model;
    private Object view;

    public ModelAndView() {
        this(null);
    }

    public ModelAndView(Object view) {
        this(view, new HashMap<>());
    }

    public ModelAndView(Object view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
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

    public String getViewName() {
        if (this.view instanceof String) {
            return (String) this.view;
        }
        return null;
    }

    public View getView() {
        if (this.view instanceof View) {
            return (View) this.view;
        }
        return null;
    }

    public void changeViewName(String viewNAme) {
        this.view = viewNAme;
    }
}
