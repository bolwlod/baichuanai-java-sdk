package dev.baichuanai;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class RequestLoggingInterceptor implements Interceptor {

    public RequestLoggingInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log(request);
        return chain.proceed(request);
    }

    private static void log(Request request) {
        try {
            log.debug("Request:\n- method: {}\n- url: {}\n- headers: {}\n- body: {}",
                    new Object[] { request.method(), request.url(), Utils.printHeadersInOneLine(request.headers()),
                            Utils.getBody(request) });
        } catch (Exception e) {
            log.warn("Failed to log request", e);
        }

    }
}
