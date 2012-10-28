#ifndef TREELANGUAGEMODEL
#define TREELANGUAGEMODEL
#include <vector>

using namespace std;

class TreeLanguageModel
{
public:
	TreeLanguageModel();
	~TreeLanguageModel();
	void build(string input);
	Vector<Candidate> getAllCandidates(Vector<String> input);
private:
	void insertIntoTree(Word word);

	/* data */
	HashTreeNode<LanguageNode>* root;
};



#endif