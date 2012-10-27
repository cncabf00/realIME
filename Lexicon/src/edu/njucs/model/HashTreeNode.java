package edu.njucs.model;

import java.util.HashMap;
import java.util.Map;

public class HashTreeNode<T>{

	Map<String,HashTreeNode<T>> children=new HashMap<String,HashTreeNode<T>>();
	HashTreeNode<T> parent=null;
	T nodeInfo=null;
	String key;
	
	public void addChild(HashTreeNode<T> node)
	{
		node.parent=this;
		children.put(node.key, node);
	}
	
	public int childCount()
	{
		return children.size();
	}
	
	public HashTreeNode<T> childWithKey(Object key)
	{
		return children.get(key);
	}
	
	public HashTreeNode<T> getParent()
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
	
	public void setKey(String key)
	{
		this.key=key;
	}
	
}
