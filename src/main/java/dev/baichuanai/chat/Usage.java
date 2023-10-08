package dev.baichuanai.chat;

import java.util.Objects;

public final class Usage {

    /*
     * 	prompt 的 token 数
     */
    private final Integer promptTokens;

    /*
     * 回答生成的 token 数
     */
    private final Integer answerTokens;

    /*
     * 会话的总 token 数
     */
    private final Integer totalTokens;

    private Usage(Builder builder) {
        this.promptTokens = builder.promptTokens;
        this.answerTokens = builder.answerTokens;
        this.totalTokens = builder.totalTokens;
    }

    public Integer promptTokens() {
        return promptTokens;
    }

    public Integer completionTokens() {
        return answerTokens;
    }

    public Integer totalTokens() {
        return totalTokens;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof Usage
                && equalTo((Usage) another);
    }

    private boolean equalTo(Usage another) {
        return Objects.equals(promptTokens, another.promptTokens)
                && Objects.equals(answerTokens, another.answerTokens)
                && Objects.equals(totalTokens, another.totalTokens);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(promptTokens);
        h += (h << 5) + Objects.hashCode(answerTokens);
        h += (h << 5) + Objects.hashCode(totalTokens);
        return h;
    }

    @Override
    public String toString() {
        return "Usage{"
                + "promptTokens=" + promptTokens
                + ", answerTokens=" + answerTokens
                + ", totalTokens=" + totalTokens
                + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Integer promptTokens;
        private Integer answerTokens;
        private Integer totalTokens;

        private Builder() {
        }

        public Builder promptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder answerTokens(Integer answerTokens) {
            this.answerTokens = answerTokens;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Usage build() {
            return new Usage(this);
        }
    }
}