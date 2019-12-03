package com.test.jfx.Utils.impl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.test.jfx.Extra.X11Extra;
import com.test.jfx.Utils.ScreenCapturer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LinuxScreenCapturer implements ScreenCapturer {
    private static X11Extra x11Extra = X11Extra.INSTANCE;
    private final String DISPLAY = "192.168.9.37:0";
    private int width;
    private int height;
    private long startTime;
    private X11.Display display;
    private X11.Visual visual;
    private Pointer pointer;

    public LinuxScreenCapturer(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public LinuxScreenCapturer() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        height = (int) bounds.getHeight();
        width = (int) bounds.getWidth();
    }


    @Override
    public boolean start() {
        try {
            display = x11Extra.XOpenDisplay(DISPLAY);
            X11.Window window = x11Extra.XDefaultRootWindow(display);
            X11.XWindowAttributes xWindowAttributes = new X11.XWindowAttributes();
            x11Extra.XGetWindowAttributes(display, window, xWindowAttributes);
            visual = new X11.Visual();
            pointer = new Memory(1920 * 1080 * 4);
            startTime = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void stop() {
        System.out.println("Capturing time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public Image getCapture() {
        try {
            X11.XImage xImage = x11Extra.XCreateImage(display, visual, 32, X11.ZPixmap, 0, pointer, width, height, 32, width * 4);
            if (xImage != null) {
                byte[] byteImage = xImage.getPointer().getByteArray(0, width * height);
                return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(byteImage)), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WritableImage(width, height);
    }

    @Override
    public int getFrameRate() {
        return 0;
    }

    @Override
    public void setFrameRate(int frameRate) {
    }
}
