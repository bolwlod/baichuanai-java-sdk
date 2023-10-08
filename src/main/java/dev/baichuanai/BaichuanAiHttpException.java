package dev.baichuanai;

public class BaichuanAiHttpException extends RuntimeException {
    
    private final int code;

    public BaichuanAiHttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
