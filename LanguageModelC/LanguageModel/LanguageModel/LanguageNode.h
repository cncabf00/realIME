#pragma once
#include "stdafx.h"
#include <set>

using namespace std;
class LanguageNode
{
public:
	LanguageNode();
	~LanguageNode();
	void addCandidate(string*);
	/* data */
	set<string*>* candidates;
};

