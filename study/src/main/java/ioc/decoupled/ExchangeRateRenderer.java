package ioc.decoupled;

public interface ExchangeRateRenderer {
    void render();

    ExchangeRateProvider getExchangeRateProvider();

    void setExchangeRateProvider(ExchangeRateProvider provider);
}
