package edu.njucs.model;

import java.util.HashMap;
import java.util.Map;

public class HashTreeNode<T> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4578330507921666833L;
	Map<String,HashTreeNode<T>> children=null;
	HashTreeNode<T> parent=null;
	T nodeInfo=null;
	String key;
	
	public HashTreeNode<T> addChild(HashTreeNode<T> node)
	{
		if (children==null)
			children=new HashMap<String, HashTreeNode<T>>();
		if (children.containsKey(node.key))
		{
			node=children.get(node.key);
		}
		else
		{
			node.parent=this;
			children.put(node.key,node);
		}
		return node;
	}
	
	public int childCount()
	{
		if (children==null)
			return 0;
		return children.size();
	}
	
	public HashTreeNode<T> childWithKey(Object key)
	{
		if (children==null)
			return null;
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
		return children!=null;
	}
	
	public void setKey(String key)
	{
		this.key=key;
	}
	
}
