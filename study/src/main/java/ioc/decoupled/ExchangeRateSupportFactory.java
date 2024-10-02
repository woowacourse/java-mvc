package ioc.decoupled;

import java.io.InputStream;
import java.util.Properties;

public final class ExchangeRateSupportFactory {

    private static final ExchangeRateSupportFactory INSTANCE;

    private final ExchangeRateProvider exchangeRateProvider;
    private final ExchangeRateRenderer exchangeRateRenderer;

    static {
        INSTANCE = new ExchangeRateSupportFactory();
    }

    private ExchangeRateSupportFactory() {
        Properties properties = new Properties();
        try {
            // resource 디렉터리에서 exchange-rate.properties 파일 가져옴
            final InputStream resourceStream = this.getClass().getResourceAsStream("/exchange-rate.properties");
            // 불러온 파일 properties 객체에 저장
            properties.load(resourceStream);

            // exchange-rate.properties 파일에서 provider.class, renderer.class 값을 가져옴
            // providerClass, rendererClass 값은 객체로 만들 클래스 이름
            final String providerClass = properties.getProperty("provider.class");
            final String rendererClass = properties.getProperty("renderer.class");

            // 클래스 이름으로 ExchangeRateProvider, ExchangeRateRenderer 객체 생성
            this.exchangeRateProvider = (ExchangeRateProvider) Class.forName(providerClass)
                    .getDeclaredConstructor()
                    .newInstance();
            this.exchangeRateRenderer = (ExchangeRateRenderer) Class.forName(rendererClass)
                    .getDeclaredConstructor()
                    .newInstance();

            exchangeRateRenderer.setExchangeRateProvider(exchangeRateProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRateSupportFactory getInstance() {
        return INSTANCE;
    }

    public ExchangeRateProvider getExchangeRateProvider() {
        return exchangeRateProvider;
    }

    public ExchangeRateRenderer getExchangeRateRenderer() {
        return exchangeRateRenderer;
    }
}
