package reflection;

import java.lang.reflect.Method;

public class MethodNameCondition {
    private final String namePrefix;

    public MethodNameCondition(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public boolean included(Method method) {
        return method.getName().startsWith(namePrefix);
    }
}
