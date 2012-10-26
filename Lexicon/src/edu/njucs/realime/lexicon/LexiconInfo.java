package edu.njucs.realime.lexicon;

public class LexiconInfo {
	char character;
	String charPath;
	
	public LexiconInfo(char character, String charPath) {
		super();
		this.character = character;
		this.charPath = charPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + character;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LexiconInfo other = (LexiconInfo) obj;
		if (character != other.character)
			return false;
		return true;
	}
	
	
	
}

