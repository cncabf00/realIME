#pragma once
#include "stdafx.h"

using namespace std;
class Word
{
public:
	Word();
	~Word();

	/* data */
	vector<string*>* pinyins;
	string* characters;
};

