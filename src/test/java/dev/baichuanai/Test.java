package dev.baichuanai;

import static java.time.Duration.ofSeconds;

import java.util.concurrent.CompletableFuture;

import dev.baichuanai.chat.ChatCompletionRequest;
import static java.util.concurrent.TimeUnit.SECONDS;


public class Test {

        public static void main(String[] args) throws Exception {

                BaichuanAiClient client = BaichuanAiClient.builder()
                                .callTimeout(ofSeconds(60))
                                .connectTimeout(ofSeconds(60))
                                .readTimeout(ofSeconds(60))
                                .writeTimeout(ofSeconds(60))
                                .logRequests()
                                .logResponses()
                                .logStreamingResponses()
                                .build();

                // sycn request
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                                .addUserMessage("我日薪8块钱，请问在闰年的二月，我月薪多少")
                                .build();

                System.out.println(client.chat(request).execute());

                // async request
                CompletableFuture<String> future = new CompletableFuture<>();
                client.chat("关于月亮的传说")
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                String response = future.get(30, SECONDS);
                System.out.println(response);

                client.shutdown();
        }
}