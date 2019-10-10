package org.viralscale.common.crawlers;

public abstract class BaseCrawler<T extends CrawlerConfig, U> {
    protected CrawlerStorage<U> storage;
    protected T config;

    private BaseCrawler() {

    }

    public BaseCrawler(T config) {
        this.storage = new CrawlerStorage<>();
        this.config = config;

        System.out.println("Crawler instantiated with options: " + config.toJson());
    }

    //protected abstract void init(T config);
    public abstract void crawl();
    public abstract String getIdentifier();
    public void markPostAsInactive(U post) {
        this.storage.markAsInactive(post);
    };
}
