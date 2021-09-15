package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.exception.view.RenderingException;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(View view) {
        this(view, new HashMap<>());
    }

    private ModelAndView(View view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }

    public void addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public void render(HttpServletRequest request, HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new RenderingException();
        }
    }

    public View getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
