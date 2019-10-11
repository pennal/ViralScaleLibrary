package org.viralscale.common.crawlers;

public abstract class BaseCrawler<T extends CrawlerConfig, U, V extends ConfigLoader<T>> {
    protected CrawlerStorage<U> storage;
    protected V configLoader;
    protected T config;

    private BaseCrawler() {

    }

    public BaseCrawler(V configLoader) {
        this.storage = new CrawlerStorage<>();
        this.configLoader = configLoader;
        this.config = configLoader.loadConfiguration();

        System.out.println("Crawler instantiated with options: " + config.toJson());
    }

    //protected abstract void init(T config);
    public abstract void crawl();
    public abstract String getIdentifier();
    public void markPostAsInactive(U post) {
        this.storage.markAsInactive(post);
    };
    public T getConfig() {
        return this.config;
    }
}
