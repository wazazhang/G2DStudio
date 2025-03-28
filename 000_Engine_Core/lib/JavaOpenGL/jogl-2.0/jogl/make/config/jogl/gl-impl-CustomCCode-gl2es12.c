/*   Java->C glue code:
 *   Java package: com.sun.opengl.impl.gl2es12.GL2ES12Impl
 *    Java method: long dispatch_glMapBuffer(int target, int access)
 *     C function: void * glMapBuffer(GLenum target, GLenum access);
 */
JNIEXPORT jlong JNICALL 
Java_com_sun_opengl_impl_gl2es12_GL2ES12Impl_dispatch_1glMapBuffer(JNIEnv *env, jobject _unused, jint target, jint access, jlong glProcAddress) {
  PFNGLMAPBUFFERPROC ptr_glMapBuffer;
  void * _res;
  ptr_glMapBuffer = (PFNGLMAPBUFFERPROC) (intptr_t) glProcAddress;
  assert(ptr_glMapBuffer != NULL);
  _res = (* ptr_glMapBuffer) ((GLenum) target, (GLenum) access);
  return (jlong) (intptr_t) _res;
}

/*   Java->C glue code:
 *   Java package: com.sun.opengl.impl.gl2es12.GL2ES12Impl
 *    Java method: ByteBuffer newDirectByteBuffer(long addr, int capacity);
 *     C function: jobject newDirectByteBuffer(jlong addr, jint capacity);
 */
JNIEXPORT jobject JNICALL
Java_com_sun_opengl_impl_gl2es12_GL2ES12Impl_newDirectByteBuffer(JNIEnv *env, jobject _unused, jlong addr, jint capacity) {
  return (*env)->NewDirectByteBuffer(env, (void*) (intptr_t) addr, capacity);
}
