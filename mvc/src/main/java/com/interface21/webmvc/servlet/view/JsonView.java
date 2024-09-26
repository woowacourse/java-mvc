package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.AbstractView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView extends AbstractView {

    public JsonView(String viewName) {
        super(viewName);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    }
}
