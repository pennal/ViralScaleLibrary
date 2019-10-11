package org.viralscale.common.crawlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.viralscale.common.utils.JSONUtils;

@Getter
@Setter
public abstract class CrawlerConfig {
    private Double timeout;
    protected abstract String getCrawlerName();

    protected String toJson() {
        try {
            return JSONUtils.toJsonString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "UNPARSABLE_CONFIG";
        }
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}
