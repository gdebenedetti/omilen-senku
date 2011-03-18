package com.omilen.games.senku.score;

public class ScoreItem implements Comparable<ScoreItem>{
    private int chips;
    private String name;
    
    public ScoreItem(String pname, int pchip) {
    	this.chips = pchip;
    	this.name = pname;        
    }
    
    public int getChips() {
        return this.chips;
    }
    public String getName() {
        return this.name;
    }

	@Override
	public int compareTo(ScoreItem another) {
		if(another.getChips()>this.chips)
			return -1;
		else
			return 1;	
	}
}
