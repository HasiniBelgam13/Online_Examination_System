
package com.example.timer;

import javax.swing.Timer;

public class ExamTimer {

    private int remainingSeconds;
    private Timer timer;

    public ExamTimer(int durationMinutes,
                     java.util.function.IntConsumer onTick,
                     Runnable onTimeUp) {

        this.remainingSeconds = durationMinutes * 60;

        timer = new Timer(1000, e -> {
            remainingSeconds--;

            onTick.accept(remainingSeconds);

            if (remainingSeconds <= 0) {
                timer.stop();
                onTimeUp.run();
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void resume() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public String getFormattedTime() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
