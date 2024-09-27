package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestServiceA;

class InstanceInitializerTest {

    private InstanceInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new InstanceInitializer();
    }

    @DisplayName("의존성이 없는 클래스의 인스턴스가 생성되어야 한다.")
    @Test
    void createInstance_ShouldReturnSimpleInstance_WhenClassHasNoDependencies() throws Exception {
        // given & when
        TestServiceA service = (TestServiceA) initializer.createInstance(TestServiceA.class);

        // then
        assertThat(service).isNotNull();
        assertThat(service.getIdentifier()).isEqualTo("TestService A");
    }

    @DisplayName("의존성이 있는 클래스의 인스턴스가 생성되고 의존성이 주입되어야 한다.")
    @Test
    void createInstance_ShouldReturnInstanceWithDependencies_WhenClassHasDependencies() throws Exception {
        // given & when
        TestController controller = (TestController) initializer.createInstance(TestController.class);

        // then
        assertAll(
                () -> assertThat(controller).isNotNull(),
                () -> assertThat(controller.getTestServiceA()).isNotNull(),
                () -> assertThat(controller.getTestServiceA().getIdentifier()).isEqualTo("TestService A"),
                () -> assertThat(controller.getTestServiceB()).isNotNull(),
                () -> assertThat(controller.getTestServiceB().getIdentifier()).isEqualTo("TestService B")
        );
    }

    @DisplayName("이미 등록된 인스턴스 객체가 캐싱되어 있을 경우 해당 인스턴스가 반환되어야 한다.")
    @Test
    void createInstance_ShouldReturnCachedInstance_WhenCalledMultipleTimes() throws Exception {
        // given & when
        TestServiceA firstInstance = (TestServiceA) initializer.createInstance(TestServiceA.class);
        TestServiceA secondInstance = (TestServiceA) initializer.createInstance(TestServiceA.class);

        // then
        assertThat(firstInstance).isSameAs(secondInstance);
    }

    @DisplayName("다중 생성자가 있을 때 첫 번째 생성자가 사용되어야 한다.")
    @Test
    void createInstance_ShouldUseFirstConstructor_WhenMultipleConstructorsAvailable() throws Exception {
        // given & when
        TestController instance = (TestController) initializer.createInstance(TestController.class);

        // then
        assertThat(instance.getTestServiceA()).isNotNull();
        assertThat(instance.getTestServiceB()).isNotNull();
    }
}
