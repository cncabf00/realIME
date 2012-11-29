#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <string>
#include <vector>
#include <utility>
#include <algorithm>
#include "TreeLanguageModel.h"
#include "Word.h"

using namespace std;

extern "C" {
jint Java_edu_njucs_realime_manager_InputManager_cReadDict(JNIEnv *env,
		jclass clazz, jobject fd_sys, jlong off, jlong len);
jint Java_edu_njucs_realime_manager_InputManager_cReadTransfer(JNIEnv *env,
		jclass clazz, jobject fd_sys, jlong off, jlong len);
jobjectArray Java_edu_njucs_realime_manager_InputManager_cGetWord(JNIEnv *env,
		jclass clazz, jstring id);
}

#define SUCCESS 0
#define ERROR_CODE_CANNOT_OPEN_MYFILE 100
#define ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD 101
#define ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS 102
#define MAX_SIZE 128

struct Item {
	string code;
	int frequency;
};

struct TransInfo {
	vector<Item> transfer;
	int totalFreq;
};

typedef union {
	JNIEnv* env;
	void* venv;
} UnionJNIEnvToVoid;

/// Help to get the field ID.
struct field {
	const char *class_name;
	const char *field_name;
	const char *field_type;
	jfieldID *jfield;
};

/// Define all the fields
struct fields_t {
	jfieldID text;
	jfieldID priority;
} fields;

TreeLanguageModel model;
map<string, TransInfo> transferMap;

int parseDictLine(char* line);
int parseTransferLine(char* line);
set<pair<double, Word*> >* getWords(string code);
string getSpecialAbbreviation(string pinyin);
string getAbbreviation(string pinyin);
bool isAbbreviationOf(string abbr,string origin);

