#ifndef HASHTREENODE
#define HASHTREENODE
#include <map>
#include <string>

using namespace std;

template <typename T>
class HashTreeNode
{
public:
	HashTreeNode();
	~HashTreeNode();
	void addChild(HashTreeNode<T>* node);
	int childCount();
	HashTreeNode<T>* childWithKey(string key);
	HashTreeNode<T>* getParent();
	T* getNodeInfo();
	void setNodeInfo(T info);
	bool hasChild();
	void setKey(string key);
	/* data */
	map<string,HashTreeNode<T>* children;
	HashTreeNode<T>* parent;
	T* nodeInfo;
	string key;
};


#endif