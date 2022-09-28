package nextstep.context;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToransactionalMethod implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ToransactionalMethod.class);

    private final Object target;
    // TODO filter This !
    // prv final MethodNameList

    public ToransactionalMethod(final Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
            throws Throwable {
        Object result = null;
        log.info("method = {}", method);
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
