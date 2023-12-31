package dev.llm.baichuanai;

import dev.llm.baichuanai.chat.ChatCompletionRequest;
import dev.llm.baichuanai.chat.ChatCompletionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BaichuanAiApi {

        @POST("chat")
        @Headers({ "Content-Type: application/json" })
        Call<ChatCompletionResponse> chat(@Body ChatCompletionRequest request);

}
