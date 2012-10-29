#pragma once
#include "stdafx.h"
#include <set>

using namespace std;
class LanguageNode
{
public:
	LanguageNode(string*);
	~LanguageNode();
	void addCandidate(string*);
	/* data */
	string* key;
	set<string*>* candidates;
};

