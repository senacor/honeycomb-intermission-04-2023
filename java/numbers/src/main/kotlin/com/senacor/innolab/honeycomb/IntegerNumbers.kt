package com.senacor.innolab.honeycomb

import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
class IntegerNumbers(private val tracer: Tracer, private val datasource: DataSource) {


    @GetMapping("/numbers")
    fun numbers(): List<Int> =
        trace("/numbers") {
            (0..1000).asSequence()
                .filter { Math.random() < 0.5 }
                .toList()
        }

    @GetMapping("/next")
    fun next(): Int =
        datasource.connection.use { con ->
            con.createStatement().use { stmt ->
                stmt.executeQuery("SELECT floor(random() * 1000)::int as number").use { rs ->
                    rs.next()
                    rs.getInt("number")
                }
            }
        }

    private fun <T> trace(spanName: String, f: Span.() -> T) =
        with(tracer.spanBuilder(spanName)
            .setSpanKind(SpanKind.SERVER)
            .startSpan()
            .apply { Context.current().with(this) }) {
            runCatching(f)
                .onFailure {
                    setAttribute("result", "error")
                    setAttribute("exception", it::class.simpleName!!)
                }.onSuccess {
                    setAttribute("result", "success")
                }.also { end() }
                .getOrThrow()
        }
}
