#pragma once
#include <string>

using namespace std;

class Word
{
public:
	Word();
	~Word();

	/* data */
	string text;
	int priority;
};

