# baichuanai-java-sdk
调用百川大模型的Java SDK。目前仅支持同步和异步调用，不支持流式调用。
# 使用代码参考

执行代码前需要在先执行
<br>
export BAICHUANAI_API_KEY=[your baichuanai api key]
export BAICHUANAI_SECRET_KEY=[your baichuanai secret key]

```
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
```
