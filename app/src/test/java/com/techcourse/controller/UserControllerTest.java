package com.techcourse.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void returnsUserAsJson() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("gugu");

        String body = render(request, response);

        assertThat(body).isEqualTo("{\"account\":\"gugu\"}");
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void returns400ForMissingOrBlankAccount() throws Exception {
        for (String account : new String[]{null, "", " \t"}) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            when(request.getParameter("account")).thenReturn(account);

            String body = render(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            assertThat(body).isEqualTo("\"account is required\"");
        }
    }

    @Test
    void returns404ForUnknownAccount() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("nonexistent-user");

        String body = render(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(body).isEqualTo("\"User not found\"");
    }

    private String render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        ModelAndView result = new UserController().show(request, response);

        result.getView().render(result.getModel(), request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        return body.toString();
    }
}
