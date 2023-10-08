package dev.baichuanai;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

import dev.baichuanai.chat.ChatCompletionRequest;
import dev.baichuanai.chat.ChatCompletionResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
public class BaichuanAiClient {

    public static final String BASE_URL = "https://api.baichuan-ai.com/v1/";

    public static final String BAICHUANAI_API_KEY = "BAICHUANAI_API_KEY";
    public static final String BAICHUANAI_SECRET_KEY = "BAICHUANAI_SECRET_KEY";

    private final String baseUrl;

    private final OkHttpClient okHttpClient;
    private final BaichuanAiApi baichuanAiApi;
    private String apiKey;
    private String secretKey;

    private final boolean logStreamingResponses;

    private BaichuanAiClient(Builder builder) {

        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.secretKey = builder.secretKey;

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .callTimeout(builder.callTimeout)
                .connectTimeout(builder.connectTimeout)
                .readTimeout(builder.readTimeout)
                .writeTimeout(builder.writeTimeout);

        if (builder.apiKey == null || builder.secretKey == null) {
            throw new IllegalArgumentException("Token must be defined or API Key and Secret Key must be defined");
        } else {
            okHttpClientBuilder.addInterceptor(new AuthorizationHeaderInjector(builder.apiKey, builder.secretKey));

            if (builder.proxy != null) {
                okHttpClientBuilder.proxy(builder.proxy);
            }

            if (builder.logRequests) {
                okHttpClientBuilder.addInterceptor(new RequestLoggingInterceptor());
            }

            if (builder.logResponses) {
                okHttpClientBuilder.addInterceptor(new ResponseLoggingInterceptor());
            }

            this.logStreamingResponses = builder.logStreamingResponses;
            this.okHttpClient = okHttpClientBuilder.build();

            // core logic
            Retrofit retrofit = (new Retrofit.Builder())
                    .baseUrl(builder.baseUrl)
                    .client(this.okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(Utils.GSON))
                    .build();

            this.baichuanAiApi = retrofit.create(BaichuanAiApi.class);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String baseUrl = BASE_URL;
        private String apiKey = System.getenv(BAICHUANAI_API_KEY);
        private String secretKey = System.getenv(BAICHUANAI_SECRET_KEY);

        private Duration callTimeout = Duration.ofSeconds(60L);
        private Duration connectTimeout = Duration.ofSeconds(60L);
        private Duration readTimeout = Duration.ofSeconds(60L);
        private Duration writeTimeout = Duration.ofSeconds(60L);
        private Proxy proxy;
        private boolean logRequests = true;
        private boolean logResponses = true;
        private boolean logStreamingResponses = false;

        private Builder() {

        }

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
            return this;

        }

        public Builder apiKey(@NonNull String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder secretKey(@NonNull String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder callTimeout(@NonNull Duration callTimeout) {
            this.callTimeout = callTimeout;
            return this;

        }

        public Builder connectTimeout(@NonNull Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(@NonNull Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;

        }

        public Builder writeTimeout(@NonNull Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder proxy(Proxy.Type type, String ip, int port) {
            this.proxy = new Proxy(type, new InetSocketAddress(ip, port));
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder logRequests() {
            return this.logRequests(true);
        }

        public Builder logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }

            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses() {
            return this.logResponses(true);
        }

        public Builder logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }

            this.logResponses = logResponses;
            return this;
        }

        public Builder logStreamingResponses() {
            return this.logStreamingResponses(true);
        }

        public Builder logStreamingResponses(Boolean logStreamingResponses) {
            if (logStreamingResponses == null) {
                logStreamingResponses = false;
            }

            this.logStreamingResponses = logStreamingResponses;
            return this;
        }

        public BaichuanAiClient build() {
            return new BaichuanAiClient(this);
        }
    }

    public SyncOrAsync<ChatCompletionResponse> chat(ChatCompletionRequest request) {

        ChatCompletionRequest syncRequest = ChatCompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                this.baichuanAiApi.chat(syncRequest),
                (r) -> r);
    }

    public SyncOrAsync<String> chat(String userMessage, String model) {

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .addUserMessage(userMessage)
                .model(model)
                .build();

        ChatCompletionRequest syncRequest = ChatCompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                this.baichuanAiApi.chat(syncRequest),
                ChatCompletionResponse::content);
    }

    public SyncOrAsync<String> chat(String userMessage) {

        return chat(userMessage, Model.BAICHUAN2_53B.toString());
    }

    public void shutdown() {
        this.okHttpClient.dispatcher().executorService().shutdown();
        this.okHttpClient.connectionPool().evictAll();
        Cache cache = this.okHttpClient.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ioe) {
                log.error("Failed to close cache", ioe);
            }
        }

    }

}
