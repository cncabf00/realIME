#pragma once
#include "stdafx.h"

using namespace std;

class Candidate
{
public:
	Candidate(string,vector<string>);
	~Candidate();

	/* data */
	string text;
	vector<string> restInput;
};