/// A function helps you to get the all the field IDs
static int find_fields(JNIEnv *env, field *fields, int count) {
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

jint Java_edu_njucs_realime_manager_InputManager_cReadDict(JNIEnv *env,
		jclass clazz, jobject fd_sys, jlong off, jlong len) {
	long l = (long) len;

	__android_log_print(ANDROID_LOG_INFO, "realime", "read dict");

	jclass fdClass = env->FindClass("java/io/FileDescriptor");
	if (fdClass != NULL) {
		jfieldID fdClassDescriptorFieldID = env->GetFieldID(fdClass,
				"descriptor", "I");
		if (fdClassDescriptorFieldID != NULL && fd_sys != NULL) {
			jint fd = env->GetIntField(fd_sys, fdClassDescriptorFieldID);
			int myfd = fd;
			FILE* myFile = fdopen(myfd, "r");
			if (myFile) {
				fseek(myFile, off, SEEK_SET);
				long count = 0;
				int size = 256;
				char var[size];
				while (fgets(var, size, myFile) != NULL) {
					if (ferror(myFile)) {
						break;
					}
					count += parseDictLine(var);
					if (count >= l) {
						__android_log_print(ANDROID_LOG_INFO, "realime", "end");
						break;
					}
				}
				__android_log_print(ANDROID_LOG_INFO, "realime",
						"read finished");
				return (jint) SUCCESS;
			} else {
				return (jint) ERROR_CODE_CANNOT_OPEN_MYFILE;
			}
		} else {
			return (jint) ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD;
		}
	} else {
		return (jint) ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS;
	}
}

jint Java_edu_njucs_realime_manager_InputManager_cReadTransfer(JNIEnv *env,
		jclass clazz, jobject fd_sys, jlong off, jlong len) {
	long l = (long) len;

	__android_log_print(ANDROID_LOG_INFO, "realime", "test");

	jclass fdClass = env->FindClass("java/io/FileDescriptor");
	if (fdClass != NULL) {
		jfieldID fdClassDescriptorFieldID = env->GetFieldID(fdClass,
				"descriptor", "I");
		if (fdClassDescriptorFieldID != NULL && fd_sys != NULL) {
			jint fd = env->GetIntField(fd_sys, fdClassDescriptorFieldID);
			int myfd = fd;
			FILE* myFile = fdopen(myfd, "r");
			if (myFile) {
				fseek(myFile, off, SEEK_SET);
				long count = 0;
				int size = 2048;
				char var[size];
				while (fgets(var, size, myFile) != NULL) {
					if (ferror(myFile)) {
						break;
					}
					count += parseTransferLine(var);
					if (count >= l) {
						__android_log_print(ANDROID_LOG_INFO, "realime", "end");
						break;
					}
				}
				__android_log_print(ANDROID_LOG_INFO, "realime",
						"read finished");
				return (jint) SUCCESS;
			} else {
				return (jint) ERROR_CODE_CANNOT_OPEN_MYFILE;
			}
		} else {
			return (jint) ERROR_CODE_CANNOT_GET_DESCRIPTOR_FIELD;
		}
	} else {
		return (jint) ERROR_CODE_CANNOT_GET_FILE_DESCRIPTOR_CLASS;
	}
}

jobjectArray Java_edu_njucs_realime_manager_InputManager_cGetWord(JNIEnv *env,
		jclass clazz, jstring id) {
	__android_log_print(ANDROID_LOG_INFO, "realime", "get word");
	/// An array for collecting all the fields
	field fields_to_find[] = { { "edu/njucs/realime/manager/Word", "text",
			"Ljava/lang/String;", &fields.text },
			{ "edu/njucs/realime/manager/Word", "priority", "I",
					&fields.priority }, };

	/// Find all the field IDs
	find_fields(env, fields_to_find, 2);

	jobjectArray result;

	const char *nativeString = env->GetStringUTFChars(id, 0);

	string i = string(nativeString);
	set<Word*>* s = &model.dict[i];
//	__android_log_print(ANDROID_LOG_INFO, "realime", "code=%s size=%d",nativeString,s->size());
	if (s->size() == 0) {
		set<pair<double, Word*> >* tempS = getWords(i);
		result = env->NewObjectArray(
				tempS->size() < MAX_SIZE ? tempS->size() : MAX_SIZE,
				env->FindClass("edu/njucs/realime/manager/Word"), NULL);
		set<pair<double, Word*> >::iterator it;
		int k = 0;
		for (it = tempS->begin(); it != tempS->end() && k < MAX_SIZE; it++) {
			jstring jstr = env->NewStringUTF((*it).second->text.c_str());

			jclass clsWord = env->FindClass("edu/njucs/realime/manager/Word");
			jmethodID constructorWord = env->GetMethodID(clsWord, "<init>",
					"()V");

			jobject objWord = env->NewObject(clsWord, constructorWord, "");
			env->SetIntField(objWord, fields.priority,
					(int) ((*it).first * 10000));
			env->SetObjectField(objWord, fields.text, jstr);
			env->SetObjectArrayElement(result, k++, objWord);
//			delete (*it);
		}
		delete tempS;
	} else {
		result = env->NewObjectArray(
				s->size() < MAX_SIZE ? s->size() : MAX_SIZE,
				env->FindClass("edu/njucs/realime/manager/Word"), NULL);
		set<Word*>::iterator it;
		int k = 0;
		for (it = s->begin(); it != s->end() && k < MAX_SIZE; it++) {
			jstring jstr = env->NewStringUTF((*it)->text.c_str());

			jclass clsWord = env->FindClass("edu/njucs/realime/manager/Word");
			jmethodID constructorWord = env->GetMethodID(clsWord, "<init>",
					"()V");

			jobject objWord = env->NewObject(clsWord, constructorWord, "");
			env->SetIntField(objWord, fields.priority, (*it)->priority);
			env->SetObjectField(objWord, fields.text, jstr);
			env->SetObjectArrayElement(result, k++, objWord);
		}
	}

	return result;
}

   string getSpecialAbbreviation(string pinyin)
    {
    	return "'"+getAbbreviation(pinyin);
    }

   string getAbbreviation(string pinyin)
	{
//	   __android_log_print(ANDROID_LOG_INFO, "realime", "in getAbbreviation %s ",pinyin.c_str());
		string abbr="";
		bool add=true;
		for (int i=0;i<pinyin.length();i++)
		{
			if (add)
			{
				abbr+=pinyin[i];
				if (i!=pinyin.length()-1 && pinyin[i+1]=='h')
				{
					abbr+='h';
				}
				add=false;
				if (i!=pinyin.length()-1)
				{
					abbr+='\'';
				}
			}
			else if (pinyin[i]=='\'')
			{
				add=true;
			}
		}
		return abbr;
	}

   bool isAbbreviationOf(string abbr,string origin)
   {
//	   __android_log_print(ANDROID_LOG_INFO, "realime", "in isAbbreviationOf %s %s",abbr.c_str(),origin.c_str());
	   bool compare=true;
	   for (int i=0,j=0;i<abbr.length();i++,j++)
	   {
		   if (j>=origin.length())
		   {
			   return false;
		   }
		   else if (compare)
		   {
			   if (abbr[i]=='\'')
			   {
				   while (origin[j]!='\'')
				   {
					   if (j>=origin.length())
					   {
						   return false;
					   }
					   else
					   {
						   j++;
					   }
				   }
			   }
			   else if (abbr[i]!=origin[j])
			   {
				   return false;
			   }
		   }
	   }
	   return true;
   }
   namespace std
   {
   template<>
     bool operator < (const pair<double, Word*> & l, const pair<double, Word*> & r)
     {
	   return l.second->text.compare(r.second->text);
     }
   }

set<pair<double, Word*> >* getWords(string code) {
	set<pair<double, Word*> >* res;
	res = new set<pair<double, Word*> >();
	TransInfo* pInfo = &(transferMap[code]);
//	__android_log_print(ANDROID_LOG_INFO, "realime", "1");
	if (pInfo->totalFreq == 0) {
		TransInfo* spec=&(transferMap[getSpecialAbbreviation(code)]);
//		__android_log_print(ANDROID_LOG_INFO, "realime", "2");
		if (spec->totalFreq!=0)
		{
//			__android_log_print(ANDROID_LOG_INFO, "realime", "3");
			string ref=spec->transfer[0].code;
			if (isAbbreviationOf(code,ref))
			{
//				__android_log_print(ANDROID_LOG_INFO, "realime", "4");
				pInfo=spec;
			}
		}
	}
	vector<pair<double, Word*> >* wordArray =
			new vector<pair<double, Word*> >();
	int size = pInfo->transfer.size();
	for (int i = 0; i < size; i++) {
		set<Word*>* tempS = &model.dict[(pInfo->transfer)[i].code];
		set<Word*>::iterator it;
		for (it = tempS->begin(); it != tempS->end(); it++) {
			double per = ((double) (*it)->priority)
					* (pInfo->transfer)[i].frequency / pInfo->totalFreq;
			wordArray->push_back(make_pair(per, *it));
		}
	}
//		__android_log_print(ANDROID_LOG_INFO, "realime", "start sort");
//		sort(&(*wordArray)[0],&(*wordArray)[wordArray->size()-1]);
	bool changed = true;
	size = wordArray->size();
	double avg = 0;
	while (MAX_SIZE < size && changed) {
		double nextAvg = 0;
		int nextSize = 0;
		for (int i = 0; i < wordArray->size(); i++) {
			if ((*wordArray)[i].first > avg) {
				nextAvg += (*wordArray)[i].first;
				nextSize++;
			}
		}
		nextAvg /= nextSize;
		avg = nextAvg;
		if (size == nextSize)
			changed = false;
		size = nextSize;
	}

	for (int i = 0; i < wordArray->size(); i++) {
		if ((*wordArray)[i].first > avg) {
			res->insert((*wordArray)[i]);
		}
	}
	delete wordArray;
	return res;
}

int parseDictLine(char* line) {

	int sep1 = 0;
	for (int i = 0; line[i] != '\0'; i++) {
		if (line[i] == ' ') {
			sep1 = i;
			break;
		}
//		else if (line[i] != '\'') {
//			code = code * 27 + (line[i] - 'a' + 1);
//		} else {
//			code = code * 27 + 27;
//		}
	}
	string code = string(line, 0, sep1);

	bool extra = true;

	int pos = sep1 + 1;
	int last = pos;
	while (line[pos] != '\0') {
		Word* word = new Word();
		bool after = false;
		int priority = 0;
		while (true) {
			if (line[pos] == ',') {
				word->text = string(line, last, pos - last);
				after = true;
			} else if (after) {
				if (line[pos] == ';' || line[pos] == '\0') {
					word->priority = priority;
					(model.dict[code]).insert(word);
//					if (priority<0)
//					{
////						__android_log_print(ANDROID_LOG_INFO, "realime", "code=%lu insert=%s priority=%d",code,word->text.c_str(),priority);
//					}
					last = pos + 1;
					if (line[pos] != '\0')
						pos++;
					break;
				} else {
					if (line[pos] >= '0' && line[pos] <= '9') {
						priority = priority * 10 + (line[pos] - '0');
//						if (priority<0)
//						{
////							__android_log_print(ANDROID_LOG_INFO, "realime", "line=%s line[pos]=%c",line,line[pos]);
//						}
					}

				}
			}
			pos++;
		}
	}
	return pos;
}

int parseTransferLine(char* line) {

	int sep1 = 0;
	for (int i = 0; line[i] != '\0'; i++) {
		if (line[i] == ' ') {
			sep1 = i;
			break;
		}
//		else if (line[i] != '\'') {
//			code = code * 27 + (line[i] - 'a' + 1);
//		} else {
//			code = code * 27 + 27;
//		}
	}
	string code = string(line, 0, sep1);

	bool extra = true;

	(transferMap[code]).totalFreq = 0;
	int pos = sep1 + 1;
	int last = pos;
	while (line[pos] != '\0') {
		Item item;
		bool after = false;
		int frequency = 0;
		while (true) {
			if (line[pos] == ',') {
//				for (int j = last; j < pos; j++) {
//					if (line[j] != '\'') {
//						c = c * 27 + (line[j] - 'a' + 1);
//					} else {
//						c = c * 27 + 27;
//					}
//				}
				item.code = string(line, last, pos - last);
				after = true;
			} else if (after) {
				if (line[pos] == ';' || line[pos] == '\0') {
					item.frequency = frequency;
					(transferMap[code]).transfer.push_back(item);
					(transferMap[code]).totalFreq += frequency;
					last = pos + 1;
					if (line[pos] != '\0')
						pos++;
					break;
				} else {
					if (line[pos] >= '0' && line[pos] <= '9') {
						frequency = frequency * 10 + (line[pos] - '0');
					}
				}
			}
			pos++;
		}
	}
	return pos;
}
