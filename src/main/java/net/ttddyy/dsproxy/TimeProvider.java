package net.ttddyy.dsproxy;

import java.util.concurrent.TimeUnit;

public interface TimeProvider {
    long getCurrentTime();
    TimeUnit getTimeUnit();
}
