#pragma once
#include "stdafx.h"
#include "HashTreeNode.h"
#include "Candidate.h"
#include "Word.h"
#include "LanguageNode.h"
#include "PinyinResult.h"

using namespace std;

class TreeLanguageModel
{
public:
	TreeLanguageModel();
	~TreeLanguageModel();
	void build(string input);
	vector<Candidate> getAllCandidates(vector<string> input);
	void insertToTree(Word* word);

	/* data */
	HashTreeNode<LanguageNode>* root;

private:
	PinyinResult parse(vector<string> input);
};

