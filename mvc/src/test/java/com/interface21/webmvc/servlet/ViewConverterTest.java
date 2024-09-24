package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.NotSupportViewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.RecordView;
import samples.TestView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ViewConverterTest {

    @Test
    @DisplayName("String 이면 기본으로 JspView 를 반환한다.")
    void string_convert_string_view() {
        final View view = ViewConverter.convert("index.jsp");
        assertThat(view).isEqualTo(new JspView("index.jsp"));
    }

    @Test
    @DisplayName("getView 메소드를 기반으로 내부 View 를 가져온다.")
    void object_convert_with_get_view_method() {
        final JspView jspView = new JspView("index.jsp");
        final TestView testView = new TestView(jspView);
        final View view = ViewConverter.convert(testView);
        assertThat(view).isEqualTo(jspView);
    }

    @Test
    @DisplayName("getView 메소드가 없으면 예외를 발생한다.")
    void throw_exception_when_not_exist_get_view_method() {
        final JspView jspView = new JspView("index.jsp");
        final RecordView recordView = new RecordView(jspView);
        assertThatThrownBy(() -> ViewConverter.convert(recordView))
                .isInstanceOf(NotSupportViewException.class);
    }
}
