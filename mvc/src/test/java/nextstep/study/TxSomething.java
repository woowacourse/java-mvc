package nextstep.study;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxSomething implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TxSomething.class);

    private final Object target;

    public TxSomething(final Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
            throws Throwable {
        Object result = null;
        try {
            log.info("######## tx start ########");
            result = proxy.invoke(target, args);
            log.info("######## tx commit ! ########");
        } catch (Exception e) {
            log.info("######## tx rollback ! ########");
        } finally {
            log.info("con release");
        }
        return result;
    }
}
