package context.org.springframework.context;

public class ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
