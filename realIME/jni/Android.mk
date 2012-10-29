LOCAL_PATH := $(call my-dir)
#
# Example Library
#


include $(CLEAR_VARS)

LOCAL_MODULE           := language-model
LOCAL_SRC_FILES        := main.cpp TreeLanguageModel.cpp Word.cpp
LOCAL_LDLIBS 		   := -llog

include $(BUILD_SHARED_LIBRARY)

