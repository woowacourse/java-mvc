package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.tobe.registry.ControllerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerRegistryTest {

    @Test
    @DisplayName("인스턴스를 저장한 후 호출하면 저장된 인스턴스를 가져온다.")
    void getOrCreateController() {
        Object controller = ControllerRegistry.getOrCreateController(TestController.class);
        Object afterSaveController = ControllerRegistry.getOrCreateController(TestController.class);

        assertThat(controller).hasSameHashCodeAs(afterSaveController);
    }
}
