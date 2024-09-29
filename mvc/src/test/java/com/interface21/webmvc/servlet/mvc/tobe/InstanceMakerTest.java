package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.RootPathFinder;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.TestRootPathStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstanceMakerTest {

    @Test
    @DisplayName("해당하는 생성자가 없는 경우 에러를 발생한다.")
    void makeInstance_WhenConstructorNotExist() {
        assertThatThrownBy(() -> InstanceMaker.makeInstance(AnnotationHandlerMapping.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메서드를 찾지 못했습니다.");
    }

    @Test
    @DisplayName("해당하는 생성자가 있는 경우 인스턴스를 생성한다.")
    void makeInstance_WhenConstructorExist() {
        assertThat(InstanceMaker.makeInstance(AnnotationHandlerMapping.class, new RootPathFinder(new TestRootPathStrategy())))
                .isInstanceOf(AnnotationHandlerMapping.class);
    }
}
