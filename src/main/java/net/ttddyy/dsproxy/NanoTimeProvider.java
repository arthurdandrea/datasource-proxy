package net.ttddyy.dsproxy;

import java.util.concurrent.TimeUnit;

public final class NanoTimeProvider implements TimeProvider {
    public static NanoTimeProvider INSTANCE = new NanoTimeProvider();

    private NanoTimeProvider() {
    }

    @Override
    public long getCurrentTime() {
        return System.nanoTime();
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.NANOSECONDS;
    }
}
