package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerRegistrationTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("registration");
        handlerMapping.initialize();
    }

    @Test
    void registersDifferentHandlersForSameUrl() throws Exception {
        final var getHandler = getHandler("/same", RequestMethod.GET);
        final var postHandler = getHandler("/same", RequestMethod.POST);

        assertThat(getHandler).isNotSameAs(postHandler);
        assertThat(getHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("get");
        assertThat(postHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("post");
        assertThat(getHandler("/same", RequestMethod.PUT)).isNull();
    }

    @Test
    void registersAllSpecifiedMethods() throws Exception {
        final var getHandler = getHandler("/multiple", RequestMethod.GET);
        final var postHandler = getHandler("/multiple", RequestMethod.POST);

        assertThat(getHandler).isNotNull().isSameAs(postHandler);
        assertThat(getHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("multiple");
        assertThat(getHandler("/multiple", RequestMethod.DELETE)).isNull();
    }

    @Test
    void registersAllMethodsWhenMethodIsOmitted() throws Exception {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            final var handler = getHandler("/all", requestMethod);

            assertThat(handler).isNotNull();
            assertThat(handler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                    .getObject("method")).isEqualTo("all");
        }
    }

    @Test
    void excludesMethodsWithoutRequestMapping() {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            assertThat(getHandler("", requestMethod)).isNull();
            assertThat(getHandler("/unmapped", requestMethod)).isNull();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"/private", "/protected", "/package-private"})
    void public이_아닌_메서드는_등록하지_않는다(final String url) {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            assertThat(getHandler(url, requestMethod)).isNull();
        }
    }

    private HandlerExecution getHandler(final String url, final RequestMethod requestMethod) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(requestMethod.name());
        return (HandlerExecution) handlerMapping.getHandler(request);
    }
}
