package edu.njucs.model;

public class HashTree<T> {
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
