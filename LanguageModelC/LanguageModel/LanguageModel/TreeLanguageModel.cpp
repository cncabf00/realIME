#include "stdafx.h"
#include "TreeLanguageModel.h"
#include "Utils.h"

TreeLanguageModel::TreeLanguageModel()
{
	root=new HashTreeNode<LanguageNode>();
}


TreeLanguageModel::~TreeLanguageModel()
{
	delete root;
}

void TreeLanguageModel::build(string input)
{
	int last=0;
	int length=input.size();
	for (int i=0;i<length;i++)
	{
		if (input[i]=='\n')
		{
			insertToTree(parseLine(string(input,last,i-last)));
		}
	}
}

void TreeLanguageModel::insertToTree(Word* word)
{
	HashTreeNode<LanguageNode>* node=root;
	int i=0;
	int length=word->pinyins->size();
	for (;i<length;i++)
	{
		HashTreeNode<LanguageNode>* child=node->childWithKey((*word->pinyins)[i]);
		if (child==NULL)
		{
			break;
		}
		else
		{
			node=child;
		}
	}
	for (;i<length;i++)
	{
		HashTreeNode<LanguageNode>* child=node->childWithKey((*word->pinyins)[i]);
		if (child==NULL)
		{
			HashTreeNode<LanguageNode>* newNode = new HashTreeNode<LanguageNode>();
			newNode->setKey(new string(*(*word->pinyins)[i]));
			node->addChild(newNode);
			node=newNode;
		}
		else
			node=child;
		
	}
	if (node->nodeInfo==NULL)
		node->setNodeInfo(new LanguageNode());
	node->getNodeInfo()->addCandidate(new string(*(word->characters)));
}

vector<Candidate> TreeLanguageModel::getAllCandidates(vector<string> input)
{
	vector<Candidate> candidates;
		for (int i=input.size();i>=0;i--)
		{
			vector<string> currentInput;
			for (int k=0;k<i;k++)
			{
				currentInput.push_back(input[k]);
			}
			PinyinResult result=parse(currentInput);
			for (int j=0;j<result.wordCandidates.size();j++)
			{
				vector<string> restInput;
				vector<string>::iterator it;
				for (it = result.restInput.begin();it != result.restInput.end();it++)
				{
					restInput.push_back(*it);
				}
				for (int k=i;k<input.size();k++)
				{
					restInput.push_back(input[k]);
				}
				Candidate candidate(result.wordCandidates[j], restInput);
				vector<Candidate>::iterator it1;
				bool exist=false;
				for (it1 = candidates.begin();it1 != candidates.end();it1++)
				{
					if (it1->text.compare(candidate.text)==0)
					{
						exist=true;
						break;
					}
				}
				if (!exist)
					candidates.push_back(candidate);
			}
		}
		return candidates;
}

PinyinResult TreeLanguageModel::parse(vector<string> input)
{
	PinyinResult result;
		HashTreeNode<LanguageNode>* node=root;
		int i=0;
		for (;i<input.size();i++)
		{
			HashTreeNode<LanguageNode>* child=node->childWithKey(&input[i]);
			if (child==NULL)
			{
				break;
			}
			else
			{
				node=child;
			}
		}
		if (node->nodeInfo!=NULL)
		{
			set<string*>::iterator it;
			for (it = node->nodeInfo->candidates->begin();it != node->nodeInfo->candidates->end();it++)
			{
 				result.wordCandidates.push_back(**it);
			}	
		}
		if (i<=input.size())
		{
			for (int j=i;j<input.size();j++)
			{
				result.restInput.push_back(input[j]);
			}
		}
		
		return result;
}
