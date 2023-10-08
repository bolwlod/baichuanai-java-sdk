package dev.llm.baichuanai.chat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class ChatCompletionResponse {

    /*
     * 状态码，0 表示成功，非 0 表示失败
     */
    private final Integer code;
    /*
     * 提示信息
     */
    private final String msg;

    /*
     * 对话结果
     */
    private final Data data;

    /*
     * token 使用信息
     */
    private final Usage usage;

    private ChatCompletionResponse(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
        this.usage = builder.usage;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatCompletionResponse that = (ChatCompletionResponse) o;

        return new EqualsBuilder()
                .append(code, that.code)
                .append(msg, that.msg)
                .append(data, that.data)
                .append(usage, that.usage)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(code).append(msg).append(data).append(usage).toHashCode();
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "code='" + code + '\'' +
                ", msg=" + msg +
                ", data='" + data + '\'' +
                ", usage='" + usage + '\'' +
                '}';
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public Usage usage() {
        return this.usage;
    }

    public Data data() {
        return this.data;
    }

    public String content() {
        return data().messages().get(0).content();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Integer code;
        private String msg;
        private Data data;
        private Usage usage;

        private Builder() {
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Data data) {
            this.data = data;
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public ChatCompletionResponse build() {
            return new ChatCompletionResponse(this);
        }
    }
}
