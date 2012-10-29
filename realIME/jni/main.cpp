#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <string>
#include "TreeLanguageModel.h"
#include "Word.h"

using namespace std;

extern "C"
{
	jint Java_edu_njucs_realime_manager_InputManager_cReadDict(JNIEnv *env, jclass clazz, jobject fd_sys, jlong off, jlong len);
	jobjectArray Java_edu_njucs_realime_manager_InputManager_cGetWord(JNIEnv *env, jclass clazz,jlong id);
}

#define SUCCESS 0
#define ERROR_CODE_CANNOT_OPEN_MYFILE 100
#define ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD 101
#define ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS 102

TreeLanguageModel model;

int parseLine(char* line);



typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

/// Help to get the field ID.
struct field {
    const char *class_name;
    const char *field_name;
    const char *field_type;
    jfieldID   *jfield;
};

/// Define all the fields
struct fields_t {
    jfieldID    text;
    jfieldID    priority;
} fields;

/// A function helps you to get the all the field IDs
static int find_fields(JNIEnv *env, field *fields, int count)
{
    for (int i = 0; i < count; i++) {
        field *f = &fields[i];
        jclass clazz = env->FindClass(f->class_name);
        if (clazz == NULL) {
            return -1;
        }

        jfieldID field = env->GetFieldID(clazz, f->field_name, f->field_type);
        if (field == NULL) {
            return -1;
        }

        *(f->jfield) = field;
    }

    return 0;
}


jint Java_edu_njucs_realime_manager_InputManager_cReadDict(JNIEnv *env, jclass clazz, jobject fd_sys, jlong off, jlong len)
{
	long l=(long) len;

	__android_log_print(ANDROID_LOG_INFO, "realime", "test");


    jclass fdClass = env->FindClass("java/io/FileDescriptor");
           if (fdClass != NULL){
                   jfieldID fdClassDescriptorFieldID = env->GetFieldID(fdClass,
   "descriptor", "I");
                   if (fdClassDescriptorFieldID != NULL && fd_sys != NULL){
                           jint fd = env->GetIntField(fd_sys, fdClassDescriptorFieldID);
                           int myfd = fd;
                           FILE* myFile = fdopen(myfd, "r");
                           if (myFile){
                                   fseek(myFile, off, SEEK_SET);
                                   /***************************************************
                                       here myFile is a regular FILE* pointing to
                                       the file I want to read.
                                       I use the fscanf function:
                                   ***************************************************/
                                   long count=0;
                                   char var[100];
                                   while (fgets(var,100,myFile)!=NULL)
                                   {
                                	   if(ferror(myFile))
                                	   {
                                		   break;
                                	   }
									   count+=parseLine(var);
									   if (count>=l)
									   {
										   __android_log_print(ANDROID_LOG_INFO, "realime", "end");
										   break;
									   }
//									   if (count%10000==0)
//										   __android_log_print(ANDROID_LOG_INFO, "realime", "line = %s, count=%lu, len=%lu",var,count,l);
                                   }
                                   __android_log_print(ANDROID_LOG_INFO, "realime", "read finished");
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

jobjectArray Java_edu_njucs_realime_manager_InputManager_cGetWord(JNIEnv *env, jclass clazz,jlong id)
{
	/// An array for collecting all the fields
	    field fields_to_find[] = {
	        { "edu/njucs/realime/manager/Word", "text",  "Ljava/lang/String;", &fields.text },
	        { "edu/njucs/realime/manager/Word", "priority", "I", &fields.priority },
	    };

	    /// Find all the field IDs
	    find_fields(env, fields_to_find, 2);

	jobjectArray result;

	long i=(long)id;
	set<Word*>* s=&model.dict[i];
	result = env->NewObjectArray(s->size(), env->FindClass("edu/njucs/realime/manager/Word"),NULL);
	set<Word*>::iterator it;
	int k=0;
	for (it=s->begin();it!=s->end();it++)
	{
		jstring jstr= env->NewStringUTF((*it)->text.c_str());

		jclass clsWord = env->FindClass("edu/njucs/realime/manager/Word");
		jmethodID constructorWord = env->GetMethodID(clsWord, "<init>", "()V");

		jobject objWord = env->NewObject(clsWord, constructorWord, "");
		env->SetIntField(objWord, fields.priority, (*it)->priority);
		env->SetObjectField(objWord, fields.text, jstr);
		env->SetObjectArrayElement(result, k++, objWord);
	}

	return result;
}

int parseLine(char* line)
{
	long code=0;
	int k=0;
	int sep1 = 0;
	int sep2 = 0;
	int cur = 0;
	int num = 0;
	for (int i = 0; line[i]!='\0'; i++) {
		k++;
		if (sep2!=0)
			continue;
		if (line[i] == ' ') {
			if (cur == 0)
				sep1 = i;
			else {
				sep2 = i;
			}
			cur++;
		} else if (cur == 0 && line[i] == '\'') {
			num++;
		}
	}
	cur = 0;
	int count = 0;
	for (int i = 0; i < sep1; i++) {
		if (line[i] != '\'') {
			code = code * 10 + line[i];
			cur = i + 1;
		}
	}
	int priority=0;
	for (int i=sep2+1;line[i]!='\0';i++)
	{
		priority=priority*10+(line[i]-'0');
	}
	Word* word=new Word();
	word->text=string(line, sep1 + 1, sep2 - sep1 - 1);
	word->priority=priority;
	(model.dict[code]).insert(word);
	return k;
}


