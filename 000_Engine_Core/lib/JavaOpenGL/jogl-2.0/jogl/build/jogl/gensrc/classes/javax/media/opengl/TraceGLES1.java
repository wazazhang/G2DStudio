package javax.media.opengl;

import java.io.*;
import javax.media.opengl.*;
import javax.media.opengl.GLBase;
import javax.media.opengl.GLES1;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES1;
import com.sun.gluegen.runtime.*;

/** <P> Composable pipeline which wraps an underlying {@link GL} implementation,
    providing tracing information to a user-specified {@link java.io.PrintStream}
    before and after each OpenGL method call. Sample code which installs this pipeline: </P>

<PRE>
     GL gl = drawable.setGL(new TraceGL(drawable.getGL(), System.err));
</PRE>
*/
public class TraceGLES1 implements javax.media.opengl.GLBase, javax.media.opengl.GL, javax.media.opengl.GL2ES1, javax.media.opengl.GLES1
{
  public static final boolean DEBUG = com.sun.opengl.impl.Debug.debug("TraceGLES1");
  public TraceGLES1(GLES1 downstreamGLES1, PrintStream stream)
  {
    if (downstreamGLES1 == null) {
      throw new IllegalArgumentException("null downstreamGLES1");
    }
    this.downstreamGLES1 = downstreamGLES1;
    this.stream = stream;
  }

