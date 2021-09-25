package nextstep.mvc.view;

import nextstep.mvc.exception.MvcException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Object view;
    private final Map<String, Object> model;

    public ModelAndView(String viewName) {
        this.view = viewName;
        this.model = new HashMap<>();
    }

    public ModelAndView(View view) {
        this.view = view;
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

    public String getViewName() {
        if (containsViewName()) {
            return (String) view;
        }
        throw new MvcException(String.format("Cannot get view name from ModelAndView instance. instance: %s", this));
    }

    public View getView() {
        if (containsView()) {
            return (View) view;
        }
        throw new MvcException(String.format("Cannot get view from ModelAndView instance. instance: %s", this));
    }

    public boolean containsViewName() {
        return view instanceof String;
    }

    public boolean containsView() {
        return view instanceof View;
    }
}
