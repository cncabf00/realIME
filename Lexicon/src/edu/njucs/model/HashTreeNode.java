package edu.njucs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTreeNode<T> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4578330507921666833L;
	Map<Object,HashTreeNode<T>> children=null;
//	HashTreeNode<T> parent=null;
	T nodeInfo=null;
	Object key;
	
	public HashTreeNode<T> addChild(HashTreeNode<T> node)
	{

		if (children==null)
		{
			children=new HashMap<Object, HashTreeNode<T>>();
		}
		if (children.containsKey(node.key)) {
			node = children.get(node.key);
		} else {
			children.put((Object) node.key, node);
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
	
//	public HashTreeNode<T> getParent()
//	{
//		return this.parent;
//	}
	
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
	
	public void setKey(Object key)
	{
		this.key=key;
	}

	public List<HashTreeNode<T>> getAllLeafs()
	{
		List<HashTreeNode<T>> leafs=new ArrayList<HashTreeNode<T>>();
		Collection<HashTreeNode<T>> allChildren=children.values();
		List<HashTreeNode<T>> array=new ArrayList<HashTreeNode<T>>(allChildren);
		while (array.size()>0)
		{
			HashTreeNode<T> node=array.get(0);
			if (!node.hasChild())
			{
				leafs.add(node);
				array.remove(0);
			}
			else
			{
				array.addAll(node.children.values());
				array.remove(0);
			}
		}
		
		return leafs;
	}
	
	public Collection<HashTreeNode<T>> getAllChildren()
	{
		return children.values();
	}
	
}
