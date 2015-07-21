package net.ttddyy.dsproxy;

import java.util.concurrent.TimeUnit;

public final class CurrentTimeProvider implements TimeProvider {
    public static CurrentTimeProvider INSTANCE = new CurrentTimeProvider();

    private CurrentTimeProvider() {
    }

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}
