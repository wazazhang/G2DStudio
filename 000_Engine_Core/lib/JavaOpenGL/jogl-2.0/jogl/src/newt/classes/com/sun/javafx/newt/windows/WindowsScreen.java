/*
 * Copyright (c) 2008 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 */

package com.sun.javafx.newt.windows;

import com.sun.javafx.newt.*;
import javax.media.nativewindow.*;

public class WindowsScreen extends Screen {
    static {
        WindowsDisplay.initSingleton();
    }


    public WindowsScreen() {
    }

    protected void createNative(int index) {
        aScreen = new DefaultGraphicsScreen(getDisplay().getGraphicsDevice(), index);
        ScreensInfo.Data data =
            WindowsScreensInfo.getInstance().getScreenData(getIndex());
        setScreenSize(data.w, data.h);
    }

    protected void closeNative() { }

    public int getX() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).x;
    }

    public int getY() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).y;
    }

    public int getWorkX() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).wx;
    }

    public int getWorkY() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).wy;
    }
    
    public int getWorkWidth() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).ww;
    }

    public int getWorkHeight() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).wh;
    }

    public float getDPIX() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).dpiX;
    }

    public float getDPIY() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex()).dpiY;
    }

    public ScreensInfo.Data getData() {
        return WindowsScreensInfo.getInstance().getScreenData(getIndex());
    }

}
