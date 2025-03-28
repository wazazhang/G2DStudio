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

package com.sun.javafx.newt.awt;

import com.sun.javafx.newt.Window;

import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;

import javax.media.nativewindow.*;
import javax.media.nativewindow.awt.*;
import com.sun.javafx.newt.impl.Debug;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class AWTCanvas extends Canvas {
  private GraphicsDevice device;
  private GraphicsConfiguration chosen;
  private AWTGraphicsConfiguration awtConfig;

  private Capabilities capabilities;

  private boolean displayConfigChanged=false;

  public AWTCanvas(Capabilities capabilities) {
    super();

    if(null==capabilities) {
        throw new NativeWindowException("Capabilities null");
    }
    this.capabilities=capabilities;
  }

  public AWTGraphicsConfiguration getAWTGraphicsConfiguration() {
    return awtConfig;
  }

  public boolean hasDeviceChanged() {
    boolean res = displayConfigChanged;
    displayConfigChanged=false;
    return res;
  }

  public void addNotify() {
    super.addNotify();

    disableBackgroundErase();

    GraphicsConfiguration gc = super.getGraphicsConfiguration();
    if(null!=gc) {
        device = gc.getDevice();
    }

    /*
     * Save the chosen capabilities for use in getGraphicsConfiguration().
     */
    awtConfig = chooseGraphicsConfiguration(capabilities, device);
    if(Window.DEBUG_IMPLEMENTATION) {
        Exception e = new Exception("Created Config: "+awtConfig);
        e.printStackTrace();
    }
    if(null!=awtConfig) {
      // update ..
      chosen = awtConfig.getGraphicsConfiguration();
    }
    if(null==awtConfig) {
          throw new NativeWindowException("Error: AWTGraphicsConfiguration is null");
    }
  }

  /**
   * Overridden to choose a GraphicsConfiguration on a parent container's
   * GraphicsDevice because both devices
   */
  public GraphicsConfiguration getGraphicsConfiguration() {
    /*
     * Workaround for problems with Xinerama and java.awt.Component.checkGD
     * when adding to a container on a different graphics device than the
     * one that this Canvas is associated with.
     * 
     * GC will be null unless:
     *   - A native peer has assigned it. This means we have a native
     *     peer, and are already comitted to a graphics configuration.
     *   - This canvas has been added to a component hierarchy and has
     *     an ancestor with a non-null GC, but the native peer has not
     *     yet been created. This means we can still choose the GC on
     *     all platforms since the peer hasn't been created.
     */
    final GraphicsConfiguration gc = super.getGraphicsConfiguration();
    /*
     * chosen is only non-null on platforms where the GLDrawableFactory
     * returns a non-null GraphicsConfiguration (in the GLCanvas
     * constructor).
     * 
     * if gc is from this Canvas' native peer then it should equal chosen,
     * otherwise it is from an ancestor component that this Canvas is being
     * added to, and we go into this block.
     */
    if (gc != null && chosen != null && !chosen.equals(gc)) {
      /*
       * Check for compatibility with gc. If they differ by only the
       * device then return a new GCconfig with the super-class' GDevice
       * (and presumably the same visual ID in Xinerama).
       * 
       */
      if (!chosen.getDevice().getIDstring().equals(gc.getDevice().getIDstring())) {
        /*
         * Here we select a GraphicsConfiguration on the alternate
         * device that is presumably identical to the chosen
         * configuration, but on the other device.
         * 
         * Should really check to ensure that we select a configuration
         * with the same X visual ID for Xinerama screens, otherwise the
         * GLDrawable may have the wrong visual ID (I don't think this
         * ever gets updated). May need to add a method to
         * X11GLDrawableFactory to do this in a platform specific
         * manner.
         * 
         * However, on platforms where we can actually get into this
         * block, both devices should have the same visual list, and the
         * same configuration should be selected here.
         */
        AWTGraphicsConfiguration config = chooseGraphicsConfiguration((Capabilities)awtConfig.getRequestedCapabilities(), gc.getDevice());
        final GraphicsConfiguration compatible = (null!=config)?config.getGraphicsConfiguration():null;
        if(Window.DEBUG_IMPLEMENTATION) {
            Exception e = new Exception("Call Stack: "+Thread.currentThread().getName());
            e.printStackTrace();
            System.err.println("!!! Created Config (n): HAVE    GC "+chosen);
            System.err.println("!!! Created Config (n): THIS    GC "+gc);
            System.err.println("!!! Created Config (n): Choosen GC "+compatible);
            System.err.println("!!! Created Config (n): HAVE    CF "+awtConfig);
            System.err.println("!!! Created Config (n): Choosen CF "+config);
            System.err.println("!!! Created Config (n): EQUALS CAPS "+config.getChosenCapabilities().equals(awtConfig.getChosenCapabilities()));
        }

        if (compatible != null) {
          /*
           * Save the new GC for equals test above, and to return to
           * any outside callers of this method.
           */
          chosen = compatible;
          if( !config.getChosenCapabilities().equals(awtConfig.getChosenCapabilities())) {
              displayConfigChanged=true;
          } 
          awtConfig = config;
        }
      }

      /*
       * If a compatible GC was not found in the block above, this will
       * return the GC that was selected in the constructor (and might
       * cause an exception in Component.checkGD when adding to a
       * container, but in this case that would be the desired behavior).
       * 
       */
      return chosen;
    } else if (gc == null) {
      /*
       * The GC is null, which means we have no native peer, and are not
       * part of a (realized) component hierarchy. So we return the
       * desired visual that was selected in the constructor (possibly
       * null).
       */
      return chosen;
    }

    /*
     * Otherwise we have not explicitly selected a GC in the constructor, so
     * just return what Canvas would have.
     */
    return gc;
  }

  private static AWTGraphicsConfiguration chooseGraphicsConfiguration(Capabilities capabilities,
                                                                      GraphicsDevice device) {
    AbstractGraphicsScreen aScreen = AWTGraphicsScreen.createScreenDevice(device);
    AWTGraphicsConfiguration config = (AWTGraphicsConfiguration)
      GraphicsConfigurationFactory.getFactory(AWTGraphicsDevice.class).chooseGraphicsConfiguration(capabilities,
                                                                                                   null,
                                                                                                   aScreen);
    if (config == null) {
      throw new NativeWindowException("Error: Couldn't fetch AWTGraphicsConfiguration");
    }

    return config;
  }

  // Disables the AWT's erasing of this Canvas's background on Windows
  // in Java SE 6. This internal API is not available in previous
  // releases, but the system property
  // -Dsun.awt.noerasebackground=true can be specified to get similar
  // results globally in previous releases.
  private static boolean disableBackgroundEraseInitialized;
  private static Method  disableBackgroundEraseMethod;
  private void disableBackgroundErase() {
    if (!disableBackgroundEraseInitialized) {
      try {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
              try {
                Class clazz = getToolkit().getClass();
                while (clazz != null && disableBackgroundEraseMethod == null) {
                  try {
                    disableBackgroundEraseMethod =
                      clazz.getDeclaredMethod("disableBackgroundErase",
                                              new Class[] { Canvas.class });
                    disableBackgroundEraseMethod.setAccessible(true);
                  } catch (Exception e) {
                    clazz = clazz.getSuperclass();
                  }
                }
              } catch (Exception e) {
              }
              return null;
            }
          });
      } catch (Exception e) {
      }
      disableBackgroundEraseInitialized = true;
    }
    if (disableBackgroundEraseMethod != null) {
      try {
        disableBackgroundEraseMethod.invoke(getToolkit(), new Object[] { this });
      } catch (Exception e) {
        // FIXME: workaround for 6504460 (incorrect backport of 6333613 in 5.0u10)
        // throw new GLException(e);
      }
    }
  }
}
