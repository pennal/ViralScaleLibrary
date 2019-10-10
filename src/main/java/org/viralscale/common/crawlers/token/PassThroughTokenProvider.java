package org.viralscale.common.crawlers.token;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class PassThroughTokenProvider implements TokenProvider {
    public Request applyToken(Request request) {
        // Simple pass-through implementation
        return request;
    }

    @Override
    public void parseLimit(Response response) {
        // Do nothing
    }
}
