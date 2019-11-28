package com.test.jfx.Utils;

import javafx.scene.image.Image;

public interface ScreenCapturer {
    public boolean start();

    public void stop();

    public Image getCapture();

    public int getFrameRate();

    public void setFrameRate(int frameRate);
}
