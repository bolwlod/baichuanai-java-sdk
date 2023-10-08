package dev.llm.baichuanai;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

class AuthorizationHeaderInjector implements Interceptor {

    private final String apiKey;
    private final String secretKey;

    AuthorizationHeaderInjector(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // 必须精确到秒而不是毫秒
        String currentTimestamp = String.valueOf((long)(Instant.now().toEpochMilli()/1000));
        // 请求body中的json数据
        String httpBody = Utils.getBody(chain.request());

        // 签名
        String signature = Utils.hashWithMD5(this.secretKey + httpBody + currentTimestamp);

        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .addHeader("X-BC-Request-Id", UUID.randomUUID().toString())
                .addHeader("X-BC-Timestamp", currentTimestamp)
                .addHeader("X-BC-Signature", signature)
                .addHeader("X-BC-Sign-Algo", "MD5")
                .build();

        return chain.proceed(request);
    }
}
