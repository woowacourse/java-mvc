package com.interface21;

import com.interface21.context.stereotype.Handler;

class HandlerScannerTest {

    @Handler
    static class Dummy {

    }

    /*@DisplayName("HandlerManagement 어노테이션이 달린 클래스들을 찾아온다")
    @Test
    void scanHandlerHelper() {
        Set<Class<?>> classes = HandlerManagementScanner.scanHandlerHelper();

        assertThat(classes.size()).isEqualTo(3);
    }*/

    /*@DisplayName("HandlerManagement 어노테이션이 달린 클래스들을 찾아온다")
    @Test
    void scanSubTypeOf() {
        List<HandlerMapping> classes = HandlerManagementScanner.scanSubTypeOf(this.getClass(), HandlerMapping.class);

        assertThat(classes.size()).isEqualTo(2);
    }*/
}
