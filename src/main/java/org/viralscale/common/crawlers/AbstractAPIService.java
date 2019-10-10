package org.viralscale.common.crawlers;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.viralscale.common.crawlers.token.PassThroughTokenProvider;
import org.viralscale.common.crawlers.token.TokenProvider;

import java.io.IOException;

public abstract class AbstractAPIService {
    Logger logger = LoggerFactory.getLogger(AbstractAPIService.class);

    private OkHttpClient client;
    private TokenProvider tokenProvider;

    protected abstract HttpUrl.Builder getBaseUrl();

    public AbstractAPIService() {
        this(new PassThroughTokenProvider());
    }
    public AbstractAPIService(TokenProvider tokenProvider) {
        this.client = new OkHttpClient();
        this.tokenProvider = tokenProvider;
    }

    protected Response call(HttpUrl url) {
        System.out.println("[ViralScaleLibrary] - call() executed with url " + url.toString());
        // Launch the actual request
        Request req = new Request.Builder()
                .url(url)
                .build();

        if (this.tokenProvider != null) {
            // In this case, we need to add the token, depending on the implementation
            req = tokenProvider.applyToken(req);
        }

        Response res = null;
        try {
            res = client.newCall(req).execute();
        } catch (IOException e) {
            logger.error("COULD NOT EXECUTE CALL!");
        }

        // TODO: Parse the request limit
        if (this.tokenProvider != null) {
            this.tokenProvider.parseLimit(res);
        }


        return res;
    }
}
