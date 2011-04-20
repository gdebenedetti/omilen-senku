/*!
 * Copyright 2010-2011, Omilen IT Solutions
 * licensed under Apache Version 2.0, http://www.apache.org/licenses/
 * http://www.omilenitsolutions.com/
 * Author: Juan Manuel Rodríguez
 */
package com.omilen.games.senku.score;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

public class ScoreUtil {
    private static final String SCORE_FILE = "scores_senku.txt";
    public static final int MAX_SCORE_ENTRIES = 6; //Max 6 users

    private static ScoreUtil mInstance;
    private static List<ScoreItem> mScores;
    private static Context mContext;
    
    private ScoreUtil() {
    	mScores = new ArrayList<ScoreItem>();
        loadScores();
    }
    
    public static ScoreUtil getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new ScoreUtil();
        }
        
        return mInstance;
    }
    
    public void clearScores() {
        createScoreFile();
        mScores.clear(); 
        loadScores();
    }
    
    private void loadScores() {
    	    	
    	if(!fileExists(SCORE_FILE)){    		
    		createScoreFile();
    		return;
    	}
      
        FileInputStream fin = null;
        DataInputStream in = null;
        try {
            fin = mContext.openFileInput(SCORE_FILE);
            in = new DataInputStream(fin);
            for (int i = 0; i < MAX_SCORE_ENTRIES; i++) {
            	String line = in.readLine();
            	String[] arrayAux = line.split(";");
            	ScoreItem si = new ScoreItem(arrayAux[0], Integer.parseInt(arrayAux[1]),
            			Integer.parseInt(arrayAux[2]),Integer.parseInt(arrayAux[3]),
            			Integer.parseInt(arrayAux[4])
            	);
                mScores.add(si);
            }            
        } catch(FileNotFoundException fnfe) {
            createScoreFile();
        } catch(IOException ioe) {
        } finally {
            if (fin != null) {                
                    try {						
						in.close();
						fin.close();
					} catch (IOException e) {					
						e.printStackTrace();
					}
            }
        }
    }
    
    private void createScoreFile() {
        FileOutputStream fout = null;
        DataOutputStream out = null;
        int len        = MAX_SCORE_ENTRIES;
        try {
            fout = mContext.openFileOutput(SCORE_FILE, Context.MODE_PRIVATE);
            out = new DataOutputStream(fout);
            for (int i = 0; i < len; i++) {
                out.writeChars("no date yet;26;32;0;0\n");
                ScoreItem si = new ScoreItem("no date yet", 26,32,0,0);
                mScores.add(si);                
            }
        } catch(IOException ioe) {
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                    out.close();
                } catch(IOException ioe) {
                }
            }
        }
    }
    
    public List<ScoreItem> getAllScores() {
        return mScores;
    }
        
    public boolean updateScores(int pegs, int score,int pegtype,int board) {
    	
    	Collections.sort(mScores);
    	ScoreItem a = mScores.get(mScores.size()-1);    	
    	if(a.getScore()>score) return false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new java.util.Date();
        String datetime = dateFormat.format(date);
    
    	mScores.add(new ScoreItem(datetime,pegs,score,board,pegtype));
    	Collections.sort(mScores);
    	mScores.remove(mScores.size()-1);   	
           
        FileOutputStream fout = null;
	    try {
	        fout = mContext.openFileOutput(SCORE_FILE, Context.MODE_PRIVATE);
	        DataOutputStream out = new DataOutputStream(fout);
	        Iterator<ScoreItem> it =  mScores.iterator();
	        ScoreItem aux;
	        while(it.hasNext()){
	        	aux = it.next();	        	 
	        	out.writeChars(aux.getDate()+";"+
	        			       String.valueOf(aux.getPegs())+";"+
	        			       String.valueOf(aux.getScore())+";"+
	        			       String.valueOf(aux.getGameNum())+";"+
	        			       String.valueOf(aux.getPegNum())+"\n");
	        }	
	    } catch(IOException ioe) {
	    } finally {
	        if(fout != null) {
	            try {
	                fout.close();
	            } catch(IOException ioe) {
	            }
	        }
	    }
	    return true;
        
    }
    
    public static boolean fileExists(String file) {

        String[] filenames = mContext.fileList();
        for (String name : filenames) {
          if (name.equals(file)) {
            return true;
          }
        }

        return false;
    }
}
