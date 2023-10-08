package dev.baichuanai.chat;

import static dev.baichuanai.chat.Message.userMessage;
import static dev.baichuanai.chat.Role.ASSISTANT;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.baichuanai.BaichuanAiClient;
import dev.baichuanai.Model;
import dev.baichuanai.RateLimitAwareTest;

class ChatCompletionTest extends RateLimitAwareTest {

        private static final String USER_MESSAGE = "Write exactly the following 2 words: 'hello world'";

        private final BaichuanAiClient client = BaichuanAiClient.builder()
                        .logRequests()
                        .logResponses()
                        .build();

        @Test
        void testSimpleApi() {

                String response = client.chat(USER_MESSAGE, Model.BAICHUAN2_53B.toString()).execute();

                assertThat(response).containsIgnoringCase("hello world");
        }

        @MethodSource
        @ParameterizedTest
        void testCustomizableApi(ChatCompletionRequest request) {

                ChatCompletionResponse response = client.chat(request).execute();

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
