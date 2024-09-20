package com.interface21.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.bean.scanner.BeanScanner;
import com.interface21.context.stereotype.Component;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanScannerTest {

    @DisplayName("@Component가 붙은 클래스를 스캔하여 인스턴스르 생성한다.")
    @Test
    void componentScan() {
        List<Object> beans = BeanScanner.componentScan(getClass().getPackageName());

        assertThat(beans).hasSize(2)
                .extracting(bean -> bean.getClass().getSimpleName())
                .containsExactlyInAnyOrder("TestController1", "TestController2");
    }

    @DisplayName("특정 클래스를 구현한 클래스를 스캔하여 인스턴스를 생성한다.")
    @Test
    void subTypeScan() {
        List<Object> beans = BeanScanner.subTypeScan(TestController1.class, getClass().getPackageName());

        assertThat(beans).hasSize(2)
                .extracting(bean -> bean.getClass().getSimpleName())
                .containsExactlyInAnyOrder("TestController2", "TestController3");
    }

    @Component
    private static class TestController1 {
    }

    @Component
    private static class TestController2 extends TestController1 {
    }

    private static class TestController3 extends TestController1 {
    }
}
