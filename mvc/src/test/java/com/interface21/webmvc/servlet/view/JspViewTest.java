package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class JspViewTest {

    private JspView jspView;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testRenderRedirect() throws Exception {
        // Given
        String redirectViewName = "redirect:/home";
        jspView = new JspView(redirectViewName);

        // When
        jspView.render(new HashMap<>(), request, response);

        // Then
        assertEquals("/home", response.getRedirectedUrl());
    }

    @Test
    void testSetRequestAttributes() throws Exception {
        // Given
        String forwardViewName = "/WEB-INF/views/home.jsp";
        jspView = new JspView(forwardViewName);
        Map<String, Object> model = new HashMap<>();
        model.put("user", "John");
        model.put("role", "admin");

        // When
        jspView.render(model, request, response);

        // Then
        assertEquals("John", request.getAttribute("user"));
        assertEquals("admin", request.getAttribute("role"));
    }
}
