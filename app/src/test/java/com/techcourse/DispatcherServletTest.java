package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private HttpServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    @DisplayName("어노테이션 방식으로 전환한 회원가입 화면을 처리한다")
    void handlesRegisterViewController() throws Exception {
        HttpServletRequest request = request("GET", "/register/view");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher view = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(view);

        servlet.service(request, response);

        verify(view).forward(request, response);
    }

    @Test
    @DisplayName("어노테이션 GET 핸들러를 실행하고 모델을 JSP 요청에 전달한다")
    void handlesAnnotationControllerAndModel() throws Exception {
        HttpServletRequest request = request("GET", "/test/annotation");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher view = mock(RequestDispatcher.class);
        when(request.getParameter("name")).thenReturn("luke");
        when(request.getRequestDispatcher("/annotation-test.jsp")).thenReturn(view);

        servlet.service(request, response);

        verify(request).setAttribute("name", "luke");
        verify(view).forward(request, response);
    }

    @Test
    @DisplayName("같은 URL의 POST 핸들러를 구분해 실행하고 리다이렉트한다")
    void handlesAnnotationPostAndRedirect() throws Exception {
        HttpServletRequest request = request("POST", "/test/annotation");
        HttpServletResponse response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendRedirect("/test/annotation/completed");
        verifyNoMoreInteractions(response);
    }

    @Test
    @DisplayName("요청에 맞는 핸들러를 찾지 못하면 404를 응답한다")
    void returnsNotFoundWhenNoHandlerMatches() throws Exception {
        HttpServletRequest request = request("GET", "/test/missing");
        HttpServletResponse response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoMoreInteractions(response);
    }

    @Test
    @DisplayName("사용자 조회 요청을 매핑하고 JsonView로 사용자 정보와 JSON 헤더를 응답한다")
    void handlesUserJsonResponse() throws Exception {
        HttpServletRequest request = request("GET", "/api/user");
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter output = new StringWriter();
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 조회하면 404와 JSON 오류를 응답한다")
    void returnsNotFoundForUnknownUser() throws Exception {
        HttpServletRequest request = request("GET", "/api/user");
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter output = new StringWriter();
        when(request.getParameter("account")).thenReturn("missing-json-test-user");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(output.toString()).isEqualTo("\"사용자를 찾을 수 없습니다.\"");
    }

    @Test
    @DisplayName("사용자 조회에 account가 없으면 400과 JSON 오류를 응답한다")
    void returnsBadRequestWithoutAccount() throws Exception {
        HttpServletRequest request = request("GET", "/api/user");
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(output.toString()).isEqualTo("\"account가 필요합니다.\"");
    }

    private HttpServletRequest request(String method, String uri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
