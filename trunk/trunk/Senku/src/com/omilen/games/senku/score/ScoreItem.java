package com.omilen.games.senku.score;

import android.graphics.Bitmap;

public class ScoreItem implements Comparable<ScoreItem>{
    private int chips;
    private int score;
    private String name;
    private Bitmap PegRef;
    private Bitmap BoardRef;
    
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

	public void setPegRef(Bitmap pegRef) {
		PegRef = pegRef;
	}

	public Bitmap getPegRef() {
		return PegRef;
	}

	public void setBoardRef(Bitmap boardRef) {
		BoardRef = boardRef;
	}

	public Bitmap getBoardRef() {
		return BoardRef;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}
}
