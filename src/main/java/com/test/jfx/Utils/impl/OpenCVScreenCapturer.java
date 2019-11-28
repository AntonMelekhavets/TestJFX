package com.test.jfx.Utils.impl;

import com.test.jfx.Utils.ScreenCapturer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Screen;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.JavaFXFrameConverter;

import java.io.IOException;

public class OpenCVScreenCapturer implements ScreenCapturer {
    private int width;
    private int height;
    private String windowName;
    private int frameRate = 30;
    private FrameGrabber frameGrabber;
    private FrameConverter<Image> frameConverter;
    private long startTime;

    public OpenCVScreenCapturer() {
        this(0, 0, "desktop");
    }

    public OpenCVScreenCapturer(int width, int height) {
        this(width, height, "desktop");
    }

    public OpenCVScreenCapturer(int width, int height, String windowName) {
        this.width = width;
        this.height = height;
        this.windowName = windowName;
    }

    public OpenCVScreenCapturer(String windowName) {
        this(0, 0, windowName);
    }

    @Override
    public boolean start() {
        try {
            if (windowName.equals("desktop")) {
                frameGrabber = new FFmpegFrameGrabber(windowName);

                int windowWidth = (int) Screen.getPrimary().getBounds().getWidth();
                int windowHeight = (int) Screen.getPrimary().getBounds().getHeight();
                if (windowHeight < height || height == 0) {
                    height = windowHeight;
                }
                if (windowWidth < width || width == 0)
                    width = windowWidth;
            } else {
                frameGrabber = new FFmpegFrameGrabber("title=" + windowName);
            }

            frameGrabber.setFormat("gdigrab");
            frameGrabber.setFrameRate(frameRate);
            frameGrabber.setImageWidth(width);
            frameGrabber.setImageHeight(height);
            frameConverter = new JavaFXFrameConverter();
            frameGrabber.start();

            startTime = System.currentTimeMillis();
            return true;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void configLinux() {
        int x = 0;
        int y = 0;
        String display = "192.168.9.37:0+";

        frameGrabber = new FFmpegFrameGrabber(display + x + "," + y);
        frameGrabber.setFormat("x11grab");
        frameGrabber.setImageWidth(width);
        frameGrabber.setImageHeight(height);
        frameGrabber.setFrameRate(frameRate);
        frameGrabber.setOption("draw_mouse", "0");
    }

    @Override
    public void stop() {
        try {
            System.out.println("Capturing time: " + (System.currentTimeMillis() - startTime));
            frameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getCapture() {
        try {
            return frameConverter.convert(frameGrabber.grabFrame());
        } catch (IOException e) {
            e.printStackTrace();
            return new WritableImage(width, height);
        }
    }

    @Override
    public int getFrameRate() {
        return frameRate;
    }

    @Override
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }
}
