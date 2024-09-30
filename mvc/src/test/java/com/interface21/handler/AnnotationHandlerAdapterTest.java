package com.interface21.handler;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("애노테이션 기반 핸들러 적용")
    void adapt_annotation_based_handler_mapping() throws Exception {
        // given
        final AnnotationHandlerAdapter mappingAdapter = new AnnotationHandlerAdapter("samples");
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/get");
        final HttpServletResponse response = new MockHttpServletResponse();

        // when
        final ModelAndView actual = mappingAdapter.adapt(request, response);

        // then
        assertThat(actual).isEqualTo(new ModelAndView(new JspView("")));
    }

    @Test
    @DisplayName("핸들러에 특정 http request에 대한 핸들러를 지원하는지 확인한다. - 참")
    void check_support_handler_with_specific_http_request_true_case() {
        // given
        final AnnotationHandlerAdapter mappingAdapter = new AnnotationHandlerAdapter("samples");
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/get");

        // when
        final boolean support = mappingAdapter.support(request);

        // then
        assertThat(support).isTrue();
    }

    @Test
    @DisplayName("핸들러에 특정 http request에 대한 핸들러를 지원하는지 확인한다. - 거짓")
    void check_support_handler_with_specific_http_request_false_case() {
        // given
        final AnnotationHandlerAdapter mappingAdapter = new AnnotationHandlerAdapter("support");
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/none");

        // when
        final boolean support = mappingAdapter.support(request);

        // then
        assertThat(support).isFalse();
    }
}
