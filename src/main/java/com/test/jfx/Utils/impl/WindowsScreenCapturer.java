package com.test.jfx.Utils.impl;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.test.jfx.Utils.ScreenCapturer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

public class WindowsScreenCapturer implements ScreenCapturer {
    private static User32 user32 = User32.INSTANCE;
    private WinDef.HWND hWnd;
    private int width;
    private int height;
    private String windowName;
    private long startTime;

    public WindowsScreenCapturer(int width, int height, String windowName) {
        this.width = width;
        this.height = height;
        this.windowName = windowName;
    }

    public WindowsScreenCapturer(String windowName) {
        this(0, 0, windowName);
    }

    public WindowsScreenCapturer(int width, int height) {
        this(width, height, "desktop");
    }

    public WindowsScreenCapturer() {
        this((int) Screen.getPrimary().getBounds().getWidth(), (int) Screen.getPrimary().getBounds().getHeight(), "desktop");
    }

    public static List<String> getWindows() {
        List<String> list = new ArrayList<>();
        char[] windowText = new char[512];
        WinUser.WINDOWINFO windowInfo = new WinUser.WINDOWINFO();
        user32.EnumWindows((hWnd, arg1) -> {
            user32.GetWindowInfo(hWnd, windowInfo);
            user32.GetWindowText(hWnd, windowText, 512);
            String wText = Native.toString(windowText);

            if (wText.isEmpty()) {
                return true;
            }

            boolean isMinimized = (windowInfo.dwStyle & User32.WS_ICONIC) == User32.WS_ICONIC;
            boolean isVisible = (windowInfo.dwStyle & User32.WS_VISIBLE) == User32.WS_VISIBLE;
            if (windowInfo.dwStyle > 0 & !isMinimized && isVisible && windowInfo.cxWindowBorders != 0 && windowInfo.cyWindowBorders != 0) {
                list.add(wText);
                System.out.println("Found window with text " + hWnd + " Text: " + wText + " | Minimized: " + isMinimized + " | Visible: " + isVisible
                        + " | Bottom: " + windowInfo.rcWindow.bottom + " | Left: " + windowInfo.rcWindow.left + " | Window Style: " + windowInfo.dwStyle);
            }
            return true;
        }, null);
        return list;
    }

    @Override
    public boolean start() {
        if (windowName.equals("desktop")) {
            hWnd = user32.GetDesktopWindow();
        } else {
            hWnd = user32.FindWindow(null, windowName);
        }

        WinDef.RECT rect = new WinDef.RECT();
        user32.GetWindowRect(hWnd, rect);

        int windowWidth = rect.right - rect.left;
        int windowHeight = rect.top - rect.bottom;
        if (windowWidth < width || width == 0)
            width = windowWidth;
        if (windowHeight < height || height == 0)
            height = windowHeight;

        startTime = System.currentTimeMillis();

        return true;
    }

    @Override
    public void stop() {
        System.out.println("Capturing time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public Image getCapture() {
        return SwingFXUtils.toFXImage(GDI32Util.getScreenshot(hWnd), null);
    }

    @Override
    public int getFrameRate() {
        return 0;
    }

    @Override
    public void setFrameRate(int frameRate) {
    }
}
