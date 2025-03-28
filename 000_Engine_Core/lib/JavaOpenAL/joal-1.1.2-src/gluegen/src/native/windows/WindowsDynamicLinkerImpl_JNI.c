/* !---- DO NOT EDIT: This file autogenerated by com\sun\gluegen\JavaEmitter.java on Mon Jul 31 16:26:59 PDT 2006 ----! */

#include <jni.h>

#include <assert.h>

 #include <windows.h>
 /* This typedef is apparently needed for compilers before VC8 */
 #if _MSC_VER < 1400
 typedef int intptr_t;
 #endif

/*   Java->C glue code:
 *   Java package: com.sun.gluegen.runtime.WindowsDynamicLinkerImpl
 *    Java method: int FreeLibrary(long hLibModule)
 *     C function: BOOL FreeLibrary(HANDLE hLibModule);
 */
JNIEXPORT jint JNICALL 
Java_com_sun_gluegen_runtime_WindowsDynamicLinkerImpl_FreeLibrary__J(JNIEnv *env, jclass _unused, jlong hLibModule) {
  BOOL _res;
  _res = FreeLibrary((HANDLE) (intptr_t) hLibModule);
  return _res;
}


/*   Java->C glue code:
 *   Java package: com.sun.gluegen.runtime.WindowsDynamicLinkerImpl
 *    Java method: int GetLastError()
 *     C function: DWORD GetLastError(void);
 */
JNIEXPORT jint JNICALL 
Java_com_sun_gluegen_runtime_WindowsDynamicLinkerImpl_GetLastError__(JNIEnv *env, jclass _unused) {
  DWORD _res;
  _res = GetLastError();
  return _res;
}


/*   Java->C glue code:
 *   Java package: com.sun.gluegen.runtime.WindowsDynamicLinkerImpl
 *    Java method: long GetProcAddress(long hModule, java.lang.String lpProcName)
 *     C function: PROC GetProcAddress(HANDLE hModule, LPCSTR lpProcName);
 */
JNIEXPORT jlong JNICALL 
Java_com_sun_gluegen_runtime_WindowsDynamicLinkerImpl_GetProcAddress__JLjava_lang_String_2(JNIEnv *env, jclass _unused, jlong hModule, jstring lpProcName) {
  const char* _UTF8lpProcName = NULL;
  PROC _res;
  if (lpProcName != NULL) {
    if (lpProcName != NULL) {
      _UTF8lpProcName = (*env)->GetStringUTFChars(env, lpProcName, (jboolean*)NULL);
    if (_UTF8lpProcName == NULL) {
      (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/OutOfMemoryError"),
                       "Failed to get UTF-8 chars for argument \"lpProcName\" in native dispatcher for \"GetProcAddress\"");
      return 0;
    }
    }
  }
  _res = GetProcAddress((HANDLE) (intptr_t) hModule, (LPCSTR) _UTF8lpProcName);
  if (lpProcName != NULL) {
    (*env)->ReleaseStringUTFChars(env, lpProcName, _UTF8lpProcName);
  }
  return (jlong) (intptr_t) _res;
}


/*   Java->C glue code:
 *   Java package: com.sun.gluegen.runtime.WindowsDynamicLinkerImpl
 *    Java method: long LoadLibraryA(java.lang.String lpLibFileName)
 *     C function: HANDLE LoadLibraryA(LPCSTR lpLibFileName);
 */
JNIEXPORT jlong JNICALL 
Java_com_sun_gluegen_runtime_WindowsDynamicLinkerImpl_LoadLibraryA__Ljava_lang_String_2(JNIEnv *env, jclass _unused, jstring lpLibFileName) {
  const char* _UTF8lpLibFileName = NULL;
  HANDLE _res;
  if (lpLibFileName != NULL) {
    if (lpLibFileName != NULL) {
      _UTF8lpLibFileName = (*env)->GetStringUTFChars(env, lpLibFileName, (jboolean*)NULL);
    if (_UTF8lpLibFileName == NULL) {
      (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/OutOfMemoryError"),
                       "Failed to get UTF-8 chars for argument \"lpLibFileName\" in native dispatcher for \"LoadLibraryA\"");
      return 0;
    }
    }
  }
  _res = LoadLibraryA((LPCSTR) _UTF8lpLibFileName);
  if (lpLibFileName != NULL) {
    (*env)->ReleaseStringUTFChars(env, lpLibFileName, _UTF8lpLibFileName);
  }
  return (jlong) (intptr_t) _res;
}


