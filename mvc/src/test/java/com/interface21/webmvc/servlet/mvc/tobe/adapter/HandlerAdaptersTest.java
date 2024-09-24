package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.valid.TestAdapter;
import samples.valid.TestController;
import samples.valid.TestControllerChild;

class HandlerAdaptersTest {

    @DisplayName("해당 핸들러를 처리할 어댑터를 반환한다.")
    @Test
    void getAdapter() {
        // given
        TestController controller = new TestController();
        HandlerAdapters adapters = new HandlerAdapters(new TestAdapter());

        // when
        HandlerAdapter<?> adapter = adapters.getAdapter(controller);

        // then
        assertThat(adapter).isInstanceOf(TestAdapter.class);
    }

    @DisplayName("부모 클래스로 핸들러를 등록해도 자식 클래스로 어댑터를 찾을 수 있다.")
    @Test
    void getAdapter_child() {
        // given
        TestControllerChild controller = new TestControllerChild();
        HandlerAdapters adapters = new HandlerAdapters(new TestAdapter());

        // when
        HandlerAdapter<?> adapter = adapters.getAdapter(controller);

        // then
        assertThat(adapter).isInstanceOf(TestAdapter.class);
    }
}
