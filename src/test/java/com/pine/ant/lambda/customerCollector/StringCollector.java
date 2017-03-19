package com.pine.ant.lambda.customerCollector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StringCollector implements Collector<String, StringBuffer, String> {

    @Override
    public Supplier<StringBuffer> supplier() {
        return () ->new StringBuffer();
    }

    @Override
    public BiConsumer<StringBuffer, String> accumulator() {
        return StringBuffer::append;
    }

    @Override
    public BinaryOperator<StringBuffer> combiner() {
        return (sb1,sb2) -> sb1.append(sb2);
    }

    @Override
    public Function<StringBuffer, String> finisher() {
        return StringBuffer::toString;
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        Set<Characteristics> set = new HashSet<>();
        set.add(Characteristics.CONCURRENT);
        set.add(Characteristics.IDENTITY_FINISH);
        return set;
    }

}
