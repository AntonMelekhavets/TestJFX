package com.test.jfx.Utils.impl;

import com.test.jfx.Utils.ScreenCapturer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.awt.*;

public class RobotScreenCapturer implements ScreenCapturer {
    private int width;
    private int height;
    private Robot robot;
    private Rectangle rectangle;
    private long startTime;

    public RobotScreenCapturer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public RobotScreenCapturer() {
        this((int) Screen.getPrimary().getBounds().getWidth(), (int) Screen.getPrimary().getBounds().getHeight());
    }

    @Override
    public boolean start() {
        try {
            robot = new Robot();
            rectangle = new Rectangle(width, height);

            startTime = System.currentTimeMillis();
            return true;
        } catch (AWTException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void stop() {
        System.out.println("Capturing time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public Image getCapture() {
        return SwingFXUtils.toFXImage(robot.createScreenCapture(rectangle), null);
    }

    @Override
    public int getFrameRate() {
        return 0;
    }

    @Override
    public void setFrameRate(int frameRate) {
    }
}
