#ifndef PINYINRESULT
#define PINYINRESULT
#include <vector>
#include <string>

class PinyinResult
{
public:
	PinyinResult();
	~PinyinResult();

	/* data */
	vector<string> wordCandidates;
	vector<string> restInput;
};

#endif