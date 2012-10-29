#ifndef LANGUAGENODE
#define LANGUAGENODE
#include <string>

using namespace std;

class LanguageNode
{
public:
	LanguageNode(string);
	~LanguageNode();
	void addCandidate(string);
	/* data */
	string key;
	set<string> candidates;
};

#endif