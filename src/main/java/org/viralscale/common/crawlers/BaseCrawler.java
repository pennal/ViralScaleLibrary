package org.viralscale.common.crawlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCrawler<T extends CrawlerConfig, U, V extends ConfigLoader<T>> {
    private static final Logger logger = LoggerFactory.getLogger(BaseCrawler.class);

    protected CrawlerStorage<U> storage;
    protected V configLoader;
    protected T config;



    private BaseCrawler() {

    }

    public BaseCrawler(V configLoader) {
        this.storage = new CrawlerStorage<>();
        this.configLoader = configLoader;
        this.config = configLoader.loadConfiguration();

        logger.info("Crawler instantiated with options: " + config.toJson());
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
