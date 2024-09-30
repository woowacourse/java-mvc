package com.interface21.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SingletonManagerTest {

    static class Dummy {
        public Dummy() {
        }
    }

    static class PrivateDummy {
        private PrivateDummy() {
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

    @Test
    @DisplayName("객체 생성에 실패하는 경우 예외가 발생한다.")
    void privateConstructor() {
        SingletonManager manager = SingletonManager.getInstance();
        assertThatThrownBy(() -> manager.get(PrivateDummy.class))
                .isInstanceOf(SingletonInstantiationException.class);
    }
}
