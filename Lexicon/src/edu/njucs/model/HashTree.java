package edu.njucs.model;

public class HashTree<T> implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5606596356961776815L;
	HashTreeNode<T> root;
	
	public HashTree(HashTreeNode<T> node)
	{
		this.root=node;
	}
	
	public HashTreeNode<T> getRoot()
	{
		return this.root;
	}
}
