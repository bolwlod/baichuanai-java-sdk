package dev.llm.baichuanai.chat;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MessageTypeAdapter extends TypeAdapter<Message> {

    private final TypeAdapter<Message> delegate;

    public static final TypeAdapterFactory MESSAGE_TYPE_ADAPTER_FACTORY = new TypeAdapterFactory() {
        
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() != Message.class) {
                return null;
            } else {
                TypeAdapter<Message> delegate = (TypeAdapter<Message>) gson.getDelegateAdapter(this, type);
                return (TypeAdapter<T>) new MessageTypeAdapter(delegate);
            }
        }
    };

    private MessageTypeAdapter(TypeAdapter<Message> delegate) {
        this.delegate = delegate;
    }

    public void write(JsonWriter out, Message message) throws IOException {
        out.beginObject();
        out.name("role");
        out.value(message.role().toString());
        out.name("content");
        if (message.content() == null) {
            boolean serializeNulls = out.getSerializeNulls();
            out.setSerializeNulls(true);
            out.nullValue();
            out.setSerializeNulls(serializeNulls);
        } else {
            out.value(message.content());
        }

        out.endObject();
    }

    public Message read(JsonReader in) throws IOException {
        return (Message)this.delegate.read(in);
    }
}
