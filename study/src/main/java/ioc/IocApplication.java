package ioc;

import ioc.decoupled.ExchangeRateConfiguration;
import ioc.decoupled.ExchangeRateRenderer;
import ioc.decoupled.ExchangeRateSupportFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IocApplication {
    public static void main(String[] args) {
        // 모두 환율 출력의 역할을 하는데, 방식이 다르다.
        myIocContainer();
        xmlApplicationContext();
        annotationApplicationContext();
    }

    private static void myIocContainer() { // 팩토리 클래스 활용 방법
        final ExchangeRateSupportFactory factory = ExchangeRateSupportFactory.getInstance();
        final ExchangeRateRenderer renderer = factory.getExchangeRateRenderer();
        renderer.render();
    }

    private static void xmlApplicationContext() { // 스프링 IoC 컨테이너 중 xml 활용 방법 (DP)
        final ApplicationContext context = new ClassPathXmlApplicationContext("exchange-rate-context.xml");
        final ExchangeRateRenderer renderer = context.getBean("exchangeRateRenderer", ExchangeRateRenderer.class);
        renderer.render();
    }

    private static void annotationApplicationContext() { // 스프링 IoC 컨테이너 중 어노테이션 활용 방법 (DP)
        final ApplicationContext context = new AnnotationConfigApplicationContext(ExchangeRateConfiguration.class);
        final ExchangeRateRenderer renderer = context.getBean("exchangeRateRenderer", ExchangeRateRenderer.class);
        renderer.render();
    }
}
