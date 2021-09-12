package nextstep.mvc.view;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

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

    public View getView() {
        return view;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        view.render(model, request, response);
    }
}
