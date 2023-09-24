package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private static final Logger log = LoggerFactory.getLogger(ModelAndView.class);

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public void render(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (final Exception e) {
            throw new IllegalStateException("화면을 처리할 수 없습니다.");
        }
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public View getView() {
        return view;
    }
}
