package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import support.FakeHttpServletRequest;
import support.FakeHttpServletResponse;

class AnnotationHandlerMappingAdapterTest {

    @Test
    @DisplayName("애노테이션 기반 핸들러 적용")
    void adapt_annotation_based_handler_mapping() throws Exception {
        // given
        final AnnotationHandlerMappingAdapter mappingAdapter = new AnnotationHandlerMappingAdapter("support");
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/get");
        final FakeHttpServletResponse response = new FakeHttpServletResponse();

        // when
        final ModelAndView actual = mappingAdapter.adapt(request, response);

        // then
        assertThat(actual).isEqualTo(new ModelAndView(new JspView("")));
    }
}
