#include "HashTreeNode.cpp"

template <typename T>
class HashTreeNode
{
public:
	HashTreeNode()
	{
		children=NULL;
		parent=NULL;
		nodeInfo=NULL:
	}
	~HashTreeNode()
	{
		delete children;
		delete nodeInfo;
	}

	void addChild(HashTreeNode<T>* node)
	{
		if (children==NULL)
		{
			children=new map<string,HashTreeNode<T>>();
			node.parent=this;
			(*children)[node->key]=node;
		}
	}

	int childCount()
	{
		if (children==NULL)
			return 0;
		else
			return children->size(0;)
	}

	HashTreeNode<T>* childWithKey(string key)
	{
		if (children==NULL)
			return NULL;
		else
			return (*children)[key];
	}

	T* getNodeInfo()
	{
		return nodeInfo;
	}

	void setNodeInfo(T* info)
	{
		nodeInfo=info;
	}

	void setKey(string key)
	{
		this->key=key;
	}
	/* data */
};