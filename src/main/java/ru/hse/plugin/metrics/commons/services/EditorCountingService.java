package ru.hse.plugin.metrics.commons.services;

import static java.lang.Math.max;

public class EditorCountingService {
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void inc() {
        validate();
        counter++;
    }

    public void dec() {
        counter--;
        validate();
    }

    private void validate() {
        counter = max(counter, 0);
    }
}
