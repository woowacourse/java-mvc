package com.interface21.webmvc.servlet.mvc.tobe.handler.mapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    @DisplayName("없는 url 호출 시 NoSuchElementException 이 발생한다")
    @Test
    void getHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/none");
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.initialize();

        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    private void setUpMockRequest(HttpServletRequest request, RequestMethod requestMethod, String requestUrl) {
        doReturn(requestUrl).when(request).getRequestURI();
        doReturn(requestMethod.name()).when(request).getMethod();
    }
}
