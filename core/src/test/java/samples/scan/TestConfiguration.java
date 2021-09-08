package samples.scan;

import air.annotation.Bean;
import air.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

    @Bean
    public TestBean2 testBean2() {
        return new TestBean2();
    }
}
