package com.omilen.games.senku;

import android.os.Bundle;

public class SenkuModel implements Cloneable {
	
	public static int ANCHO = 7;
	public static int LARGO = 7;
	
	
	protected int[][] grilla  = { { -1, -1, 1, 1, 1, -1, -1 }, { -1, -1, 1, 1, 1, -1, -1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 0, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { -1, -1, 1, 1, 1, -1, -1 }, { -1, -1, 1, 1, 1, -1, -1 } };	
	protected int currentKeyX = 3;
	protected int currentKeyY = 3;
	protected int currentGameType = 5;
	protected int currentPegType = 0;
	protected int pegcount = -1;
	protected int score = 0;
	
	public int getCurrentGameType() {
		return currentGameType;
	}

	public void setCurrentGameType(int currentGameType) {
		if(currentGameType<0 || currentGameType>SenkuGames.GAME_TYPES){
			return;
		}
		this.currentGameType = currentGameType;		
	}

	protected boolean selected = false;
	/*Backup Atributes*/
	protected int[][] grillaPrevia = null;
	protected int currentKeyXPrevia = 3;
	protected int currentKeyYPrevia = 3;
	protected boolean selectedPrevia = true;
		
	public int getCurrentKeyX() {
		return currentKeyX;
	}

	public int getCurrentKeyY() {
		return currentKeyY;
	}	
	
	public SenkuModel(){
		this.start();
	}	
	
	public int getCelda(int x, int y){
		if(x>=0 && x <ANCHO){
			if(y>=0 && y<LARGO){
				return this.grilla[x][y];
			}
		}
		return -1;
	}
	public void start(){
		
		
		int[][] aux = SenkuGames.GAMES[this.currentGameType];
		
		for(int i=0;i<ANCHO;i++){
			for(int j=0;j<LARGO;j++){
				this.grilla[i][j] = aux[i][j];
			}
		}
		pegcount = -1;
		score = 0;
		currentKeyX = 3;
		currentKeyY = 3;
		selected = false;		
	}
	
	public boolean isEnded(){
		
		for(int i=0;i<ANCHO;i++){
			for(int j=0;j<LARGO;j++){				
				if(this.grilla[i][j] == 1){
					for(int k=0;k<4;k++){
						if(checkMovementsOf(i,j,k)) return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * k= 0 => norte /noth
	 * k= 1 => este  /east
	 * k= 2 => oeste /west
	 * k= 3 => sur   /south
	 * */
	private boolean checkMovementsOf(int i, int j, int k) {

		switch (k) {
			case 0: if(j>1){
						if(this.grilla[i][j-1]==1 && this.grilla[i][j-2]==0) return true;
					}
					break;
			case 1: if(i>1){
						if(this.grilla[i-1][j]==1 && this.grilla[i-2][j]==0) return true;
					}
					break;
			case 2: if(i<5){
						if(this.grilla[i+1][j]==1 && this.grilla[i+2][j]==0) return true;
					}				
					break;
			case 3:if(j<5){
						if(this.grilla[i][j+1]==1 && this.grilla[i][j+2]==0) return true;
					}
					break;
		}		
		return false;
	}
	
	public boolean eatNorth() {
		if(!this.selected) return false;
		if(!checkMovementsOf(this.currentKeyX,this.currentKeyY,0)){
			this.selected = false;
			return false;
		}
		this.grillaPrevia = (int[][])copyArray(this.grilla);
		this.grilla[this.currentKeyX][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX][this.currentKeyY-1] = 0;
		this.grilla[this.currentKeyX][this.currentKeyY-2] = 1;
		this.selected = false;		
		this.currentKeyY = this.currentKeyY-2;
		return true;
	}
	public boolean eatEast() {
		if(!this.selected) return false;
		if(!checkMovementsOf(this.currentKeyX,this.currentKeyY,1)){
			this.selected = false;
			return false;
		}
		this.grillaPrevia = (int[][])copyArray(this.grilla);
		this.grilla[this.currentKeyX][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX-1][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX-2][this.currentKeyY] = 1;
		this.selected = false;		
		this.currentKeyX = this.currentKeyX-2;
		return true;
	}
	public boolean eatWest() {
		if(!this.selected) return false;
		if(!checkMovementsOf(this.currentKeyX,this.currentKeyY,2)){
			this.selected = false;
			return false;
		}
		this.grillaPrevia = (int[][])copyArray(this.grilla);
		this.grilla[this.currentKeyX][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX+1][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX+2][this.currentKeyY] = 1;
		this.selected = false;		
		this.currentKeyX = this.currentKeyX+2;
		return true;
		
	}
	public boolean eatSouth() {
		if(!this.selected) return false;
		if(!checkMovementsOf(this.currentKeyX,this.currentKeyY,3)){
			this.selected = false;
			return false;
		}
		this.grillaPrevia = (int[][])copyArray(this.grilla);
		this.grilla[this.currentKeyX][this.currentKeyY] = 0;
		this.grilla[this.currentKeyX][this.currentKeyY+1] = 0;
		this.grilla[this.currentKeyX][this.currentKeyY+2] = 1;
		this.selected = false;		
		this.currentKeyY = this.currentKeyY+2;
		return true;
	}
	
	public void select(){
		if(this.grilla[this.currentKeyX][this.currentKeyY]==1){
			this.selectedPrevia = this.selected; 
			this.selected = true;
		}
	}
	
	public void unSelect(){
		this.selectedPrevia = this.selected;
		this.selected = false;
	}
	
	public void toggleSelect(){
		
		if(this.selected){
			this.selectedPrevia = this.selected;
			this.selected = false;
		}else{
			if(this.grilla[this.currentKeyX][this.currentKeyY]==1){
				this.selectedPrevia = this.selected;
				this.selected = true;
			}
		}
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public boolean moveNorth(){
		if(this.currentKeyY>0 && this.grilla[currentKeyX][currentKeyY-1]!=-1){
			this.currentKeyYPrevia = this.currentKeyY;
			this.currentKeyY--;
			return true;
		}
		return false;
	}
	public boolean moveEast(){
		if(this.currentKeyX>0 && this.grilla[currentKeyX-1][currentKeyY]!=-1){
			this.currentKeyXPrevia = this.currentKeyX;
			this.currentKeyX--;
			return true;
		}
		return false;
	}
	public boolean moveWest(){
		if(this.currentKeyX<ANCHO-1 && this.grilla[currentKeyX+1][currentKeyY]!=-1){
			this.currentKeyXPrevia = this.currentKeyX;
			this.currentKeyX++;
			return true;
		}
		return false;
		
	}
	public boolean moveSouth(){
		if(this.currentKeyY<LARGO-1 && this.grilla[currentKeyX][currentKeyY+1]!=-1){
			this.currentKeyYPrevia = this.currentKeyY; 
			this.currentKeyY++;
			return true;
		}
		return false;
		
	}
	
	public boolean setCursor(int x, int y,boolean doSelect){
		if( (x <ANCHO && y <LARGO) && (this.grilla[x][y]!=-1) ){
			this.currentKeyYPrevia = this.currentKeyY;
			this.currentKeyXPrevia = this.currentKeyX;
			this.currentKeyX = x;
			this.currentKeyY = y;
			
			if(doSelect){
				if(this.selected && (this.currentKeyX == this.currentKeyXPrevia && this.currentKeyY == this.currentKeyYPrevia)){
					return false;
				}
				if(this.grilla[this.currentKeyX][this.currentKeyY]==1){
					this.selectedPrevia = this.selected;
					this.selected = true;
				}else{
					this.selectedPrevia = this.selected;
					this.selected = false;
					return false;
				}				
			}
			return true;
		}
		return false;
	}
	
	public boolean eatIn(int x, int y) {
		
		if(this.selected){
			if(this.currentKeyX == x){
				if(this.currentKeyY<y){
					return this.eatSouth();
				}else{
					return this.eatNorth();
				}
			}else if(this.currentKeyY == y){
				if(this.currentKeyX>x){
					return this.eatEast();
				}else{
					return this.eatWest();
				}
			}
		}
		this.unSelect();
		return false;
	}	
	
	public boolean centerCursor(){
		return this.setCursor((ANCHO/2), (LARGO/2), false);
	}
	
	public int getCountOfFichas(){
		int count = 0;
		for(int i=0;i<ANCHO;i++){
			for(int j=0;j<LARGO;j++){				
				if(this.grilla[i][j] == 1){
					count++;
				}
			}
		}
		pegcount = count;
		return count;
	}
	
	public int getScore(){
		if(this.pegcount == -1){
			this.getCountOfFichas();
		}		
		int restedpegs = SenkuGames.BOARD_TOTAL_PEGS[this.currentGameType] - this.pegcount; 
		this.score = (int) ((1.0/(this.pegcount*1.0))*SenkuGames.BOARD_SCORE[this.currentGameType]);
		this.score += restedpegs*SenkuPegs.getInstance().getPegs()[this.currentPegType].getScoreValue();
		return this.score;
	}
	
	/*
	 * just one level of undo
	 * return: true if could made undo el return false
	 * */
	public boolean undo(){
		
		if(this.grillaPrevia == null){
			return false;
		}else{
			this.grilla= (int[][])copyArray(this.grillaPrevia);	
			this.currentKeyX = this.currentKeyXPrevia;
			this.currentKeyY = this.currentKeyYPrevia;
			this.selected = this.selectedPrevia;
			this.grillaPrevia = null;
			return true;
		}
		
	}
	
	
 @Override 
 	public Object clone() throws CloneNotSupportedException {
	 	SenkuModel result = (SenkuModel)super.clone();
	 	result.grilla   	= copyArray(this.grilla);	
	 	result.currentKeyX  = this.currentKeyX;
	 	result.currentKeyY  = this.currentKeyY;
	 	result.selected 	= this.selected;
	 	
	 	return result;
   }

 	private int[][] copyArray(int[][] array){
 		int[][] aux = new int[array.length][array[0].length];
 		for(int i=0;i<array.length;i++)
 			aux[i] = (int[])array[i].clone();
 		return aux;
 	}
	
	/****************************/
	public Bundle saveState(Bundle map) {
		
		for(int i=0;i<ANCHO;i++){
			for(int j=0;j<LARGO;j++){
				 map.putInt("GRILLA_"+String.valueOf(i)+"_"+String.valueOf(j), Integer.valueOf(this.grilla[i][j]));
			}
		}
		map.putInt("CURRENT_X", Integer.valueOf(this.currentKeyX));
		map.putInt("CURRENT_Y", Integer.valueOf(this.currentKeyY));		
		return map;		
	}
	
	public void restoreState(Bundle savedState) {
        
			for(int i=0;i<ANCHO;i++){
				for(int j=0;j<LARGO;j++){					 
					 this.grilla[i][j] = savedState.getInt("GRILLA_"+String.valueOf(i)+"_"+String.valueOf(j));
				}
			}
			this.currentKeyX = savedState.getInt("CURRENT_X");
			this.currentKeyY = savedState.getInt("CURRENT_Y");
			this.selected = false;        
    }

	
}
