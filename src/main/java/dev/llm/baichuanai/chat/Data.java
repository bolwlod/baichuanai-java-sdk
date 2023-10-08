package dev.llm.baichuanai.chat;

import java.util.List;
import java.util.Objects;

public final class Data {

    /*
     * 对话消息列表(历史对话按从老到新顺序填入)
     */
    private final List<Message> messages;

    private Data(Builder builder) {
        this.messages = builder.messages;

    }

    public List<Message> messages() {
        return messages;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof Data
                && equalTo((Data) another);
    }

    private boolean equalTo(Data another) {
        return Objects.equals(messages, another.messages);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(messages);
        return h;
    }

    @Override
    public String toString() {
        return "Data{"
                + "messages=" + messages
                + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<Message> messages;

        private Builder() {
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Data build() {
            return new Data(this);
        }
    }
}
