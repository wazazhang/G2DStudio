/* !---- DO NOT EDIT: This file autogenerated by com/sun/gluegen/procaddress/ProcAddressEmitter.java on Thu May 22 03:14:25 PDT 2008 ----! */

package net.java.games.joal;

import java.io.UnsupportedEncodingException;
import java.util.*;
import net.java.games.joal.*;
import net.java.games.joal.impl.*;
import com.sun.gluegen.runtime.*;

public interface ALC extends ALCConstants
{


  /** Entry point (through function pointer) to C language function: <br> <code> ALCboolean alcCaptureCloseDevice(ALCdevice *  device); </code>    */
  public boolean alcCaptureCloseDevice(ALCdevice device);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCdevice *  alcCaptureOpenDevice(const ALCchar *  devicename, ALCuint frequency, ALCenum format, ALCsizei buffersize); </code>    */
  public ALCdevice alcCaptureOpenDevice(java.nio.ByteBuffer devicename, int frequency, int format, int buffersize);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCdevice *  alcCaptureOpenDevice(const ALCchar *  devicename, ALCuint frequency, ALCenum format, ALCsizei buffersize); </code>    */
  public ALCdevice alcCaptureOpenDevice(byte[] devicename, int devicename_offset, int frequency, int format, int buffersize);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcCaptureSamples(ALCdevice *  device, ALCvoid *  buffer, ALCsizei samples); </code>    */
  public void alcCaptureSamples(ALCdevice device, java.nio.Buffer buffer, int samples);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcCaptureStart(ALCdevice *  device); </code>    */
  public void alcCaptureStart(ALCdevice device);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcCaptureStop(ALCdevice *  device); </code>    */
  public void alcCaptureStop(ALCdevice device);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCboolean alcCloseDevice(ALCdevice *  device); </code>    */
  public boolean alcCloseDevice(ALCdevice device);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCcontext *  alcCreateContext(ALCdevice *  device, const ALCint *  attrlist); </code>    */
  public ALCcontext alcCreateContext(ALCdevice device, java.nio.IntBuffer attrlist);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCcontext *  alcCreateContext(ALCdevice *  device, const ALCint *  attrlist); </code>    */
  public ALCcontext alcCreateContext(ALCdevice device, int[] attrlist, int attrlist_offset);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcDestroyContext(ALCcontext *  context); </code>    */
  public void alcDestroyContext(ALCcontext context);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCdevice *  alcGetContextsDevice(ALCcontext *  context); </code>    */
  public ALCdevice alcGetContextsDevice(ALCcontext context);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCcontext *  alcGetCurrentContext(ALCvoid); </code>    */
  public ALCcontext alcGetCurrentContext();

  /** Entry point (through function pointer) to C language function: <br> <code> ALCenum alcGetEnumValue(ALCdevice *  device, const ALCchar *  enumname); </code>    */
  public int alcGetEnumValue(ALCdevice device, java.nio.ByteBuffer enumname);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCenum alcGetEnumValue(ALCdevice *  device, const ALCchar *  enumname); </code>    */
  public int alcGetEnumValue(ALCdevice device, byte[] enumname, int enumname_offset);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCenum alcGetError(ALCdevice *  device); </code>    */
  public int alcGetError(ALCdevice device);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcGetIntegerv(ALCdevice *  device, ALCenum param, ALCsizei size, ALCint *  data); </code>    */
  public void alcGetIntegerv(ALCdevice device, int param, int size, java.nio.IntBuffer data);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcGetIntegerv(ALCdevice *  device, ALCenum param, ALCsizei size, ALCint *  data); </code>    */
  public void alcGetIntegerv(ALCdevice device, int param, int size, int[] data, int data_offset);

  /** Entry point (through function pointer) to C language function: <br> <code> const ALCchar *  alcGetString(ALCdevice *  device, ALCenum param); </code>    */
  public java.nio.ByteBuffer alcGetStringImpl(ALCdevice device, int param);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCboolean alcIsExtensionPresent(ALCdevice *  device, const ALCchar *  extname); </code>    */
  public boolean alcIsExtensionPresent(ALCdevice device, java.lang.String extname);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCboolean alcMakeContextCurrent(ALCcontext *  context); </code>    */
  public boolean alcMakeContextCurrent(ALCcontext context);

  /** Entry point (through function pointer) to C language function: <br> <code> ALCdevice *  alcOpenDevice(const ALCchar *  devicename); </code>    */
  public ALCdevice alcOpenDevice(java.lang.String devicename);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcProcessContext(ALCcontext *  context); </code>    */
  public void alcProcessContext(ALCcontext context);

  /** Entry point (through function pointer) to C language function: <br> <code> void alcSuspendContext(ALCcontext *  context); </code>    */
  public void alcSuspendContext(ALCcontext context);


  // --- Begin CustomJavaCode .cfg declarations
  /** Entry point (through function pointer) to C language function: <br> <code> const ALCchar *  alcGetString(ALCdevice *  device, ALCenum param); </code>    */
  public java.lang.String alcGetString(ALCdevice device, int param);
  
  /** Fetches the names of the available ALC device specifiers.
      Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER). */
  public java.lang.String[] alcGetDeviceSpecifiers();
  
  /** Fetches the names of the available ALC capture device specifiers.
      Equivalent to the C call alcGetString(NULL, ALC_CAPTURE_DEVICE_SPECIFIER). */
  public java.lang.String[] alcGetCaptureDeviceSpecifiers();
  // ---- End CustomJavaCode .cfg declarations

} // end of class ALC
