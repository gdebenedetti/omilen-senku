package com.omilen.games.senku.score;


public class ScoreItem implements Comparable<ScoreItem>{
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getPegs() {
		return pegs;
	}


	public void setPegs(int pegs) {
		this.pegs = pegs;
	}


	public int getGameNum() {
		return gameNum;
	}


	public void setGameNum(int gameNum) {
		this.gameNum = gameNum;
	}


	public int getPegNum() {
		return pegNum;
	}


	public void setPegNum(int pegNum) {
		this.pegNum = pegNum;
	}

	private String date;
	private int pegs;
    private int score;    
    private int gameNum;
    private int pegNum;
    
    public ScoreItem(String name, int ppegs,int pscore, int pgameNum, int ppegNum) {
    	this.date = name;
    	this.pegs    = ppegs;
    	this.score   = pscore;
    	this.gameNum = pgameNum;
    	this.pegNum  = ppegNum;
    }
    

	@Override
	public int compareTo(ScoreItem another) {
		if(another.getScore()>this.score)
			return -1;
		else
			return 1;	
	}


	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}
}
