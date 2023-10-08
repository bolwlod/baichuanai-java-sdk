package dev.llm.baichuanai.chat;

import java.util.Objects;

import lombok.NonNull;

public final class Message {

    /*
     * user=用户, assistant=助手
     */
    private final Role role;
    /*
     * 内容
     */
    private final String content;
    private final String finishReason;

    private Message(Builder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.finishReason = builder.finishReason;
    }

    public Role role() {
        return role;
    }

    public String content() {
        return content;
    }

    public String finishReason() {
        return finishReason;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof Message
                && equalTo((Message) another);
    }

    private boolean equalTo(Message another) {
        return Objects.equals(role, another.role)
                && Objects.equals(content, another.content)
                && Objects.equals(finishReason, another.finishReason);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(role);
        h += (h << 5) + Objects.hashCode(content);
        h += (h << 5) + Objects.hashCode(finishReason);
        return h;
    }

    @Override
    public String toString() {
        return "Message{"
                + "role=" + role
                + ", content=" + content
                + ", finishReason=" + finishReason
                + "}";
    }

    public static Message userMessage(String content) {
        return Message.builder()
                .role(Role.USER)
                .content(content)
                .build();
    }

    public static Message assistantMessage(String content) {
        return Message.builder()
                .role(Role.ASSISTANT)
                .content(content)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Role role;
        private String content;
        private String finishReason;

        private Builder() {
        }

        public Builder role(@NonNull Role role) {
            this.role = role;
            return this;
        }

        public Builder role(@NonNull String role) {
            return role(Role.from(role));
        }

        public Builder content(@NonNull String content) {
            this.content = content;
            return this;
        }

        public Builder finishReason(String finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
