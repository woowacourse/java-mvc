package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    @Override
    public String getViewName() {
        return "";
    }
}
