package com.test.jfx.Extra;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;

public interface WinGDIExtra extends WinGDI {
    WinDef.DWORD SRCCOPY = new WinDef.DWORD(0x00CC0020);
}