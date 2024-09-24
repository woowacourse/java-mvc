package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

	private JspView jspView;

	@DisplayName("render 메서드를 호출하면 request 에 속성을 넣는다.")
	@Test
	void renderTest() throws Exception {
		jspView = new JspView("/get-test");
		Map<String, String> model = new HashMap<>();
		var request = mock(HttpServletRequest.class);
		var requestDispatcher = mock(RequestDispatcher.class);
		var response = mock(HttpServletResponse.class);

		model.put("key", "value");
		when(request.getRequestDispatcher("/get-test")).thenReturn(requestDispatcher);
		jspView.render(model, request, response);
		verify(request).setAttribute("key", "value");
	}

	@DisplayName("jsp 파일을 포워딩한다.")
	@Test
	void renderForwardTest() throws Exception {
		jspView = new JspView("/get-test");
		var request = mock(HttpServletRequest.class);
		var requestDispatcher = mock(RequestDispatcher.class);
		var response = mock(HttpServletResponse.class);

		when(request.getRequestDispatcher("/get-test")).thenReturn(requestDispatcher);
		jspView.render(new HashMap<>(), request, response);
		verify(requestDispatcher).forward(request, response);
	}
}
