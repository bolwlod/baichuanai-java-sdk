package dev.llm.baichuanai;

public enum Model {
    
    // chat models
    BAICHUAN2_53B("Baichuan2-53B");

    private final String stringValue;

    private Model(String stringValue) {
        this.stringValue = stringValue;
    }

    public String stringValue() {
        return this.stringValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    
}
