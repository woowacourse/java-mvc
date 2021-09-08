package air.context;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import air.annotation.Bean;
import air.annotation.Component;
import air.annotation.Configuration;
import air.annotation.Controller;
import samples.di.InjectController;
import samples.scan.TestBean;
import samples.scan.TestBean2;
import samples.scan.TestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationContextTest {

    ApplicationContext applicationContext;

    @AfterEach
    void tearDown() {
        applicationContext.refresh();
    }

    @Test
    @DisplayName("컴포넌트 스캔 테스트")
    void componentScan() {
        // given
        applicationContext = new ApplicationContext("samples.scan");
        applicationContext.initializeContext();

        // when
        List<Object> controllers = applicationContext.findAllBeanHasAnnotation(Controller.class);
        List<Object> components = applicationContext.findAllBeanHasAnnotation(Component.class);
        List<Object> configurations = applicationContext.findAllBeanHasAnnotation(Configuration.class);
        TestBean testBean = applicationContext.findBeanByType(TestBean.class);
        TestBean2 testBean2 = applicationContext.findBeanByType(TestBean2.class);

        // then
        assertThat(controllers).hasSize(2);
        assertThat(components).hasSize(1);
        assertThat(configurations).hasSize(1);
        assertThat(testBean).isNotNull();
        assertThat(testBean2).isNotNull();
    }

    @Test
    @DisplayName("InjectController가 InjectComponent를 의존하고 있는 경우 의존성 주입")
    void injection() {
        // given
        applicationContext = new ApplicationContext("samples.di");
        applicationContext.initializeContext();

        // when
        InjectController controller = applicationContext.findBeanByType(InjectController.class);

        // then
        assertThat(controller).isNotNull();
        assertThat(controller.injectComponent).isNotNull();
    }

    @Test
    @DisplayName("해당 클래스에 특정 어노테이션이 붙어있는 모든 메서드 가져오기")
    void findAllMethodByAnnotation() {
        // given
        applicationContext = new ApplicationContext("samples.scan");
        applicationContext.initializeContext();
        TestConfiguration configuration = applicationContext.findBeanByType(TestConfiguration.class);

        // when
        List<Method> methods = applicationContext.findAllMethodByAnnotation(configuration.getClass(), Bean.class);

        // then
        assertThat(methods).hasSize(2);
    }
}
