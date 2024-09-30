package com.interface21.webmvc.servlet.view;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

	public JsonView() {
		this.objectMapper = new ObjectMapper();
	}

	@Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        objectMapper.writeValue(response.getWriter(), model);
    }
}
