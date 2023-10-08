package dev.baichuanai.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.baichuanai.Model;
import lombok.NonNull;

public final class ChatCompletionRequest {

    transient private final Boolean stream;

    /*
     * 使用的模型 ID，当前默认 Baichuan2-53B
     */
    private final String model;
    /*
     * 对话消息列表 (历史对话按从老到新顺序填入)
     */
    private final List<Message> messages;

    /*
     * 参数
     */
    private final Parameters parameters;

    private ChatCompletionRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.parameters = builder.parameters;
        this.stream = builder.stream;
    }

    public String model() {
        return this.model;
    }

    public List<Message> messages() {
        return this.messages;
    }

    public Parameters parameters() {
        return this.parameters;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ChatCompletionRequest that = (ChatCompletionRequest) object;

        return new EqualsBuilder()
                .append(model, that.model)
                .append(messages, that.messages)
                .append(parameters, that.parameters).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(model).append(messages).append(parameters).toHashCode();
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                ", model=" + model +
                ", messages=" + messages +
                ", parameters=" + parameters +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String model = Model.BAICHUAN2_53B.toString();
        private List<Message> messages;
        private Parameters parameters;;
        private Boolean stream = null;

        private Builder() {
        }

        public Builder from(
                ChatCompletionRequest instance) {
            this.model(instance.model);
            this.messages(instance.messages);
            this.parameters(instance.parameters);
            this.stream(instance.stream);
            return this;
        }

        public Builder model(@NonNull String model) {
            this.model = model;
            return this;
        }

        public Builder messages(@NonNull List<Message> messages) {

            this.messages = Collections.unmodifiableList(messages);
            return this;

        }

        public Builder messages(@NonNull Message... messages) {
            return this.messages(Arrays.asList(messages));
        }

        public Builder addUserMessage(String userMessage) {
            if (this.messages == null) {
                this.messages = new ArrayList<Message>();
            }

            this.messages.add(Message.userMessage(userMessage));
            return this;
        }

        public Builder addAssistantMessage(String assistantMessage) {
            if (this.messages == null) {
                this.messages = new ArrayList<Message>();
            }

            this.messages.add(Message.assistantMessage(assistantMessage));
            return this;
        }

        public Builder parameters(Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(this);
        }
    }
}
