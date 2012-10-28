#ifndef WORD
#define WORD
#include <vector>
#include <string>

using namespace std;

class Word
{
public:
	Word();
	~Word();

	/* data */
	vector<string> pinyins;
	string characters;
};


#endif