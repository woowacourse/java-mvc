package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import support.FakeHttpServletRequest;
import support.FakeHttpServletResponse;

class ManualHandlerMappingAdapterTest {

    @Test
    @DisplayName("ManualHandler를 적용한다.")
    void adapt_manual_handler_mapping() throws Exception {
        // given
        final ManualHandlerMappingAdapter mappingAdapter = new ManualHandlerMappingAdapter();
        final HttpServletRequest request = new FakeHttpServletRequest("GET", "/");
        final FakeHttpServletResponse response = new FakeHttpServletResponse();

        // when
        final ModelAndView modelAndView = mappingAdapter.adapt(request, response);

        // then
        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("/index.jsp")));
    }

}