  public boolean isGL() {
    return true;
  }
  public boolean isGL3bc() {
    return false;
  }
  public boolean isGL3() {
    return false;
  }
  public boolean isGL2() {
    return false;
  }
  public boolean isGLES1() {
    return true;
  }
  public boolean isGLES2() {
    return false;
  }
  public boolean isGL2ES1() {
    return true;
  }
  public boolean isGL2ES2() {
    return false;
  }
  public boolean isGL2GL3() {
    return false;
  }
  public boolean isGLES() {
    return isGLES2() || isGLES1();
  }
  public javax.media.opengl.GL getGL() {
    return this;
  }
  public javax.media.opengl.GL3bc getGL3bc() {
    throw new GLException("Not a GL3bc implementation");
  }
  public javax.media.opengl.GL3 getGL3() {
    throw new GLException("Not a GL3 implementation");
  }
  public javax.media.opengl.GL2 getGL2() {
    throw new GLException("Not a GL2 implementation");
  }
  public javax.media.opengl.GLES1 getGLES1() {
    return this;
  }
  public javax.media.opengl.GLES2 getGLES2() {
    throw new GLException("Not a GLES2 implementation");
  }
  public javax.media.opengl.GL2ES1 getGL2ES1() {
    return this;
  }
  public javax.media.opengl.GL2ES2 getGL2ES2() {
    throw new GLException("Not a GL2ES2 implementation");
  }
  public javax.media.opengl.GL2GL3 getGL2GL3() {
    throw new GLException("Not a GL2GL3 implementation");
  }
  public GLProfile getGLProfile() {
    return downstreamGLES1.getGLProfile();
  }
  public  void glLoadPaletteFromModelViewMatrixOES()
  {
    printIndent();
    print(    "glLoadPaletteFromModelViewMatrixOES("+")");
downstreamGLES1.glLoadPaletteFromModelViewMatrixOES();
    println("");
  }
  public  void glTexImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,java.nio.Buffer arg8)
  {
    printIndent();
    print(    "glTexImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<java.nio.Buffer> "+arg8+")");
downstreamGLES1.glTexImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  void glLoadMatrixf(java.nio.FloatBuffer arg0)
  {
    printIndent();
    print(    "glLoadMatrixf("+"<java.nio.FloatBuffer> "+arg0+")");
downstreamGLES1.glLoadMatrixf(arg0);
    println("");
  }
  public  void glGetFloatv(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetFloatv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetFloatv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexCoordPointer(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glTexCoordPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glTexCoordPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glBufferSubData(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glBufferSubData("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glBufferSubData(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glTexEnvf(int arg0,int arg1,float arg2)
  {
    printIndent();
    print(    "glTexEnvf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<float> "+arg2+")");
downstreamGLES1.glTexEnvf(arg0,arg1,arg2);
    println("");
  }
  public  void glGetMaterialfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glGetMaterialfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glGetMaterialfv(arg0,arg1,arg2);
    println("");
  }
  public  void glFlush()
  {
    printIndent();
    print(    "glFlush("+")");
downstreamGLES1.glFlush();
    println("");
  }
  public  void glColorPointer(int arg0,int arg1,int arg2,long arg3)
  {
    printIndent();
    print(    "glColorPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<long> "+arg3+")");
downstreamGLES1.glColorPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glTexEnvi(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexEnvi("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexEnvi(arg0,arg1,arg2);
    println("");
  }
  public  void glLightfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glLightfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glLightfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glCullFace(int arg0)
  {
    printIndent();
    print(    "glCullFace("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glCullFace(arg0);
    println("");
  }
  public  void glGetLightfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetLightfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetLightfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glLightfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glLightfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glLightfv(arg0,arg1,arg2);
    println("");
  }
  public  boolean glIsFramebuffer(int arg0)
  {
    printIndent();
    print(    "glIsFramebuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glIsFramebuffer(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glBlendEquationSeparate(int arg0,int arg1)
  {
    printIndent();
    print(    "glBlendEquationSeparate("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBlendEquationSeparate(arg0,arg1);
    println("");
  }
  public  void glGetLightfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glGetLightfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glGetLightfv(arg0,arg1,arg2);
    println("");
  }
  public  void glLineWidth(float arg0)
  {
    printIndent();
    print(    "glLineWidth("+"<float> "+arg0+")");
downstreamGLES1.glLineWidth(arg0);
    println("");
  }
  public  boolean isExtensionAvailable(java.lang.String arg0)
  {
        return downstreamGLES1.isExtensionAvailable(arg0);
  }
  public  void glEGLImageTargetTexture2DOES(int arg0,java.nio.Buffer arg1)
  {
    printIndent();
    print(    "glEGLImageTargetTexture2DOES("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.Buffer> "+arg1+")");
downstreamGLES1.glEGLImageTargetTexture2DOES(arg0,arg1);
    println("");
  }
  public  int getSwapInterval()
  {
        return downstreamGLES1.getSwapInterval();
  }
  public  void glDrawTexxOES(int arg0,int arg1,int arg2,int arg3,int arg4)
  {
    printIndent();
    print(    "glDrawTexxOES("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+")");
downstreamGLES1.glDrawTexxOES(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glLightModelx(int arg0,int arg1)
  {
    printIndent();
    print(    "glLightModelx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glLightModelx(arg0,arg1);
    println("");
  }
  public  void glStencilOp(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glStencilOp("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glStencilOp(arg0,arg1,arg2);
    println("");
  }
  public  void glTexGenx(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexGenx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexGenx(arg0,arg1,arg2);
    println("");
  }
  public  void glGetMaterialfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetMaterialfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetMaterialfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDeleteFramebuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glDeleteFramebuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glDeleteFramebuffers(arg0,arg1,arg2);
    println("");
  }
  public  void setSwapInterval(int arg0)
  {
    downstreamGLES1.setSwapInterval(arg0);
  }
  public  void glGetTexParameteriv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexParameteriv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glTexEnvxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexEnvxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexEnvxv(arg0,arg1,arg2);
    println("");
  }
  public  void glBlendFuncSeparate(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glBlendFuncSeparate("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glBlendFuncSeparate(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glBindBuffer(int arg0,int arg1)
  {
    printIndent();
    print(    "glBindBuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBindBuffer(arg0,arg1);
    println("");
  }
  public  void glGetClipPlanex(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetClipPlanex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetClipPlanex(arg0,arg1,arg2);
    println("");
  }
  public  void glLoadMatrixf(float[] arg0,int arg1)
  {
    printIndent();
    print(    "glLoadMatrixf("+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glLoadMatrixf(arg0,arg1);
    println("");
  }
  public  void glBlendEquation(int arg0)
  {
    printIndent();
    print(    "glBlendEquation("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glBlendEquation(arg0);
    println("");
  }
  public  void glGetFramebufferAttachmentParameteriv(int arg0,int arg1,int arg2,int[] arg3,int arg4)
  {
    printIndent();
    print(    "glGetFramebufferAttachmentParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+")");
downstreamGLES1.glGetFramebufferAttachmentParameteriv(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glMaterialf(int arg0,int arg1,float arg2)
  {
    printIndent();
    print(    "glMaterialf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<float> "+arg2+")");
downstreamGLES1.glMaterialf(arg0,arg1,arg2);
    println("");
  }
  public  void glGetFloatv(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glGetFloatv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glGetFloatv(arg0,arg1);
    println("");
  }
  public  void glNormalPointer(int arg0,int arg1,java.nio.Buffer arg2)
  {
    printIndent();
    print(    "glNormalPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.Buffer> "+arg2+")");
downstreamGLES1.glNormalPointer(arg0,arg1,arg2);
    println("");
  }
  public  void glLightModelfv(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glLightModelfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glLightModelfv(arg0,arg1);
    println("");
  }
  public  void glVertexPointer(javax.media.opengl.GLArrayData arg0)
  {
    printIndent();
    print(    "glVertexPointer("+"<javax.media.opengl.GLArrayData> "+arg0+")");
downstreamGLES1.glVertexPointer(arg0);
    println("");
  }
  public  void glReadPixels(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,java.nio.Buffer arg6)
  {
    printIndent();
    print(    "glReadPixels("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<java.nio.Buffer> "+arg6+")");
downstreamGLES1.glReadPixels(arg0,arg1,arg2,arg3,arg4,arg5,arg6);
    println("");
  }
  public  void glMaterialx(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glMaterialx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glMaterialx(arg0,arg1,arg2);
    println("");
  }
  public  void glFramebufferRenderbuffer(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glFramebufferRenderbuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glFramebufferRenderbuffer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glMultMatrixf(float[] arg0,int arg1)
  {
    printIndent();
    print(    "glMultMatrixf("+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glMultMatrixf(arg0,arg1);
    println("");
  }
  public  void glEnableClientState(int arg0)
  {
    printIndent();
    print(    "glEnableClientState("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glEnableClientState(arg0);
    println("");
  }
  public  void glGetTexParameterxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexParameterxv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexGeniv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexGeniv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexGeniv(arg0,arg1,arg2);
    println("");
  }
  public  void glFogfv(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glFogfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glFogfv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexParameteri(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexParameteri("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexParameteri(arg0,arg1,arg2);
    println("");
  }
  public  void glPointSizex(int arg0)
  {
    printIndent();
    print(    "glPointSizex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glPointSizex(arg0);
    println("");
  }
  public  void glDrawTexfvOES(java.nio.FloatBuffer arg0)
  {
    printIndent();
    print(    "glDrawTexfvOES("+"<java.nio.FloatBuffer> "+arg0+")");
downstreamGLES1.glDrawTexfvOES(arg0);
    println("");
  }
  public  void glClearStencil(int arg0)
  {
    printIndent();
    print(    "glClearStencil("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glClearStencil(arg0);
    println("");
  }
  public  void glCompressedTexImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,long arg7)
  {
    printIndent();
    print(    "glCompressedTexImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<long> "+arg7+")");
downstreamGLES1.glCompressedTexImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
    println("");
  }
  public  void glTexGenf(int arg0,int arg1,float arg2)
  {
    printIndent();
    print(    "glTexGenf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<float> "+arg2+")");
downstreamGLES1.glTexGenf(arg0,arg1,arg2);
    println("");
  }
  public  void glPointParameterfv(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glPointParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glPointParameterfv(arg0,arg1);
    println("");
  }
  public  void glTexGenfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexGenfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexGenfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glOrthox(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5)
  {
    printIndent();
    print(    "glOrthox("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+")");
downstreamGLES1.glOrthox(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  boolean glIsEnabled(int arg0)
  {
    printIndent();
    print(    "glIsEnabled("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glIsEnabled(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glTexGeni(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexGeni("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexGeni(arg0,arg1,arg2);
    println("");
  }
  public  void glDrawTexxvOES(java.nio.IntBuffer arg0)
  {
    printIndent();
    print(    "glDrawTexxvOES("+"<java.nio.IntBuffer> "+arg0+")");
downstreamGLES1.glDrawTexxvOES(arg0);
    println("");
  }
  public  void glTexSubImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,long arg8)
  {
    printIndent();
    print(    "glTexSubImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<long> "+arg8+")");
downstreamGLES1.glTexSubImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  void glTexParameterx(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexParameterx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexParameterx(arg0,arg1,arg2);
    println("");
  }
  public  boolean glIsTexture(int arg0)
  {
    printIndent();
    print(    "glIsTexture("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glIsTexture(arg0);
    println(" = "+_res);
    return _res;
  }
  public  boolean glIsVBOArrayEnabled()
  {
    printIndent();
    print(    "glIsVBOArrayEnabled("+")");
    boolean _res = downstreamGLES1.glIsVBOArrayEnabled();
    println(" = "+_res);
    return _res;
  }
  public  java.nio.ByteBuffer glMapBuffer(int arg0,int arg1)
  {
    printIndent();
    print(    "glMapBuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
    java.nio.ByteBuffer _res = downstreamGLES1.glMapBuffer(arg0,arg1);
    println(" = "+_res);
    return _res;
  }
  public  void glPointParameterfv(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glPointParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glPointParameterfv(arg0,arg1,arg2);
    println("");
  }
  public  void glFramebufferTexture2D(int arg0,int arg1,int arg2,int arg3,int arg4)
  {
    printIndent();
    print(    "glFramebufferTexture2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+")");
downstreamGLES1.glFramebufferTexture2D(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glTexEnviv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexEnviv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexEnviv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexGenfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glTexGenfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glTexGenfv(arg0,arg1,arg2);
    println("");
  }
  public  void glDrawElements(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glDrawElements("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glDrawElements(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glNormalPointer(javax.media.opengl.GLArrayData arg0)
  {
    printIndent();
    print(    "glNormalPointer("+"<javax.media.opengl.GLArrayData> "+arg0+")");
downstreamGLES1.glNormalPointer(arg0);
    println("");
  }
  public  void glTexEnvx(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTexEnvx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTexEnvx(arg0,arg1,arg2);
    println("");
  }
  public  void glDrawTexfvOES(float[] arg0,int arg1)
  {
    printIndent();
    print(    "glDrawTexfvOES("+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glDrawTexfvOES(arg0,arg1);
    println("");
  }
  public  void glFogfv(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glFogfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glFogfv(arg0,arg1);
    println("");
  }
  public  void glAlphaFuncx(int arg0,int arg1)
  {
    printIndent();
    print(    "glAlphaFuncx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glAlphaFuncx(arg0,arg1);
    println("");
  }
  public  void glActiveTexture(int arg0)
  {
    printIndent();
    print(    "glActiveTexture("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glActiveTexture(arg0);
    println("");
  }
  public  void glDeleteRenderbuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glDeleteRenderbuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glDeleteRenderbuffers(arg0,arg1,arg2);
    println("");
  }
  public  void glMultMatrixf(java.nio.FloatBuffer arg0)
  {
    printIndent();
    print(    "glMultMatrixf("+"<java.nio.FloatBuffer> "+arg0+")");
downstreamGLES1.glMultMatrixf(arg0);
    println("");
  }
  public  void glLoadIdentity()
  {
    printIndent();
    print(    "glLoadIdentity("+")");
downstreamGLES1.glLoadIdentity();
    println("");
  }
  public  void glLightModelfv(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glLightModelfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glLightModelfv(arg0,arg1,arg2);
    println("");
  }
  public  void glDrawTexiOES(int arg0,int arg1,int arg2,int arg3,int arg4)
  {
    printIndent();
    print(    "glDrawTexiOES("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+")");
downstreamGLES1.glDrawTexiOES(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glDepthRangef(float arg0,float arg1)
  {
    printIndent();
    print(    "glDepthRangef("+"<float> "+arg0+", "+"<float> "+arg1+")");
downstreamGLES1.glDepthRangef(arg0,arg1);
    println("");
  }
  public  void glClipPlanef(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glClipPlanef("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glClipPlanef(arg0,arg1);
    println("");
  }
  public  void glDrawTexfOES(float arg0,float arg1,float arg2,float arg3,float arg4)
  {
    printIndent();
    print(    "glDrawTexfOES("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+", "+"<float> "+arg4+")");
downstreamGLES1.glDrawTexfOES(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glLineWidthx(int arg0)
  {
    printIndent();
    print(    "glLineWidthx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glLineWidthx(arg0);
    println("");
  }
  public  void glGetTexParameterxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexParameterxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glOrtho(double arg0,double arg1,double arg2,double arg3,double arg4,double arg5)
  {
    printIndent();
    print(    "glOrtho("+"<double> "+arg0+", "+"<double> "+arg1+", "+"<double> "+arg2+", "+"<double> "+arg3+", "+"<double> "+arg4+", "+"<double> "+arg5+")");
downstreamGLES1.glOrtho(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  void glGetTexGeniv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexGeniv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexGeniv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glAlphaFunc(int arg0,float arg1)
  {
    printIndent();
    print(    "glAlphaFunc("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<float> "+arg1+")");
downstreamGLES1.glAlphaFunc(arg0,arg1);
    println("");
  }
  public  void glMatrixMode(int arg0)
  {
    printIndent();
    print(    "glMatrixMode("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glMatrixMode(arg0);
    println("");
  }
  public  void glClearColor(float arg0,float arg1,float arg2,float arg3)
  {
    printIndent();
    print(    "glClearColor("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+")");
downstreamGLES1.glClearColor(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glTranslatex(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glTranslatex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glTranslatex(arg0,arg1,arg2);
    println("");
  }
  public  void glGenerateMipmap(int arg0)
  {
    printIndent();
    print(    "glGenerateMipmap("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glGenerateMipmap(arg0);
    println("");
  }
  public  void glDrawTexxvOES(int[] arg0,int arg1)
  {
    printIndent();
    print(    "glDrawTexxvOES("+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glDrawTexxvOES(arg0,arg1);
    println("");
  }
  public  void glCompressedTexSubImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,java.nio.Buffer arg8)
  {
    printIndent();
    print(    "glCompressedTexSubImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<java.nio.Buffer> "+arg8+")");
downstreamGLES1.glCompressedTexSubImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  void glWeightPointer(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glWeightPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glWeightPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glEnable(int arg0)
  {
    printIndent();
    print(    "glEnable("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glEnable(arg0);
    println("");
  }
  public  void glDepthRangex(int arg0,int arg1)
  {
    printIndent();
    print(    "glDepthRangex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glDepthRangex(arg0,arg1);
    println("");
  }
  public  void glTexEnviv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexEnviv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexEnviv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glVertexPointer(int arg0,int arg1,int arg2,long arg3)
  {
    printIndent();
    print(    "glVertexPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<long> "+arg3+")");
downstreamGLES1.glVertexPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  boolean hasGLSL()
  {
        return downstreamGLES1.hasGLSL();
  }
  public  void glPointParameterx(int arg0,int arg1)
  {
    printIndent();
    print(    "glPointParameterx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glPointParameterx(arg0,arg1);
    println("");
  }
  public  void glColor4f(float arg0,float arg1,float arg2,float arg3)
  {
    printIndent();
    print(    "glColor4f("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+")");
downstreamGLES1.glColor4f(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDeleteRenderbuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glDeleteRenderbuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glDeleteRenderbuffers(arg0,arg1);
    println("");
  }
  public  void glTranslatef(float arg0,float arg1,float arg2)
  {
    printIndent();
    print(    "glTranslatef("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+")");
downstreamGLES1.glTranslatef(arg0,arg1,arg2);
    println("");
  }
  public  void glTexParameterf(int arg0,int arg1,float arg2)
  {
    printIndent();
    print(    "glTexParameterf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<float> "+arg2+")");
downstreamGLES1.glTexParameterf(arg0,arg1,arg2);
    println("");
  }
  public  void glPointParameterf(int arg0,float arg1)
  {
    printIndent();
    print(    "glPointParameterf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<float> "+arg1+")");
downstreamGLES1.glPointParameterf(arg0,arg1);
    println("");
  }
  public  boolean glIsVBOElementEnabled()
  {
    printIndent();
    print(    "glIsVBOElementEnabled("+")");
    boolean _res = downstreamGLES1.glIsVBOElementEnabled();
    println(" = "+_res);
    return _res;
  }
  public  void glClipPlanef(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glClipPlanef("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glClipPlanef(arg0,arg1,arg2);
    println("");
  }
  public  void glBindTexture(int arg0,int arg1)
  {
    printIndent();
    print(    "glBindTexture("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBindTexture(arg0,arg1);
    println("");
  }
  public  void glColorMask(boolean arg0,boolean arg1,boolean arg2,boolean arg3)
  {
    printIndent();
    print(    "glColorMask("+"<boolean> "+arg0+", "+"<boolean> "+arg1+", "+"<boolean> "+arg2+", "+"<boolean> "+arg3+")");
downstreamGLES1.glColorMask(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glBlendFunc(int arg0,int arg1)
  {
    printIndent();
    print(    "glBlendFunc("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBlendFunc(arg0,arg1);
    println("");
  }
  public  void glDrawTexsvOES(short[] arg0,int arg1)
  {
    printIndent();
    print(    "glDrawTexsvOES("+"<[S>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glDrawTexsvOES(arg0,arg1);
    println("");
  }
  public  boolean glUnmapBuffer(int arg0)
  {
    printIndent();
    print(    "glUnmapBuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glUnmapBuffer(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glBindFramebuffer(int arg0,int arg1)
  {
    printIndent();
    print(    "glBindFramebuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBindFramebuffer(arg0,arg1);
    println("");
  }
  public  void glColor4x(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glColor4x("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glColor4x(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDeleteFramebuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glDeleteFramebuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glDeleteFramebuffers(arg0,arg1);
    println("");
  }
  public  void glDrawTexsOES(short arg0,short arg1,short arg2,short arg3,short arg4)
  {
    printIndent();
    print(    "glDrawTexsOES("+"<short> "+arg0+", "+"<short> "+arg1+", "+"<short> "+arg2+", "+"<short> "+arg3+", "+"<short> "+arg4+")");
downstreamGLES1.glDrawTexsOES(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glClientActiveTexture(int arg0)
  {
    printIndent();
    print(    "glClientActiveTexture("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glClientActiveTexture(arg0);
    println("");
  }
  public  void glDepthRange(double arg0,double arg1)
  {
    printIndent();
    print(    "glDepthRange("+"<double> "+arg0+", "+"<double> "+arg1+")");
downstreamGLES1.glDepthRange(arg0,arg1);
    println("");
  }
  public  void glCurrentPaletteMatrix(int arg0)
  {
    printIndent();
    print(    "glCurrentPaletteMatrix("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glCurrentPaletteMatrix(arg0);
    println("");
  }
  public  void glGetFramebufferAttachmentParameteriv(int arg0,int arg1,int arg2,java.nio.IntBuffer arg3)
  {
    printIndent();
    print(    "glGetFramebufferAttachmentParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg3+")");
downstreamGLES1.glGetFramebufferAttachmentParameteriv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGetClipPlanex(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGetClipPlanex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGetClipPlanex(arg0,arg1);
    println("");
  }
  public  void glColorPointer(javax.media.opengl.GLArrayData arg0)
  {
    printIndent();
    print(    "glColorPointer("+"<javax.media.opengl.GLArrayData> "+arg0+")");
downstreamGLES1.glColorPointer(arg0);
    println("");
  }
  public  void glGetTexParameteriv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexParameteriv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexEnvxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexEnvxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexEnvxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glCopyTexImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7)
  {
    printIndent();
    print(    "glCopyTexImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+")");
downstreamGLES1.glCopyTexImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
    println("");
  }
  public  boolean glIsRenderbuffer(int arg0)
  {
    printIndent();
    print(    "glIsRenderbuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glIsRenderbuffer(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glTexImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,long arg8)
  {
    printIndent();
    print(    "glTexImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<long> "+arg8+")");
downstreamGLES1.glTexImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  void glTexParameteriv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexParameteriv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glMaterialxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glMaterialxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glMaterialxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGetClipPlanef(int arg0,float[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetClipPlanef("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetClipPlanef(arg0,arg1,arg2);
    println("");
  }
  public  void glTexCoordPointer(int arg0,int arg1,int arg2,long arg3)
  {
    printIndent();
    print(    "glTexCoordPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<long> "+arg3+")");
downstreamGLES1.glTexCoordPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDisableClientState(int arg0)
  {
    printIndent();
    print(    "glDisableClientState("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glDisableClientState(arg0);
    println("");
  }
  public  void glTexGenxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexGenxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexGenxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDeleteBuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glDeleteBuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glDeleteBuffers(arg0,arg1);
    println("");
  }
  public  void glClear(int arg0)
  {
    printIndent();
    print(    "glClear("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glClear(arg0);
    println("");
  }
  public  void glGetTexEnvxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexEnvxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexEnvxv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexParameterfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glTexParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glTexParameterfv(arg0,arg1,arg2);
    println("");
  }
  public  void glPolygonOffsetx(int arg0,int arg1)
  {
    printIndent();
    print(    "glPolygonOffsetx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glPolygonOffsetx(arg0,arg1);
    println("");
  }
  public  void glTexParameterxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexParameterxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glColorPointer(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glColorPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glColorPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  boolean glIsBuffer(int arg0)
  {
    printIndent();
    print(    "glIsBuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    boolean _res = downstreamGLES1.glIsBuffer(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glCopyTexSubImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7)
  {
    printIndent();
    print(    "glCopyTexSubImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+")");
downstreamGLES1.glCopyTexSubImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
    println("");
  }
  public  void glGenBuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGenBuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGenBuffers(arg0,arg1,arg2);
    println("");
  }
  public  void glTexGeniv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexGeniv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexGeniv(arg0,arg1,arg2);
    println("");
  }
  public  void glGenRenderbuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGenRenderbuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGenRenderbuffers(arg0,arg1,arg2);
    println("");
  }
  public  void glEGLImageTargetRenderbufferStorageOES(int arg0,java.nio.Buffer arg1)
  {
    printIndent();
    print(    "glEGLImageTargetRenderbufferStorageOES("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.Buffer> "+arg1+")");
downstreamGLES1.glEGLImageTargetRenderbufferStorageOES(arg0,arg1);
    println("");
  }
  public  void glLoadMatrixx(int[] arg0,int arg1)
  {
    printIndent();
    print(    "glLoadMatrixx("+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glLoadMatrixx(arg0,arg1);
    println("");
  }
  public  void glNormal3x(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glNormal3x("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glNormal3x(arg0,arg1,arg2);
    println("");
  }
  public  void glGetIntegerv(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetIntegerv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetIntegerv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexParameterfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexParameterfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  boolean isFunctionAvailable(java.lang.String arg0)
  {
        return downstreamGLES1.isFunctionAvailable(arg0);
  }
  public  void glDrawArrays(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glDrawArrays("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glDrawArrays(arg0,arg1,arg2);
    println("");
  }
  public  java.lang.Object getExtension(java.lang.String arg0)
  {
        return downstreamGLES1.getExtension(arg0);
  }
  public  void glClipPlanex(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glClipPlanex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glClipPlanex(arg0,arg1);
    println("");
  }
  public  void glStencilMask(int arg0)
  {
    printIndent();
    print(    "glStencilMask("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glStencilMask(arg0);
    println("");
  }
  public  void glGenFramebuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGenFramebuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGenFramebuffers(arg0,arg1,arg2);
    println("");
  }
  public  void glFogxv(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glFogxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glFogxv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetBufferParameteriv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetBufferParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetBufferParameteriv(arg0,arg1,arg2);
    println("");
  }
  public  void glNormalPointer(int arg0,int arg1,long arg2)
  {
    printIndent();
    print(    "glNormalPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<long> "+arg2+")");
downstreamGLES1.glNormalPointer(arg0,arg1,arg2);
    println("");
  }
  public  void glGetClipPlanef(int arg0,java.nio.FloatBuffer arg1)
  {
    printIndent();
    print(    "glGetClipPlanef("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg1+")");
downstreamGLES1.glGetClipPlanef(arg0,arg1);
    println("");
  }
  public  void glDeleteTextures(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glDeleteTextures("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glDeleteTextures(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexGenxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexGenxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexGenxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glSampleCoverage(float arg0,boolean arg1)
  {
    printIndent();
    print(    "glSampleCoverage("+"<float> "+arg0+", "+"<boolean> "+arg1+")");
downstreamGLES1.glSampleCoverage(arg0,arg1);
    println("");
  }
  public  void glPushMatrix()
  {
    printIndent();
    print(    "glPushMatrix("+")");
downstreamGLES1.glPushMatrix();
    println("");
  }
  public  void glReadPixels(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,long arg6)
  {
    printIndent();
    print(    "glReadPixels("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<long> "+arg6+")");
downstreamGLES1.glReadPixels(arg0,arg1,arg2,arg3,arg4,arg5,arg6);
    println("");
  }
  public  void glRotatef(float arg0,float arg1,float arg2,float arg3)
  {
    printIndent();
    print(    "glRotatef("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+")");
downstreamGLES1.glRotatef(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glPolygonOffset(float arg0,float arg1)
  {
    printIndent();
    print(    "glPolygonOffset("+"<float> "+arg0+", "+"<float> "+arg1+")");
downstreamGLES1.glPolygonOffset(arg0,arg1);
    println("");
  }
  public  void glLightModelxv(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glLightModelxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glLightModelxv(arg0,arg1,arg2);
    println("");
  }
  public  void glNormal3f(float arg0,float arg1,float arg2)
  {
    printIndent();
    print(    "glNormal3f("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+")");
downstreamGLES1.glNormal3f(arg0,arg1,arg2);
    println("");
  }
  public  void glGetBooleanv(int arg0,java.nio.ByteBuffer arg1)
  {
    printIndent();
    print(    "glGetBooleanv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.ByteBuffer> "+arg1+")");
downstreamGLES1.glGetBooleanv(arg0,arg1);
    println("");
  }
  public  void glGetMaterialxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetMaterialxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetMaterialxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  java.lang.Object getPlatformGLExtensions()
  {
        return downstreamGLES1.getPlatformGLExtensions();
  }
  public  void glPointParameterxv(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glPointParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glPointParameterxv(arg0,arg1,arg2);
    println("");
  }
  public  void glCompressedTexImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,java.nio.Buffer arg7)
  {
    printIndent();
    print(    "glCompressedTexImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<java.nio.Buffer> "+arg7+")");
downstreamGLES1.glCompressedTexImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
    println("");
  }
  public  void glViewport(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glViewport("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glViewport(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGetFixedv(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGetFixedv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGetFixedv(arg0,arg1);
    println("");
  }
  public  void glGenTextures(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGenTextures("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGenTextures(arg0,arg1,arg2);
    println("");
  }
  public  void glMultMatrixx(int[] arg0,int arg1)
  {
    printIndent();
    print(    "glMultMatrixx("+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glMultMatrixx(arg0,arg1);
    println("");
  }
  public  void glRotatex(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glRotatex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glRotatex(arg0,arg1,arg2,arg3);
    println("");
  }
  public  int glGetBoundBuffer(int arg0)
  {
    printIndent();
    print(    "glGetBoundBuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    int _res = downstreamGLES1.glGetBoundBuffer(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glBindRenderbuffer(int arg0,int arg1)
  {
    printIndent();
    print(    "glBindRenderbuffer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glBindRenderbuffer(arg0,arg1);
    println("");
  }
  public  void glTexSubImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,java.nio.Buffer arg8)
  {
    printIndent();
    print(    "glTexSubImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<java.nio.Buffer> "+arg8+")");
downstreamGLES1.glTexSubImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  void glDrawTexsvOES(java.nio.ShortBuffer arg0)
  {
    printIndent();
    print(    "glDrawTexsvOES("+"<java.nio.ShortBuffer> "+arg0+")");
downstreamGLES1.glDrawTexsvOES(arg0);
    println("");
  }
  public  void glGetLightxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetLightxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetLightxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDepthMask(boolean arg0)
  {
    printIndent();
    print(    "glDepthMask("+"<boolean> "+arg0+")");
downstreamGLES1.glDepthMask(arg0);
    println("");
  }
  public  void glClearDepthf(float arg0)
  {
    printIndent();
    print(    "glClearDepthf("+"<float> "+arg0+")");
downstreamGLES1.glClearDepthf(arg0);
    println("");
  }
  public  void glDrawElements(int arg0,int arg1,int arg2,long arg3)
  {
    printIndent();
    print(    "glDrawElements("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<long> "+arg3+")");
downstreamGLES1.glDrawElements(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glSampleCoveragex(int arg0,boolean arg1)
  {
    printIndent();
    print(    "glSampleCoveragex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<boolean> "+arg1+")");
downstreamGLES1.glSampleCoveragex(arg0,arg1);
    println("");
  }
  public  void glDisable(int arg0)
  {
    printIndent();
    print(    "glDisable("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glDisable(arg0);
    println("");
  }
  public  void glGetTexEnviv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexEnviv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexEnviv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glLightxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glLightxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glLightxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glColor4ub(byte arg0,byte arg1,byte arg2,byte arg3)
  {
    printIndent();
    print(    "glColor4ub("+"<byte> "+arg0+", "+"<byte> "+arg1+", "+"<byte> "+arg2+", "+"<byte> "+arg3+")");
downstreamGLES1.glColor4ub(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glClearDepthx(int arg0)
  {
    printIndent();
    print(    "glClearDepthx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glClearDepthx(arg0);
    println("");
  }
  public  void glDrawTexivOES(int[] arg0,int arg1)
  {
    printIndent();
    print(    "glDrawTexivOES("+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glDrawTexivOES(arg0,arg1);
    println("");
  }
  public  void glPopMatrix()
  {
    printIndent();
    print(    "glPopMatrix("+")");
downstreamGLES1.glPopMatrix();
    println("");
  }
  public  void glGetRenderbufferParameteriv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetRenderbufferParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetRenderbufferParameteriv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glMatrixIndexPointer(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glMatrixIndexPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glMatrixIndexPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glPointSizePointerOES(int arg0,int arg1,java.nio.Buffer arg2)
  {
    printIndent();
    print(    "glPointSizePointerOES("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.Buffer> "+arg2+")");
downstreamGLES1.glPointSizePointerOES(arg0,arg1,arg2);
    println("");
  }
  public  void glLightModelxv(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glLightModelxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glLightModelxv(arg0,arg1);
    println("");
  }
  public  void glLightx(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glLightx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glLightx(arg0,arg1,arg2);
    println("");
  }
  public  void glRenderbufferStorage(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glRenderbufferStorage("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glRenderbufferStorage(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glPointSize(float arg0)
  {
    printIndent();
    print(    "glPointSize("+"<float> "+arg0+")");
downstreamGLES1.glPointSize(arg0);
    println("");
  }
  public  void glGetTexGenxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexGenxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexGenxv(arg0,arg1,arg2);
    println("");
  }
  public  void glLogicOp(int arg0)
  {
    printIndent();
    print(    "glLogicOp("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glLogicOp(arg0);
    println("");
  }
  public  void glGetTexParameterfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glGetTexParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glGetTexParameterfv(arg0,arg1,arg2);
    println("");
  }
  public  void glMaterialfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glMaterialfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glMaterialfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glMultiTexCoord4x(int arg0,int arg1,int arg2,int arg3,int arg4)
  {
    printIndent();
    print(    "glMultiTexCoord4x("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+")");
downstreamGLES1.glMultiTexCoord4x(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glClearColorx(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glClearColorx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glClearColorx(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGenTextures(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGenTextures("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGenTextures(arg0,arg1);
    println("");
  }
  public  void glMultMatrixx(java.nio.IntBuffer arg0)
  {
    printIndent();
    print(    "glMultMatrixx("+"<java.nio.IntBuffer> "+arg0+")");
downstreamGLES1.glMultMatrixx(arg0);
    println("");
  }
  public  void glClearDepth(double arg0)
  {
    printIndent();
    print(    "glClearDepth("+"<double> "+arg0+")");
downstreamGLES1.glClearDepth(arg0);
    println("");
  }
  public  void glFrontFace(int arg0)
  {
    printIndent();
    print(    "glFrontFace("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glFrontFace(arg0);
    println("");
  }
  public  void glFinish()
  {
    printIndent();
    print(    "glFinish("+")");
downstreamGLES1.glFinish();
    println("");
  }
  public  void glPointParameterxv(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glPointParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glPointParameterxv(arg0,arg1);
    println("");
  }
  public  void glGetMaterialxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetMaterialxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetMaterialxv(arg0,arg1,arg2);
    println("");
  }
  public  void glCompressedTexSubImage2D(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5,int arg6,int arg7,long arg8)
  {
    printIndent();
    print(    "glCompressedTexSubImage2D("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg6).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg7).toUpperCase()+", "+"<long> "+arg8+")");
downstreamGLES1.glCompressedTexSubImage2D(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
    println("");
  }
  public  java.lang.String glGetString(int arg0)
  {
    printIndent();
    print(    "glGetString("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    java.lang.String _res = downstreamGLES1.glGetString(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glGetFixedv(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetFixedv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetFixedv(arg0,arg1,arg2);
    println("");
  }
  public  void glHint(int arg0,int arg1)
  {
    printIndent();
    print(    "glHint("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glHint(arg0,arg1);
    println("");
  }
  public  void glVertexPointer(int arg0,int arg1,int arg2,java.nio.Buffer arg3)
  {
    printIndent();
    print(    "glVertexPointer("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<java.nio.Buffer> "+arg3+")");
downstreamGLES1.glVertexPointer(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGetLightxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetLightxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetLightxv(arg0,arg1,arg2);
    println("");
  }
  public  void glShadeModel(int arg0)
  {
    printIndent();
    print(    "glShadeModel("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glShadeModel(arg0);
    println("");
  }
  public  void glGetRenderbufferParameteriv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetRenderbufferParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetRenderbufferParameteriv(arg0,arg1,arg2);
    println("");
  }
  public  void glOrthof(float arg0,float arg1,float arg2,float arg3,float arg4,float arg5)
  {
    printIndent();
    print(    "glOrthof("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+", "+"<float> "+arg4+", "+"<float> "+arg5+")");
downstreamGLES1.glOrthof(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  int glQueryMatrixxOES(int[] arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glQueryMatrixxOES("+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
    int _res = downstreamGLES1.glQueryMatrixxOES(arg0,arg1,arg2,arg3);
    println(" = "+_res);
    return _res;
  }
  public  int glQueryMatrixxOES(java.nio.IntBuffer arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glQueryMatrixxOES("+"<java.nio.IntBuffer> "+arg0+", "+"<java.nio.IntBuffer> "+arg1+")");
    int _res = downstreamGLES1.glQueryMatrixxOES(arg0,arg1);
    println(" = "+_res);
    return _res;
  }
  public  void glMaterialfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glMaterialfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glMaterialfv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexParameterfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexParameterfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexParameterfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glLightxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glLightxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glLightxv(arg0,arg1,arg2);
    println("");
  }
  public  void glFrustum(double arg0,double arg1,double arg2,double arg3,double arg4,double arg5)
  {
    printIndent();
    print(    "glFrustum("+"<double> "+arg0+", "+"<double> "+arg1+", "+"<double> "+arg2+", "+"<double> "+arg3+", "+"<double> "+arg4+", "+"<double> "+arg5+")");
downstreamGLES1.glFrustum(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  void glGetTexEnviv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glGetTexEnviv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glGetTexEnviv(arg0,arg1,arg2);
    println("");
  }
  public  void glDrawTexivOES(java.nio.IntBuffer arg0)
  {
    printIndent();
    print(    "glDrawTexivOES("+"<java.nio.IntBuffer> "+arg0+")");
downstreamGLES1.glDrawTexivOES(arg0);
    println("");
  }
  public  void glLightf(int arg0,int arg1,float arg2)
  {
    printIndent();
    print(    "glLightf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<float> "+arg2+")");
downstreamGLES1.glLightf(arg0,arg1,arg2);
    println("");
  }
  public  int glCheckFramebufferStatus(int arg0)
  {
    printIndent();
    print(    "glCheckFramebufferStatus("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
    int _res = downstreamGLES1.glCheckFramebufferStatus(arg0);
    println(" = "+_res);
    return _res;
  }
  public  void glTexGenxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexGenxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexGenxv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexGenfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexGenfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexGenfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGetTexEnvfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexEnvfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexEnvfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glScissor(int arg0,int arg1,int arg2,int arg3)
  {
    printIndent();
    print(    "glScissor("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glScissor(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glFrustumx(int arg0,int arg1,int arg2,int arg3,int arg4,int arg5)
  {
    printIndent();
    print(    "glFrustumx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg4).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg5).toUpperCase()+")");
downstreamGLES1.glFrustumx(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  void glDeleteBuffers(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glDeleteBuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glDeleteBuffers(arg0,arg1,arg2);
    println("");
  }
  public  void glMaterialxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glMaterialxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glMaterialxv(arg0,arg1,arg2);
    println("");
  }
  public  void glPixelStorei(int arg0,int arg1)
  {
    printIndent();
    print(    "glPixelStorei("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glPixelStorei(arg0,arg1);
    println("");
  }
  public  void glTexParameteriv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexParameteriv(arg0,arg1,arg2);
    println("");
  }
  public  void glTexEnvfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glTexEnvfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glTexEnvfv(arg0,arg1,arg2);
    println("");
  }
  public  void glLightModelf(int arg0,float arg1)
  {
    printIndent();
    print(    "glLightModelf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<float> "+arg1+")");
downstreamGLES1.glLightModelf(arg0,arg1);
    println("");
  }
  public  void glTexGeniv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexGeniv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexGeniv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGenRenderbuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGenRenderbuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGenRenderbuffers(arg0,arg1);
    println("");
  }
  public  void glFrustumf(float arg0,float arg1,float arg2,float arg3,float arg4,float arg5)
  {
    printIndent();
    print(    "glFrustumf("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+", "+"<float> "+arg4+", "+"<float> "+arg5+")");
downstreamGLES1.glFrustumf(arg0,arg1,arg2,arg3,arg4,arg5);
    println("");
  }
  public  int glGetError()
  {
    printIndent();
    print(    "glGetError("+")");
    int _res = downstreamGLES1.glGetError();
    println(" = "+_res);
    return _res;
  }
  public  void glStencilFunc(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glStencilFunc("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glStencilFunc(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexEnvxv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetTexEnvxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetTexEnvxv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glTexParameterxv(int arg0,int arg1,java.nio.IntBuffer arg2)
  {
    printIndent();
    print(    "glTexParameterxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg2+")");
downstreamGLES1.glTexParameterxv(arg0,arg1,arg2);
    println("");
  }
  public  void glGenBuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGenBuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGenBuffers(arg0,arg1);
    println("");
  }
  public  javax.media.opengl.GLContext getContext()
  {
        return downstreamGLES1.getContext();
  }
  public  void glGetBooleanv(int arg0,byte[] arg1,int arg2)
  {
    printIndent();
    print(    "glGetBooleanv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[B>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glGetBooleanv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetIntegerv(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGetIntegerv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGetIntegerv(arg0,arg1);
    println("");
  }
  public  void glDepthFunc(int arg0)
  {
    printIndent();
    print(    "glDepthFunc("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+")");
downstreamGLES1.glDepthFunc(arg0);
    println("");
  }
  public  void glTexEnvfv(int arg0,int arg1,float[] arg2,int arg3)
  {
    printIndent();
    print(    "glTexEnvfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[F>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glTexEnvfv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glLoadMatrixx(java.nio.IntBuffer arg0)
  {
    printIndent();
    print(    "glLoadMatrixx("+"<java.nio.IntBuffer> "+arg0+")");
downstreamGLES1.glLoadMatrixx(arg0);
    println("");
  }
  public  void glTexCoordPointer(javax.media.opengl.GLArrayData arg0)
  {
    printIndent();
    print(    "glTexCoordPointer("+"<javax.media.opengl.GLArrayData> "+arg0+")");
downstreamGLES1.glTexCoordPointer(arg0);
    println("");
  }
  public  void glScalex(int arg0,int arg1,int arg2)
  {
    printIndent();
    print(    "glScalex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glScalex(arg0,arg1,arg2);
    println("");
  }
  public  void glFogx(int arg0,int arg1)
  {
    printIndent();
    print(    "glFogx("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+")");
downstreamGLES1.glFogx(arg0,arg1);
    println("");
  }
  public  void glFogf(int arg0,float arg1)
  {
    printIndent();
    print(    "glFogf("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<float> "+arg1+")");
downstreamGLES1.glFogf(arg0,arg1);
    println("");
  }
  public  void glScalef(float arg0,float arg1,float arg2)
  {
    printIndent();
    print(    "glScalef("+"<float> "+arg0+", "+"<float> "+arg1+", "+"<float> "+arg2+")");
downstreamGLES1.glScalef(arg0,arg1,arg2);
    println("");
  }
  public  void glFogxv(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glFogxv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glFogxv(arg0,arg1);
    println("");
  }
  public  void glGetBufferParameteriv(int arg0,int arg1,int[] arg2,int arg3)
  {
    printIndent();
    print(    "glGetBufferParameteriv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glGetBufferParameteriv(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glGenFramebuffers(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glGenFramebuffers("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glGenFramebuffers(arg0,arg1);
    println("");
  }
  public  void glBufferData(int arg0,int arg1,java.nio.Buffer arg2,int arg3)
  {
    printIndent();
    print(    "glBufferData("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.Buffer> "+arg2+", "+"<int> 0x"+Integer.toHexString(arg3).toUpperCase()+")");
downstreamGLES1.glBufferData(arg0,arg1,arg2,arg3);
    println("");
  }
  public  void glDeleteTextures(int arg0,java.nio.IntBuffer arg1)
  {
    printIndent();
    print(    "glDeleteTextures("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<java.nio.IntBuffer> "+arg1+")");
downstreamGLES1.glDeleteTextures(arg0,arg1);
    println("");
  }
  public  void glClipPlanex(int arg0,int[] arg1,int arg2)
  {
    printIndent();
    print(    "glClipPlanex("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<[I>"+", "+"<int> 0x"+Integer.toHexString(arg2).toUpperCase()+")");
downstreamGLES1.glClipPlanex(arg0,arg1,arg2);
    println("");
  }
  public  void glMultiTexCoord4f(int arg0,float arg1,float arg2,float arg3,float arg4)
  {
    printIndent();
    print(    "glMultiTexCoord4f("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<float> "+arg1+", "+"<float> "+arg2+", "+"<float> "+arg3+", "+"<float> "+arg4+")");
downstreamGLES1.glMultiTexCoord4f(arg0,arg1,arg2,arg3,arg4);
    println("");
  }
  public  void glGetTexGenfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glGetTexGenfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glGetTexGenfv(arg0,arg1,arg2);
    println("");
  }
  public  void glGetTexEnvfv(int arg0,int arg1,java.nio.FloatBuffer arg2)
  {
    printIndent();
    print(    "glGetTexEnvfv("+"<int> 0x"+Integer.toHexString(arg0).toUpperCase()+", "+"<int> 0x"+Integer.toHexString(arg1).toUpperCase()+", "+"<java.nio.FloatBuffer> "+arg2+")");
downstreamGLES1.glGetTexEnvfv(arg0,arg1,arg2);
    println("");
  }
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("TraceGLES1 [ implementing javax.media.opengl.GLES1,\n\t");
    sb.append(" downstream: "+downstreamGLES1.toString()+"\n\t]");
    return sb.toString();
  }
private PrintStream stream;
private int indent = 0;
protected String dumpArray(Object obj)
{
  if (obj == null) return "[null]";
  StringBuffer sb = new StringBuffer("[");
  int len  = java.lang.reflect.Array.getLength(obj);
  int count = Math.min(len,16);
  for ( int i =0; i < count; i++ ) {
    sb.append(java.lang.reflect.Array.get(obj,i));
    if (i < count-1)
      sb.append(',');
  }
  if ( len > 16 )
    sb.append("...").append(len);
  sb.append(']');
  return sb.toString();
}
protected void print(String str)
{
  stream.print(str);
}
protected void println(String str)
{
  stream.println(str);
}
protected void printIndent()
{
  for( int i =0; i < indent; i++) {stream.print(' ');}
}

  private GLES1 downstreamGLES1;
} // end class TraceGLES1
