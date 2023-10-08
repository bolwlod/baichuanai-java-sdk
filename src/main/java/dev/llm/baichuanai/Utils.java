package dev.llm.baichuanai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.llm.baichuanai.chat.MessageTypeAdapter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Response;

@Slf4j
public class Utils {

    public static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(MessageTypeAdapter.MESSAGE_TYPE_ADAPTER_FACTORY)
                .create();
    }

    Utils() {
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static String printHeadersInOneLine(Headers headers) {
        return (String) StreamSupport.stream(headers.spliterator(), false).map((header) -> {
            String headerKey = (String) header.component1();
            String headerValue = (String) header.component2();

            return String.format("[%s: %s]", headerKey, headerValue);
        }).collect(Collectors.joining(", "));
    }

    public static RuntimeException toException(Response<?> response) throws IOException {
        return new BaichuanAiHttpException(response.code(), response.errorBody().string());
    }

    public static RuntimeException toException(okhttp3.Response response) throws IOException {
        return new BaichuanAiHttpException(response.code(), response.body().string());
    }

    public static String hashWithMD5(String input) {
        try {
            // 获取一个MD5算法的MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 用输入的字节更新摘要
            md.update(input.getBytes(StandardCharsets.UTF_8));

            // 完成哈希计算并返回结果
            byte[] digest = md.digest();

            // 将生成的字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // 如果MD5算法不可用，抛出异常
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public static String getBody(Request request) {
        RequestBody body = request.body();

        if (body == null){
            return "";
        }

        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            log.warn("Exception happened while reading request body", e);
            return "[Exception happened while reading request body. Check logs for more details.]";
        }
    }
}