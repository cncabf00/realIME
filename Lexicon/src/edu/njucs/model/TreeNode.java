package edu.njucs.model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
	List<TreeNode<T>> children=new ArrayList<TreeNode<T>>();
	TreeNode<T> parent=null;
	T nodeInfo=null;
	String key;
	
	public void addChild(TreeNode<T> node)
	{
		node.parent=this;
		children.add(node);
	}
	
	public TreeNode<T> leftChild()
	{
		if (children.isEmpty())
			return null;
		else
			return children.get(0);
	}
	
	public TreeNode<T> rightChild()
	{
		if (children.isEmpty())
			return null;
		else
			return children.get(children.size()-1);
	}
	
	public int childCount()
	{
		return children.size();
	}
	
	public TreeNode<T> childAt(int pos)
	{
		if (pos<0 || pos>=children.size())
			return null;
		else
			return children.get(pos);
	}
	
	public TreeNode<T> getParent()
	{
		return this.parent;
	}
	
	public T getNodeInfo()
	{
		return nodeInfo;
	}
	
	public void setNodeInfo(T info)
	{
		this.nodeInfo=info;
	}
	
	public boolean hasChild()
	{
		return children.size()>0;
	}
	
	public TreeNode<T> childWithKey(Object key)
	{
		for (int i=0;i<this.childCount();i++)
		{
			if (this.childAt(i).key.equals(key))
				return this.childAt(i);
		}
		return null;
	}
	
	public void setKey(String key)
	{
		this.key=key;
	}
}
