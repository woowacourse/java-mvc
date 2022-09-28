package nextstep.study;

import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CglibTest {

    private static final Logger log = LoggerFactory.getLogger(CglibTest.class);

    @Test
    void test() {
        final TestService target = new TestService();
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestService.class);
        enhancer.setCallback(new TxSomething(target));
        final TestService proxy = (TestService) enhancer.create();
        log.info("target class = {}", target.getClass());
        log.info("proxy class = {}", proxy.getClass());
        proxy.somethingDo();
    }
}
