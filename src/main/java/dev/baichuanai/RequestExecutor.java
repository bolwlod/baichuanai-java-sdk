package dev.baichuanai;

import java.util.function.Consumer;
import java.util.function.Function;

import retrofit2.Call;

public class RequestExecutor<Request, Response, ResponseContent> implements SyncOrAsync<ResponseContent> {

    private final Call<Response> call;
    private final Function<Response, ResponseContent> responseContentExtractor;

    public RequestExecutor(Call<Response> call, Function<Response, ResponseContent> responseContentExtractor) {
        this.call = call;
        this.responseContentExtractor = responseContentExtractor;
    }

    @Override
    public ResponseContent execute() {

        return new SyncRequestExecutor<>(this.call, this.responseContentExtractor).execute();
    }

    @Override
    public AsyncResponseHandling onResponse(Consumer<ResponseContent> responseHandler) {
        return new AsyncRequestExecutor<>(this.call, this.responseContentExtractor).onResponse(responseHandler);
    }

}
