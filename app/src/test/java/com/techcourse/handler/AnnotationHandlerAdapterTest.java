package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import support.FakeHttpServletRequest;
import support.FakeHttpServletResponse;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("애노테이션 기반 핸들러 적용")
    void adapt_annotation_based_handler_mapping() throws Exception {
        // given
        final AnnotationHandlerAdapter mappingAdapter = new AnnotationHandlerAdapter("support");
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/get");
        final FakeHttpServletResponse response = new FakeHttpServletResponse();

        // when
        final ModelAndView actual = mappingAdapter.adapt(request, response);

        // then
        assertThat(actual).isEqualTo(new ModelAndView(new JspView("")));
    }

    @Test
    @DisplayName("핸들러에 특정 http request에 대한 핸들러를 지원하는지 확인한다. - 참")
    void check_support_handler_with_specific_http_request_true_case() {
        // given
        final AnnotationHandlerAdapter mappingAdapter = new AnnotationHandlerAdapter("support");
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/get");

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
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/none");

        // when
        final boolean support = mappingAdapter.support(request);

        // then
        assertThat(support).isFalse();
    }
}
