package com.interface21.webmvc.servlet.mvc.tobe.pathfinder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultRootPathStrategyTest {

    @Test
    @DisplayName("Application을 실행할 수 있는 어노테이션이 없을 경우 에러를 발생한다.")
    void search_WhenNoApplication() {
        assertThatThrownBy(() -> new DefaultRootPathStrategy().search())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("시작할 수 없는 Application 입니다.");
    }
}
