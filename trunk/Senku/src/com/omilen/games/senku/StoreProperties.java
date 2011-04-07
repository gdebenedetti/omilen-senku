package com.omilen.games.senku;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public class StoreProperties {
    private static final String PROPERTIES_FILE = "properties_senku.txt";
  
    private static StoreProperties mInstance;
    private static Map<String,String> mproperties;
    private static Context mContext = null;
    
    private StoreProperties() {
    	mproperties = new HashMap<String, String>();
        loadProperties();
    }
    
    public static StoreProperties getInstance() {        
        if (mInstance == null) {
            mInstance = new StoreProperties();
        }        
        return mInstance;
    }
    
    public static void setContext(Context context){
    	mContext = context;
    }
        
    private void loadProperties() {
    	
    	File f = new File(PROPERTIES_FILE);
    	if(!f.exists()){    		
    		createPropertiesFile();
    		return;
    	}
      
        FileInputStream fin = null;
        try {
            fin = mContext.openFileInput(PROPERTIES_FILE);
            DataInputStream in = new DataInputStream(fin);
            String line = in.readLine();
            while(line!=null){            	
            	String[] arrayAux = line.split("=");
            	mproperties.put(arrayAux[0], arrayAux[1]);
                line = in.readLine();
            }
        } catch(FileNotFoundException fnfe) {
        	createPropertiesFile();
        } catch(IOException ioe) {
        } finally {
            if (fin != null) {
                try {
                    fin.close();                    
                    try{
                    	f = new File(PROPERTIES_FILE);
                    	f.delete();
                    }catch (Exception e) {
                    		
					}
                } catch(IOException ioe) {       
                }
            }
        }
    }
    
    private void createPropertiesFile() {
        FileOutputStream fout = null;
        try {
            fout = mContext.openFileOutput(PROPERTIES_FILE, Context.MODE_PRIVATE);
            DataOutputStream out = new DataOutputStream(fout);
            out.writeChars("sound=1\n");
            out.writeChars("facebook=1\n");
            mproperties.put("sound", "1");
            mproperties.put("facebook", "-1"); //not defined
                
        } catch(IOException ioe) {
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch(IOException ioe) {
                }
            }
        }
    }
   
    public String getProperty(String key) {
        return mproperties.get(key);
    }
        
    public boolean setProperty(String key, String value) {
    
    	mproperties.put(key, value);
           
        FileOutputStream fout = null;
	    try {
	        fout = mContext.openFileOutput(PROPERTIES_FILE, Context.MODE_PRIVATE);
	        DataOutputStream out = new DataOutputStream(fout);
	        Iterator<String> it =  mproperties.keySet().iterator();	        
	        while(it.hasNext()){
	        	String aux = it.next();
	        	out.writeChars(aux+"="+mproperties.get(aux)+"\n");
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
}
