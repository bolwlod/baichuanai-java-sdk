package dev.baichuanai.chat;

import java.util.Objects;

public class Parameters {

    /*
     * 取值范围: [.0f, 1.0f]。 多样性，越高，多样性越好, 缺省 0.3
     */
    private final Double temperature;
    /*
     * 取值范围: [0, 20]。搜索采样控制参数，越大，采样集大, 
     * 0 则不走 top_k 采样筛选策略，最大 20(超过 20 会被修正成 20)，
     * 缺省 5
     */
    private final Integer topK;
    /*
     * 取值范围: [.0f, 1.0f]。值越小，越容易出头部, 缺省 0.85
     */
    private final Double topP;
    /*
     * True: 开启搜索增强，搜索增强会产生额外的费用, 缺省 False
     */
    private final Boolean withSearchEnhance;

    private Parameters(Builder builder) {
        this.temperature = builder.temperature;
        this.topK = builder.topK;
        this.topP = builder.topP;
        this.withSearchEnhance = builder.withSearchEnhance;
    }

    public Double temperature() {
        return this.temperature;
    }

    public Integer topK() {
        return this.topK;
    }

    public Double topP() {
        return this.topP;
    }

    public Boolean withSearchEnhance() {
        return this.withSearchEnhance;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        } else {
            return another instanceof Parameters
                    && this.equalTo((Parameters) another);
        }
    }

    private boolean equalTo(Parameters another) {
        return Objects.equals(this.temperature, another.temperature) && Objects.equals(this.topK, another.topK)
                && Objects.equals(this.topP, another.topP)
                && Objects.equals(this.withSearchEnhance, another.withSearchEnhance);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.temperature);
        h += (h << 5) + Objects.hashCode(this.topK);
        h += (h << 5) + Objects.hashCode(this.topP);
        h += (h << 5) + Objects.hashCode(this.withSearchEnhance);
        return h;
    }

    public String toString() {
        return "Parameters{temperature=" + this.temperature + ", topK=" + this.topK + ", topP=" + this.topP
                + ", withSearchEnhance=" + this.withSearchEnhance + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Double temperature = Double.valueOf(0.3);
        private Integer topK = Integer.valueOf(5);
        private Double topP = Double.valueOf(0.85);
        private Boolean withSearchEnhance = Boolean.valueOf(false);

        private Builder() {

        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder withSearchEnhance(Boolean withSearchEnhance) {
            this.withSearchEnhance = withSearchEnhance;
            return this;
        }

        public Parameters build() {
            return new Parameters(this);
        }
    }
}
