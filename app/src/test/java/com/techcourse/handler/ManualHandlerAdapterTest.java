package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import support.FakeHttpServletRequest;
import support.FakeHttpServletResponse;

class ManualHandlerAdapterTest {

    @Test
    @DisplayName("ManualHandler를 적용한다.")
    void adapt_manual_handler_mapping() throws Exception {
        // given
        final ManualHandlerAdapter mappingAdapter = new ManualHandlerAdapter();
        final HttpServletRequest request = new FakeHttpServletRequest("GET", "/");
        final FakeHttpServletResponse response = new FakeHttpServletResponse();

        // when
        final ModelAndView modelAndView = mappingAdapter.adapt(request, response);

        // then
        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("/index.jsp")));
    }

    @Test
    @DisplayName("핸들러에 특정 http request에 대한 핸들러를 지원하는지 확인한다. - 참")
    void check_support_handler_with_specific_http_request_true_case() {
        // given
        final ManualHandlerAdapter mappingAdapter = new ManualHandlerAdapter();
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/");

        // when
        final boolean support = mappingAdapter.support(request);

        // then
        assertThat(support).isTrue();
    }

    @Test
    @DisplayName("핸들러에 특정 http request에 대한 핸들러를 지원하는지 확인한다. - 거짓")
    void check_support_handler_with_specific_http_request_false_case() {
        // given
        final ManualHandlerAdapter mappingAdapter = new ManualHandlerAdapter();
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/get");

        // when
        final boolean support = mappingAdapter.support(request);

        // then
        assertThat(support).isFalse();
    }
}
