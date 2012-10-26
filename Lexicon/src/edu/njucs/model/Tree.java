package edu.njucs.model;

public class Tree<T> {
	TreeNode<T> root;
	
	public Tree(TreeNode<T> node)
	{
		this.root=node;
	}
	
	public TreeNode<T> getRoot()
	{
		return this.root;
	}
}
