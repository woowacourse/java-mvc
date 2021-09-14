package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private View view;
    private final Map<String, Object> model;

    public ModelAndView() {
        this(null);
    }

    public ModelAndView(View view) {
        this(view, new HashMap<>());
    }

    public ModelAndView(View view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }

    public void changeView(View view) {
        this.view = view;
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
}
