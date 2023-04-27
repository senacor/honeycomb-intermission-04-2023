package io.honeycomb.examples.javaotlp;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static io.opentelemetry.api.GlobalOpenTelemetry.getPropagators;

@RestController
public class YearController {
    @Autowired
    private YearService yearService;

    @RequestMapping("/year")
    public String index(@RequestHeader Map<String, String> headers) {
            return yearService.getYear();
    }
}
