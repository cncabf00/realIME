#pragma once
#include <map>
#include <string>
#include <set>
#include "Word.h"

using namespace std;

class TreeLanguageModel
{
public:
	TreeLanguageModel();
	~TreeLanguageModel();

	/* data */
	map<jlong,set<Word*> > dict;
};

