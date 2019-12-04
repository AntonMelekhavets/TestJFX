package com.test.jfx.Utils;

import javafx.scene.image.Image;

public interface ScreenCapturer {
    boolean start();

    void stop();

    Image getCapture();

    int getFrameRate();

    void setFrameRate(int frameRate);
}
