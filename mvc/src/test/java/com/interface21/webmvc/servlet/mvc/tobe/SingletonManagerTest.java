package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SingletonManagerTest {

    static class Dummy {
        public Dummy() {
        }
    }

    @Test
    @DisplayName("여러 번 요청하더라도 같은 인스턴스를 반환한다.")
    void identicalInstanceOnMultipleRequest() {
        SingletonManager manager = SingletonManager.getInstance();
        Object dummy1 = manager.get(Dummy.class);
        Object dummy2 = manager.get(Dummy.class);
        assertThat(dummy1).isSameAs(dummy2);
    }
}
