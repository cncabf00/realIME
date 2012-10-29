#pragma once
#include "stdafx.h"

using namespace std;
class PinyinResult
{
public:
	PinyinResult();
	~PinyinResult();

	/* data */
	vector<string> wordCandidates;
	vector<string> restInput;
};

