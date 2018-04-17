package org.dan.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReportDataWorker {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(new OneMinuteCallBack(), 0 ,60, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new HalfCallBack(), 0 ,30, TimeUnit.MINUTES);
        scheduledExecutorService.scheduleAtFixedRate(new OneHourCallBack(), 0 ,60, TimeUnit.MINUTES);
    }
}
