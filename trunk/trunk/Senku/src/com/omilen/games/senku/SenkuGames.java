/*!
 * Copyright 2010-2011, Omilen IT Solutions
 * licensed under Apache Version 2.0, http://www.apache.org/licenses/
 * http://www.omilenitsolutions.com/
 * Author: Juan Manuel Rodríguez
 */
package com.omilen.games.senku;

public class SenkuGames {

	public static final int GAME_TYPES = 7;
	public static final int[] BOARD_SCORE = {100,200,300,400,500,800,1000};
	public static final int[] BOARD_TOTAL_PEGS = {6,9,11,16,24,32,36};
	
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
