package com.test.jfx.Control;

import com.test.jfx.Utils.ScreenCapturer;
import com.test.jfx.Utils.impl.OpenCVScreenCapturer;
import com.test.jfx.Utils.impl.RobotScreenCapturer;
import com.test.jfx.Utils.impl.WindowsScreenCapturer;
import javafx.scene.image.ImageView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FrameTimer {
    private ImageView imageView;
    private ScreenCapturer screenCapturer;
    private ScheduledExecutorService service;

    public FrameTimer(ImageView imageView, String windowName) {
        this.imageView = imageView;

        screenCapturer = new OpenCVScreenCapturer(windowName);
        screenCapturer.setFrameRate(60);

//        screenCapturer = new RobotScreenCapturer();

//        screenCapturer = new WindowsScreenCapturer(windowName);

        service = Executors.newScheduledThreadPool(5);
    }

    public void start() {
        screenCapturer.start();
        service.scheduleWithFixedDelay(() -> {
            imageView.setImage(screenCapturer.getCapture());
        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        service.shutdownNow();
        screenCapturer.stop();
    }
}
