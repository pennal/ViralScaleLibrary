package org.viralscale.common.crawlers;

public abstract class ConfigLoader<T extends CrawlerConfig> {
    public abstract T loadConfiguration();
}
