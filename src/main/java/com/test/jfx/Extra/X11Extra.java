package com.test.jfx.Extra;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.X11;

public interface X11Extra extends X11 {
    X11Extra INSTANCE = (X11Extra) Native.load("libX11", X11.class);
}
