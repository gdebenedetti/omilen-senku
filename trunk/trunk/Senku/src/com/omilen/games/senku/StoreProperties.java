/*!
 * Copyright 2010-2011, Omilen IT Solutions
 * licensed under Apache Version 2.0, http://www.apache.org/licenses/
 * http://www.omilenitsolutions.com/
 * Author: Juan Manuel Rodríguez
 */
package com.omilen.games.senku;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public class StoreProperties {
    private static final String PROPERTIES_FILE = "properties_senku.txt";
  
    private static StoreProperties instance = null;
    private static Map<String,String> mproperties;
    private static Context mContext = null;
    
    private StoreProperties() {
    	mproperties = new HashMap<String, String>();
        loadProperties();
    }
    
    public static StoreProperties getInstance() {        
        if (instance == null) {
        	instance = new StoreProperties();
        }        
        return instance;
    }
    
    public static void setContext(Context context){
    	mContext = context;
    }
            
    private void loadProperties() {
    	
    	if(!fileExists(PROPERTIES_FILE)){
    		createPropertiesFile();
    		return;
    	}

//        FileInputStream fin = null;
//        DataInputStream in = null;
//        try {
//            fin = mContext.openFileInput(PROPERTIES_FILE);            
//            in = new DataInputStream(fin);            
//            String line = in.readLine();
//            while(line!=null){            	
//            	String[] arrayAux = line.split("=");
//            	mproperties.put(arrayAux[0], arrayAux[1]);
//                line = in.readLine();
//            }
//        } catch(FileNotFoundException fnfe) {
//        	createPropertiesFile();
//        } catch(IOException ioe) {
//        } finally {
//            if (fin != null) {
//                try {
//                    fin.close();                    
//                    in.close();
//                } catch(IOException ioe) {       
//                }
//            }
//        }
    	 InputStream instream = null;
    	 InputStreamReader inputreader = null;
	     BufferedReader buffreader = null; 
		try {
			instream = mContext.openFileInput(PROPERTIES_FILE);
			// if file the available for reading
    	    if (instream!=null) {
    	      // prepare the file for reading
    	      inputreader = new InputStreamReader(instream);
    	      buffreader = new BufferedReader(inputreader);
    	 
    	      String line;    	      
			  line = buffreader.readLine();			  
    	      // read every line of the file into the line-variable, on line at the time
    	      while (line != null) {
    	        // do something with the settings from the file
    	    		String[] arrayAux = line.split("=");
                	mproperties.put(arrayAux[0], arrayAux[1]);
                	line = buffreader.readLine();
    	      }
    	    }
		} catch (FileNotFoundException e) {
			createPropertiesFile();
		} catch (IOException e) {
			createPropertiesFile();
		}finally{
			try {
				if (buffreader != null) {
					buffreader.close();
				}
				if (inputreader != null) {
					inputreader.close();
				}
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {

			}
	    	 
		}
    	 
    	    
    	
    }
    
    private void createPropertiesFile() {
    	mproperties.put("sound", "1");
        mproperties.put("facebook", "-1");
        saveProperties();
    }
   
    public String getProperty(String key) {
        return mproperties.get(key);
    }
    
    public boolean saveProperties(){
    	
    	FileOutputStream fout = null;
    	DataOutputStream out = null;
	    try {
	        fout = mContext.openFileOutput(PROPERTIES_FILE, Context.MODE_PRIVATE);
	        out = new DataOutputStream(fout);
	        Iterator<String> it =  mproperties.keySet().iterator();	        
	        while(it.hasNext()){
	        	String aux = it.next();
	        	out.writeBytes(aux+"="+mproperties.get(aux)+"\n");	        	
	        }	
	    } catch(IOException ioe) {
	    } finally {
	        if(fout != null) {
	            try {
	            	out.close();
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
        
    public boolean setProperty(String key, String value) {    
    	mproperties.put(key, value);        
	    return saveProperties();
        
    }    
}
