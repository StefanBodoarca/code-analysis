package com.lmig.ci.grs.salesforce.file_connector.util;

import lombok.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * Custom exchange filter for calculating the length of multipart request.
 */
public class MultipartExchangeFilterFunction implements ExchangeFilterFunction {
    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, @NonNull ExchangeFunction next) {
        if (MediaType.MULTIPART_FORM_DATA.includes(request.headers().getContentType())
                && (request.method() == HttpMethod.PUT
                || request.method() == HttpMethod.POST)) {
            return next.exchange(
                    ClientRequest.from(request)
                            .body(
                                    (outputMessage, context) ->
                                            request.body().insert(new BufferingDecorator(outputMessage), context)
                            )
                            .build()
            );
        } else {
            return next.exchange(request);
        }
    }

    /**
     * Custom decorator that adds the content length to the header of the request.
     */
    private static final class BufferingDecorator extends ClientHttpRequestDecorator {

        private BufferingDecorator(ClientHttpRequest delegate) {
            super(delegate);
        }

        @Override
        @NonNull
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
            return DataBufferUtils.join(body).flatMap(buffer -> {
                getHeaders().setContentLength(buffer.readableByteCount());
                return super.writeWith(Mono.just(buffer));
            });
        }
    }
}
