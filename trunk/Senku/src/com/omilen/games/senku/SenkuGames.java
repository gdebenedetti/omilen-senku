package com.omilen.games.senku;

public class SenkuGames {

	public static final int GAME_TYPES = 7;
	
	public static final int[][] GAME_BOARD_01 ={ { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 1, 0, -1, -1 }, 
											     {  0,  0, 1, 1, 1,  0,  0 }, 
											     {  0,  0, 0, 1, 0,  0,  0 }, 
											     {  0,  0, 0, 1, 0,  0,  0 }, 
											     { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 0, 0, -1, -1 } };

	public static final int[][] GAME_BOARD_02 ={ { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 1, 0, -1, -1 }, 
											     {  0,  0, 0, 1, 0,  0,  0 }, 
											     {  0,  1, 1, 1, 1,  1,  0 }, 
											     {  0,  0, 0, 1, 0,  0,  0 }, 
											     { -1, -1, 0, 1, 0, -1, -1 }, 
											     { -1, -1, 0, 0, 0, -1, -1 } };
	
	public static final int[][] GAME_BOARD_03 ={ { -1, -1, 1, 1, 1, -1, -1 }, 
											     { -1, -1, 1, 1, 1, -1, -1 }, 
											     {  0,  0, 1, 1, 1,  0,  0 }, 
											     {  0,  0, 1, 0, 1,  0,  0 }, 
											     {  0,  0, 0, 0, 0,  0,  0 }, 
											     { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 0, 0, -1, -1 } };

	public static final int[][] GAME_BOARD_04 ={ { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 1, 0, -1, -1 }, 
											     {  0,  0, 1, 1, 1,  0,  0 }, 
											     {  0,  1, 1, 1, 1,  1,  0 }, 
											     {  1,  1, 1, 1, 1,  1,  1 }, 
											     { -1, -1, 0, 0, 0, -1, -1 }, 
											     { -1, -1, 0, 0, 0, -1, -1 } };

	public static final int[][] GAME_BOARD_05 ={{ -1, -1, 0, 1, 0, -1, -1 }, 
										        { -1, -1, 1, 1, 1, -1, -1 }, 
										        {  0,  1, 1, 1, 1,  1,  0 }, 
										        {  1,  1, 1, 0, 1,  1,  1 }, 
										        {  0,  1, 1, 1, 1,  1,  0 }, 
										        { -1, -1, 1, 1, 1, -1, -1 }, 
										        { -1, -1, 0, 1, 0, -1, -1 } };

	public static final int[][] GAME_BOARD_06 ={{ -1, -1, 1, 1, 1, -1, -1 }, 
										        { -1, -1, 1, 1, 1, -1, -1 }, 
										        {  1,  1, 1, 1, 1,  1,  1 }, 
										        {  1,  1, 1, 0, 1,  1,  1 }, 
										        {  1,  1, 1, 1, 1,  1,  1 }, 
										        { -1, -1, 1, 1, 1, -1, -1 }, 
										        { -1, -1, 1, 1, 1, -1, -1 } };
	
	public static final int[][] GAME_BOARD_07 ={{ -1, -1, 1, 1, 1, -1, -1 }, 
										        { -1,  1, 1, 1, 1,  1, -1 }, 
										        {  1,  1, 1, 1, 1,  1,  1 }, 
										        {  1,  1, 1, 0, 1,  1,  1 }, 
										        {  1,  1, 1, 1, 1,  1,  1 }, 
										        { -1,  1, 1, 1, 1,  1, -1 }, 
										        { -1, -1, 1, 1, 1, -1, -1 } };
	
	public static final int[][][] GAMES ={GAME_BOARD_01, GAME_BOARD_02, GAME_BOARD_03, GAME_BOARD_04,
										  GAME_BOARD_05, GAME_BOARD_06, GAME_BOARD_07};
										
	
}
