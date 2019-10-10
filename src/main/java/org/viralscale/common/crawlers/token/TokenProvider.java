package org.viralscale.common.crawlers.token;

import okhttp3.Request;
import okhttp3.Response;

public interface TokenProvider {
    Request applyToken(Request request);
    void parseLimit(Response response);
}
