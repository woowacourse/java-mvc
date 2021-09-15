package nextstep.mvc.view;

import java.util.*;

public class ModelAndView {

    private final View view;
    private final Map<String, List<Object>> model;

    public ModelAndView(View view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public void addObject(String attributeName, Object attributeValue) {
        if (model.containsKey(attributeName)) {
            model.get(attributeName).add(attributeValue);
        } else {
            List<Object> attributeValues = new ArrayList<>();
            attributeValues.add(attributeValue);
            model.put(attributeName, attributeValues);
        }
    }

    public List<Object> getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> model() {
        return Collections.unmodifiableMap(model);
    }

    public View view() {
        return view;
    }
}
