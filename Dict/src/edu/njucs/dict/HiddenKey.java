package edu.njucs.dict;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HiddenKey {
	String value;
	Set<Key> refs=new HashSet<Key>();
	
	public HiddenKey(String abbriviation)
	{
		value=abbriviation;
	}
	
	public void add(Key key)
	{
		refs.add(key);
	}
	
	public void addAll(Collection<Key> keys)
	{
		refs.addAll(keys);
	}
}
