/*!
 * Copyright 2010-2011, Omilen IT Solutions
 * licensed under Apache Version 2.0, http://www.apache.org/licenses/
 * http://www.omilenitsolutions.com/
 * Author: Juan Manuel Rodríguez
 */
package com.omilen.games.senku;


public class SenkuPegs{
	
	public static final int NUMBER_OF_PEGS = 8;
	public static final String MASTER_PHRASE = "OMILEN";
	//plastic    => 10   // Default
	//wood       => 20   // 1 Plus board || 2 Home board
	//silver     => 50   // 1 Home Board || 2 pyramid || 4 normal || 4 European death board
	//gold       => 100  // 1 pyramid Board || 2 diamond || 3 normal || 3 European death board
	//sapphire   => 250  // 1 Diamond board || 2 normal board || 3 European death board
	//Emerald    => 500  // 1 Normal board
	//Diamond    => 1000 // 1 European death board
	//Eight ball => 800  // only if special code is typed
		
	protected Peg[] PEGS = new Peg[NUMBER_OF_PEGS];
	private static SenkuPegs instance = null; 
		
	private SenkuPegs(){
		if(PEGS[0] == null){
			PEGS[0] = new Peg("Default", 10);
			PEGS[1] = new Peg("12385a902841249dde68ccc9689f9f94", 20);
			PEGS[2] = new Peg("812deb858fa4d3b2a35bc71f9373bcab", 50);
			PEGS[3] = new Peg("7b528fc3aa72df35333ef9fca93253e9", 100);
			PEGS[4] = new Peg("7a856d8ebc838041627e87503bf2474d", 250);
			PEGS[5] = new Peg("5658af43062660d6e137451b6b702d5f", 500);
			PEGS[6] = new Peg("b5acaebca6f29532ea70ce54b45259ce", 1000);
			PEGS[7] = new Peg("27c47245ea6b81a697d37db0333d94e9", 800);
		}
	}
	
	public String unLockSpecialPegs(String phrase){
		if(phrase.compareTo(MASTER_PHRASE)==0){
			return PEGS[7].codeName;
		}
		return "";
	}
	public String unLockPegs(int gameType,int pegCount){
	
		if(pegCount>4 || gameType==0){ return "";}
		
		switch(gameType){
			case 1: //PLUS
					if(pegCount==1) return PEGS[1].codeName;	
				break;
			case 2: //HOME
					if(pegCount==2) return PEGS[1].codeName;
					if(pegCount==1) return PEGS[2].codeName;
				break;
			case 3://PYRAM
					if(pegCount==2) return PEGS[2].codeName;
					if(pegCount==1) return PEGS[3].codeName;
				break;
			case 4://DIAMON
					if(pegCount==2) return PEGS[3].codeName;
					if(pegCount==1) return PEGS[4].codeName;
				break;
			case 5://Normal
					if(pegCount==4) return PEGS[2].codeName;
					if(pegCount==3) return PEGS[3].codeName;
					if(pegCount==2) return PEGS[4].codeName;
					if(pegCount==1) return PEGS[5].codeName;
				break;
			case 6://european Deadth
					if(pegCount==4) return PEGS[2].codeName;
					if(pegCount==3) return PEGS[3].codeName;
					if(pegCount==2) return PEGS[4].codeName;
					if(pegCount==1) return PEGS[6].codeName;
				break;
		}
		return "";
	}
	
	public Peg[] getPegs(){
		return this.PEGS;
	}
	
	public static SenkuPegs getInstance(){
		if(instance==null){
			instance = new SenkuPegs();
		}
		
		return instance;
	}
	
	public class Peg{		
		private String codeName;
		private int scoreValue;
		
		Peg(String name, int value){
			this.codeName=name;
			this.scoreValue=value; 
		}

		public String getCodeName() {
			return codeName;
		}

		public int getScoreValue() {
			return scoreValue;
		}
	}
	
}
