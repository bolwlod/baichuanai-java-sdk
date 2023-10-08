package dev.llm.baichuanai.chat;

import static dev.llm.baichuanai.chat.Message.userMessage;
import static dev.llm.baichuanai.chat.Role.ASSISTANT;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.llm.baichuanai.BaichuanAiClient;
import dev.llm.baichuanai.RateLimitAwareTest;

class ChatCompletionAsyncTest extends RateLimitAwareTest {

        private static final String USER_MESSAGE = "write exactly the following 2 words: 'hello world'";

        private final BaichuanAiClient client = BaichuanAiClient.builder()
                        .logRequests()
                        .logResponses()
                        .build();

        @Test
        void testSimpleApi() throws Exception {

                CompletableFuture<String> future = new CompletableFuture<>();

                client.chat(USER_MESSAGE)
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                String response = future.get(30, SECONDS);

                assertThat(response).containsIgnoringCase("hello world");
        }

        @MethodSource
        @ParameterizedTest
        void testCustomizableApi(ChatCompletionRequest request) throws Exception {

                CompletableFuture<ChatCompletionResponse> future = new CompletableFuture<>();

                client.chat(request)
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                ChatCompletionResponse response = future.get(30, SECONDS);

                assertThat(response.data().messages()).hasSize(1);
                assertThat(response.data().messages().get(0).role()).isEqualTo(ASSISTANT);
                assertThat(response.data().messages().get(0).content()).containsIgnoringCase("hello world");

                assertThat(response.content()).containsIgnoringCase("hello world");
        }

        static Stream<Arguments> testCustomizableApi() {
                return Stream.of(
                                Arguments.of(
                                                ChatCompletionRequest.builder()
                                                                .messages(singletonList(userMessage(USER_MESSAGE)))
                                                                .build()),
                                Arguments.of(
                                                ChatCompletionRequest.builder()
                                                                .messages(userMessage(USER_MESSAGE))
                                                                .build()),
                                Arguments.of(
                                                ChatCompletionRequest.builder()
                                                                .addUserMessage(USER_MESSAGE)
                                                                .build()));
        }

}