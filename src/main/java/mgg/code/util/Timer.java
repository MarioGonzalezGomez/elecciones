package mgg.code.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Timer {
    public static long startTime;
    public static long endTime;

    private static Timer timer;
    private AtomicInteger nTimer = new AtomicInteger(-1);

    private Timer() {
        startTime = 0;
        endTime = 0;
    }

    public static Timer getInstance() {
        if (timer == null)
            timer = new Timer();
        return timer;
    }

    public void startTimer(String tipo) {

        System.out.println("Iniciando timer " + tipo + "->" + nTimer.getAndIncrement() + " ->");
        startTime = System.currentTimeMillis();
    }

    public void calculateTime(String tipo) {
        endTime = System.currentTimeMillis();
        System.out.println("Resultado timer " + tipo + "->" + nTimer.get() + ": " + (endTime - startTime));
    }
}
