package com.interface21;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.context.stereotype.HandlerManagement;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerManagementScannerTest {

    @HandlerManagement
    static class Dummy {

    }

    /*@DisplayName("HandlerManagement 어노테이션이 달린 클래스들을 찾아온다")
    @Test
    void scanHandlerHelper() {
        Set<Class<?>> classes = HandlerManagementScanner.scanHandlerHelper();

        assertThat(classes.size()).isEqualTo(3);
    }*/

    @DisplayName("HandlerManagement 어노테이션이 달린 클래스들을 찾아온다")
    @Test
    void scanSubTypeOf() {
        List<HandlerMapping> classes = HandlerManagementScanner.scanSubTypeOf(this.getClass(), HandlerMapping.class);

        assertThat(classes.size()).isEqualTo(2);
    }
}
