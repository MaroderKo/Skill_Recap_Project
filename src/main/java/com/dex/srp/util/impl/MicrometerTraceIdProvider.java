package com.dex.srp.util.impl;

import com.dex.srp.util.TraceIdProvider;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MicrometerTraceIdProvider implements TraceIdProvider {

    private final Tracer tracer;


    @Override
    public String getTraceId() {
        Span span = tracer.currentSpan();
        if (span != null) {
            return span.context().traceId();
        }
        return null;
    }


}
