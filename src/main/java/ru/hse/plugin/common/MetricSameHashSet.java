package ru.hse.plugin.common;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.util.MetricWrapper;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;

import static ru.hse.plugin.metrics.commons.util.MetricWrapper.*;

public class MetricSameHashSet extends AbstractSet<Metric> {
    private final HashSet<MetricWrapper> delegate = new HashSet<>();

    @Override
    public boolean add(Metric metric) {
        return delegate.add(wrap(metric));
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Metric && delegate.contains(wrap((Metric) o));
    }

    @Override
    public boolean remove(Object o) {
        return o instanceof Metric && delegate.remove(wrap((Metric) o));
    }

    @Override
    @NotNull
    public Iterator<Metric> iterator() {
        final Iterator<MetricWrapper> wrapperIterator = delegate.iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return wrapperIterator.hasNext();
            }

            @Override
            public Metric next() {
                return wrapperIterator.next().unwrap();
            }

            @Override
            public void remove() {
                wrapperIterator.remove();
            }
        };
    }
}
