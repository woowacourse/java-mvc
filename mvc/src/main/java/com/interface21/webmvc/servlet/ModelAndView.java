package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JspView;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelAndView {

    @Getter
    private final View view;

    private final Map<String, Object> model;

    public static ModelAndView of(final View view, final Map<String, Object> model) {
        return new ModelAndView(view, model);
    }

    public static ModelAndView withoutModel(final View view) {
        return new ModelAndView(view, Collections.emptyMap());
    }

    public static ModelAndView redirect(final String url) {
        return new ModelAndView(JspView.redirect(url), Collections.emptyMap());
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }
}
