#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <fstream>
#include <string>

using namespace std;
#define SUCCESS                                                                                 0
#define ERROR_CODE_CANNOT_OPEN_MYFILE                                 100
#define ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD                        101
#define ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS                102
extern "C"
{
jint Java_edu_njucs_realime_manager_InputManager_test(JNIEnv *env, jclass clazz, jobject fd_sys, jlong off, jlong len)
{
	__android_log_print(ANDROID_LOG_INFO, "realime", "test");


    jclass fdClass = env->FindClass("java/io/FileDescriptor");
           if (fdClass != NULL){
                   jfieldID fdClassDescriptorFieldID = env->GetFieldID(fdClass,
   "descriptor", "I");
                   if (fdClassDescriptorFieldID != NULL && fd_sys != NULL){
                           jint fd = env->GetIntField(fd_sys, fdClassDescriptorFieldID);
                           int myfd = fd;
                           FILE* myFile = fdopen(myfd, "rb");
                           if (myFile){
                                   fseek(myFile, off, SEEK_SET);
                                   /***************************************************
                                       here myFile is a regular FILE* pointing to
                                       the file I want to read.
                                       I use the fscanf function:
                                   ***************************************************/
                                   char var[100];
                                   fgets(var,100,myFile);
                                   __android_log_print(ANDROID_LOG_INFO, "realime", "line = %s",var);
                                   return (jint)SUCCESS;
                           }
                           else {
                                   return (jint) ERROR_CODE_CANNOT_OPEN_MYFILE;
                           }
                   }
                   else {
                           return (jint)ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD;
                   }
           }
           else {
                   return (jint)ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS;
           }
}
}


