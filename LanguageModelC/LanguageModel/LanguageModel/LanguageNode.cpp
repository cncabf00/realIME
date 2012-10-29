#include "stdafx.h"
#include "LanguageNode.h"


LanguageNode::LanguageNode(string* key)
{
	this->key=key;
}


LanguageNode::~LanguageNode()
{
	delete candidates;
}

void LanguageNode::addCandidate(string* str)
{
	if (candidates==NULL)
		candidates=new set<string*>();
	candidates->insert(str);
}

