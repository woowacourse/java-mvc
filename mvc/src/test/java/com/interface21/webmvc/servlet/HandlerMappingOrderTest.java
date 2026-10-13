package com.interface21.webmvc.servlet;

import com.interface21.core.Ordered;
import com.interface21.webmvc.servlet.handler.AbstractHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.mvc.annotation.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.annotation.HandlerExecutionAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerMappingOrderTest {

    @Test
    @DisplayName("등록 순서와 관계없이 숫자가 작은 매핑을 먼저 선택한다")
    void selectsLowerOrderFirst() throws Exception {
        assertRedirect(List.of(mapping(100, "/low"), mapping(-1, "/high")), "/high");
    }

    @Test
    @DisplayName("우선순위가 같으면 등록 순서를 유지한다")
    void preservesRegistrationOrderForTies() throws Exception {
        assertRedirect(List.of(mapping(10, "/first"), mapping(10, "/second")), "/first");
    }

    @Test
    @DisplayName("Ordered 미구현 매핑은 가장 낮은 우선순위로 처리한다")
    void treatsUnorderedMappingAsLowestPriority() throws Exception {
        final var unordered = mock(HandlerMapping.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var fallback = handler("/fallback");
        when(unordered.getHandler(request)).thenReturn(fallback);
        final var servlet = new DispatcherServlet(List.of(unordered, mapping(0, "/ordered")),
                List.of(new HandlerExecutionAdapter()));

        servlet.init();
        servlet.service(request, response);

        verify(response).sendRedirect("/ordered");
    }

    @Test
    @DisplayName("우선 매핑에 핸들러가 없으면 다음 매핑을 조회한다")
    void fallsBackWhenHigherPriorityHasNoHandler() throws Exception {
        final var empty = new TestMapping(null);
        empty.setOrder(-1);
        assertRedirect(List.of(mapping(100, "/fallback"), empty), "/fallback");
    }

    @Test
    @DisplayName("추상 매핑의 기본 우선순위는 가장 낮으며 변경할 수 있다")
    void defaultsToLowestPriorityAndAllowsConfiguration() {
        final var mapping = new TestMapping(null);
        assertThat(mapping.getOrder()).isEqualTo(Ordered.LOWEST_PRECEDENCE);
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        assertThat(mapping.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
    }

    private TestMapping mapping(final int order, final String path) throws Exception {
        final var mapping = new TestMapping(handler(path));
        mapping.setOrder(order);
        return mapping;
    }

    private HandlerExecution handler(final String path) throws Exception {
        final var handler = mock(HandlerExecution.class);
        when(handler.handle(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .thenReturn(new ModelAndView(new JspView("redirect:" + path)));
        return handler;
    }

    private void assertRedirect(final List<HandlerMapping> mappings, final String path) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var servlet = new DispatcherServlet(mappings, List.of(new HandlerExecutionAdapter()));
        servlet.init();
        servlet.service(request, response);
        verify(response).sendRedirect(path);
    }

    private static class TestMapping extends AbstractHandlerMapping {
        private final HandlerExecution handler;

        TestMapping(final HandlerExecution handler) {
            this.handler = handler;
        }

        @Override
        public void initialize() {
        }

        @Override
        public Object getHandler(final HttpServletRequest request) {
            return handler;
        }
    }
}
