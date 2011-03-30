package com.omilen.games.senku;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.omilen.games.senku.SenkuView.SenkuThread;
import com.omilen.games.senku.score.HighScoreListAdapter;
import com.omilen.games.senku.score.ScoreItem;
import com.omilen.games.senku.score.ScoreUtil;


public class Senku extends Activity  implements OnKeyListener {
	
	private static final int MENU_START   = 0;
	private static final int MENU_UNDO    = 1;
	private static final int MENU_SCORES  = 2;
	private static final int MENU_HELP  = 3;
	private static final int MENU_GAME_TYPE  = 6;
	private static final int MENU_PEG_TYPE  = 7;
	private static final int MENU_OPTIONS  = 8;
		
	/** A handle to the thread that's actually running the animation. */
    private SenkuThread mSenkuThread;

    /** A handle to the View in which the game is running. */
    private SenkuView mSenkuView;
    private StoreProperties instanceProp = null;
    private int selectedGame = 5;
    private int selectedPeg = 0;
       
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        // turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.senku_layout);
        
        // get handles to the LunarView from XML, and its LunarThread
        mSenkuView = (SenkuView) findViewById(R.id.senku);
        mSenkuThread = mSenkuView.getThread();

        // give the SenkuView a handle to the TextView used for messages
        mSenkuView.setTextView((TextView) findViewById(R.id.text));

        if (savedInstanceState == null) {
            // we were just launched: set up a new game
        	mSenkuThread.setState(SenkuThread.STATE_RUNNING);            
        } else {
            // we are being restored: resume a previous game
        	mSenkuThread.restoreState(savedInstanceState);           
        }
       
        //set the current-Options
        instanceProp = StoreProperties.getInstance(this);
        final String sound = instanceProp.getProperty("sound");
        if(sound.compareTo("1")==0){
    		mSenkuThread.turnOnSound();
        }else{    
        	mSenkuThread.turnOffSound();
        }
    	
        final String gameStr = instanceProp.getProperty("currentGame");
        
        if(gameStr!=null){
        	 mSenkuThread.doStart(Integer.valueOf(gameStr));
        }else{
        	 mSenkuThread.doStart();
        }
       
       
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);    	
        menu.add(0, MENU_START, 0, R.string.menu_start).setIcon(R.drawable.ic_menu_start);
        menu.add(0, MENU_UNDO, 0, R.string.menu_undo).setIcon(R.drawable.ic_menu_undo);
        menu.add(0, MENU_SCORES, 0, R.string.menu_score).setIcon(R.drawable.ic_menu_hiscores);
        menu.add(0, MENU_PEG_TYPE, 0, R.string.menu_peg_type).setIcon(R.drawable.ic_menu_peg);
        menu.add(0, MENU_OPTIONS, 0, R.string.menu_options).setIcon(R.drawable.ic_menu_options);
        menu.add(0, MENU_GAME_TYPE, 0, R.string.menu_game_type).setIcon(R.drawable.ic_menu_board);      
        
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	mSenkuThread = mSenkuView.getThread();
        switch (item.getItemId()) {
            case MENU_START:
                mSenkuThread.doStart();
                break;
            case MENU_UNDO: 
            	mSenkuThread.doUndo();
            	break;
            case MENU_SCORES:
            	showHighScoreListDialog();
            	break;
            case MENU_HELP:
            	showHelpDialog();
                break;           
            case MENU_GAME_TYPE: 
            	showGameTypeDialog();
            	break;
            case MENU_PEG_TYPE:
            	showPegTypeDialog();
            	break;
            case MENU_OPTIONS:
            	showOptionsDialog();
            	break;
        }

        return true;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle        
    	super.onSaveInstanceState(outState);
    	mSenkuView.saveState(outState);       
    }
        
    @Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {	
		return false;
	}
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		return mSenkuThread.doTouch(event);
	}
        
	/********* CLASSES PRIVADAS *************/
    private class ScoresCancelListener implements OnCancelListener {
        public void onCancel(DialogInterface dialog) {
                    
        }
    }
    
    private class ConfirmDeleteListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            if (whichButton == AlertDialog.BUTTON1) {
                ScoreUtil.getInstance(Senku.this).clearScores();                
            }
            showHighScoreListDialog();
        }
    }
    private class ScoresListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            switch (whichButton) {
                case AlertDialog.BUTTON1: {
                    showConfirmDeleteDialog();
                    return;
                }
                case AlertDialog.BUTTON2: {       
                    return;
                }
            }
        }
    }
    
    private class PegDialogListener implements View.OnClickListener {
    
    	protected ImageView imageView = null;
    	
    	public PegDialogListener(ImageView image){
    		super();
    		this.imageView = image;    		
    	}
    	
    	@Override
		public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.radioPeg0:
			selectedPeg = 0;
//			instanceProp.setProperty("currentPeg", "00-"+SenkuPegs.getInstance().getPegs()[0].getCodeName());
//			mSenkuThread.setCurrentPeg(0);
			break;
		case R.id.radioPeg1:
			selectedPeg = 1;
//			instanceProp.setProperty("currentPeg", "01-"+SenkuPegs.getInstance().getPegs()[1].getCodeName());
//			mSenkuThread.setCurrentPeg(1);			
			break;
		case R.id.radioPeg2:
			selectedPeg = 2;
//			instanceProp.setProperty("currentPeg", "02-"+SenkuPegs.getInstance().getPegs()[2].getCodeName());
//			mSenkuThread.setCurrentPeg(2);				
			break;
		case R.id.radioPeg3:
			selectedPeg = 3;
//			instanceProp.setProperty("currentPeg", "03-"+SenkuPegs.getInstance().getPegs()[3].getCodeName());
//			mSenkuThread.setCurrentPeg(3);				
			break;
		case R.id.radioPeg4:
			selectedPeg = 4;
//			instanceProp.setProperty("currentPeg", "04-"+SenkuPegs.getInstance().getPegs()[4].getCodeName());
//			mSenkuThread.setCurrentPeg(4);				
			break;
		case R.id.radioPeg5:
			selectedPeg = 5;
//			instanceProp.setProperty("currentPeg", "05-"+SenkuPegs.getInstance().getPegs()[5].getCodeName());
//			mSenkuThread.setCurrentPeg(5);				
			break;
		case R.id.radioPeg6:
			selectedPeg = 6;
//			instanceProp.setProperty("currentPeg", "06-"+SenkuPegs.getInstance().getPegs()[6].getCodeName());
//			mSenkuThread.setCurrentPeg(6);				
			break;		
		case R.id.radioPeg7:
			selectedPeg = 7;
//			instanceProp.setProperty("currentPeg", "07-"+SenkuPegs.getInstance().getPegs()[7].getCodeName());
//			mSenkuThread.setCurrentPeg(7);				
			break;			
		default:
			break;
		}
    	imageView.setImageBitmap(Senku.this.mSenkuThread.getPegImage(selectedPeg));
    	mSenkuThread.setCurrentPeg(selectedPeg);
    	
    	}
    }
    
    private class BoardDialogListener implements View.OnClickListener {

    	protected ImageView image = null;
    	    	
    	public BoardDialogListener(ImageView image){
    		super();
    		this.image = image;    		
    	}
    	
    	@Override
		public void onClick(View v) {
			
			mSenkuThread = mSenkuView.getThread();
	        	        
			switch (v.getId()) {
			case R.id.ButtonBoard00:
				//instanceProp.setProperty("currentGame", "00");
				selectedGame=0;
				mSenkuThread.setCurrentGame(0);
				image.setImageResource(R.drawable.ic_menu_board_00);
				break;
			case R.id.ButtonBoard01:
				//instanceProp.setProperty("currentGame", "01");
				selectedGame=1;
				mSenkuThread.setCurrentGame(1);
				image.setImageResource(R.drawable.ic_menu_board_01);
				break;
			case R.id.ButtonBoard02:
				//instanceProp.setProperty("currentGame", "02");
				selectedGame=2;
				mSenkuThread.setCurrentGame(2);
				image.setImageResource(R.drawable.ic_menu_board_02);
				break;
			case R.id.ButtonBoard03:
				//instanceProp.setProperty("currentGame", "03");
				selectedGame=3;
				mSenkuThread.setCurrentGame(3);
				image.setImageResource(R.drawable.ic_menu_board_03);
				break;
			case R.id.ButtonBoard04:
				//instanceProp.setProperty("currentGame", "04");
				selectedGame=4;
				mSenkuThread.setCurrentGame(4);
				image.setImageResource(R.drawable.ic_menu_board_04);
				break;
			case R.id.ButtonBoard05:
				//instanceProp.setProperty("currentGame", "05");
				selectedGame=5;
				mSenkuThread.setCurrentGame(5);
				image.setImageResource(R.drawable.ic_menu_board);
				break;
			case R.id.ButtonBoard06:
				//instanceProp.setProperty("currentGame", "06");
				selectedGame=6;
				mSenkuThread.setCurrentGame(6);
				image.setImageResource(R.drawable.ic_menu_board_06);
				break;		
			default:
				break;
			}

		}
    	
    }
   
    private class HelpListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            switch (whichButton) {
                case AlertDialog.BUTTON1: {                  
                    return;
                }
            }
        }
    }
    
    private class BoardCloseListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            switch (whichButton) {
                case AlertDialog.BUTTON2: {
                	instanceProp.setProperty("currentGame", String.valueOf(selectedGame));
                    return;
                }
            }
        }
    }
    
    private class PegDialogCloseListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            switch (whichButton) {
                case AlertDialog.BUTTON2: {                	
                	instanceProp.setProperty("currentPeg", String.valueOf(selectedPeg)+"-"+SenkuPegs.getInstance().getPegs()[selectedPeg].getCodeName());
                    return;
                }
            }
        }
    }
    /**********************/
    public void showHighScoreListDialog() {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.high_score_list, null);
        ListView listView = (ListView) layout.findViewById(R.id.score_list);
        ScoresListener listener = new ScoresListener();
        List<ScoreItem> scoreAux = ScoreUtil.getInstance(this).getAllScores();             
        Collections.sort(scoreAux);
        listView.setAdapter(new HighScoreListAdapter(this, scoreAux));
        /*********************************/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle(R.string.scores_title);
        builder.setCancelable(true);
        builder.setView(layout);
        
        builder.setPositiveButton(R.string.menu_clear, listener);
        builder.setNegativeButton(R.string.dialog_close, listener);
        builder.setOnCancelListener(new ScoresCancelListener());
        builder.show();        
    }
    
    public void showPegTypeDialog(){
    	
    	PegDialogCloseListener listener = new PegDialogCloseListener();    
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_peg_type);
        builder.setIcon(R.drawable.ic_menu_peg);
        builder.setCancelable(true);        
        builder.setNegativeButton(R.string.dialog_close, listener);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.peg_layout, null);
        builder.setView(layout);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.peg_image);
        PegDialogListener pegListener = new PegDialogListener(imageView);
                       
        final RadioButton buttonBoard00 = (RadioButton) layout.findViewById(R.id.radioPeg0);
        final RadioButton buttonBoard01 = (RadioButton) layout.findViewById(R.id.radioPeg1);
        final RadioButton buttonBoard02 = (RadioButton) layout.findViewById(R.id.radioPeg2);
        final RadioButton buttonBoard03 = (RadioButton) layout.findViewById(R.id.radioPeg3);
        final RadioButton buttonBoard04 = (RadioButton) layout.findViewById(R.id.radioPeg4);
        final RadioButton buttonBoard05 = (RadioButton) layout.findViewById(R.id.radioPeg5);
        final RadioButton buttonBoard06 = (RadioButton) layout.findViewById(R.id.radioPeg6);
        final RadioButton buttonBoard07 = (RadioButton) layout.findViewById(R.id.radioPeg7);        
        buttonBoard00.setOnClickListener(pegListener);
        buttonBoard01.setOnClickListener(pegListener);
        buttonBoard02.setOnClickListener(pegListener);
        buttonBoard03.setOnClickListener(pegListener);
        buttonBoard04.setOnClickListener(pegListener);
        buttonBoard05.setOnClickListener(pegListener);
        buttonBoard06.setOnClickListener(pegListener);
        buttonBoard07.setOnClickListener(pegListener);
       
        String selected = instanceProp.getProperty("currentPeg");
        if(selected == null){
        	selected = "00";
        }else{
        	try{
	        	String aux[] = selected.split("-");
	        	selected = aux[0]; 
	        	if(!aux[1].equals(SenkuPegs.getInstance().getPegs()[Integer.parseInt(selected)].getCodeName() )){
	        		selected = "00";
	        	}
        	}catch (Exception e) {
        		selected = "00";
			}
        }
        
        int selectedPegAux = Integer.parseInt(selected);
        imageView.setImageBitmap(Senku.this.mSenkuThread.getPegImage(selectedPegAux));
        switch (selectedPegAux) {
		case 0:	buttonBoard00.setChecked(true);		
			break;
		case 1:	buttonBoard01.setChecked(true);		
		    break;
		case 2:	buttonBoard02.setChecked(true);		
			break;
		case 3:	buttonBoard03.setChecked(true);		
			break;
		case 4:	buttonBoard04.setChecked(true);		
			break;
		case 5:	buttonBoard05.setChecked(true);		
			break;  
		case 6:	buttonBoard06.setChecked(true);		
			break;
		default:
			break;
		}
        builder.show(); 
    }
    
    public void showGameTypeDialog(){
    	
    	BoardCloseListener listener = new BoardCloseListener();    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_game_type);       
        builder.setCancelable(true);        
        builder.setNegativeButton(R.string.dialog_close, listener);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.board_layout, null);
        builder.setView(layout);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.board_image);
        BoardDialogListener boardListener = new BoardDialogListener(imageView);
                
        final RadioButton buttonBoard00 = (RadioButton) layout.findViewById(R.id.ButtonBoard00);
        final RadioButton buttonBoard01 = (RadioButton) layout.findViewById(R.id.ButtonBoard01);
        final RadioButton buttonBoard02 = (RadioButton) layout.findViewById(R.id.ButtonBoard02);
        final RadioButton buttonBoard03 = (RadioButton) layout.findViewById(R.id.ButtonBoard03);
        final RadioButton buttonBoard04 = (RadioButton) layout.findViewById(R.id.ButtonBoard04);
        final RadioButton buttonBoard05 = (RadioButton) layout.findViewById(R.id.ButtonBoard05);
        final RadioButton buttonBoard06 = (RadioButton) layout.findViewById(R.id.ButtonBoard06);        
        
        buttonBoard00.setOnClickListener(boardListener);
        buttonBoard01.setOnClickListener(boardListener);
        buttonBoard02.setOnClickListener(boardListener);
        buttonBoard03.setOnClickListener(boardListener);
        buttonBoard04.setOnClickListener(boardListener);
        buttonBoard05.setOnClickListener(boardListener);
        buttonBoard06.setOnClickListener(boardListener);
        
        String selected = instanceProp.getProperty("currentGame");
        if(selected == null){
        	selected = "05";
        }
        switch (Integer.parseInt(selected)) {
		case 0:	buttonBoard00.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_00);
			break;
		case 1:	buttonBoard01.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_01);
		    break;
		case 2:	buttonBoard02.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_02);
			break;
		case 3:	buttonBoard03.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_03);
			break;
		case 4:	buttonBoard04.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_04);
			break;
		case 5:	buttonBoard05.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board);
			break;  
		case 6:	buttonBoard06.setChecked(true);
				imageView.setImageResource(R.drawable.ic_menu_board_06);
			break;
		default:
			break;
		}
        builder.show(); 
    }
    
    public void showOptionsDialog() {
    	        
    	HelpListener listener = new HelpListener();    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_options);
        builder.setIcon(R.drawable.ic_menu_options);
        builder.setCancelable(true);        
        builder.setNegativeButton(R.string.dialog_close, listener);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.options_layout, null);
        builder.setView(layout);
        
        String sound = StoreProperties.getInstance(this).getProperty("sound");
        String facebook = StoreProperties.getInstance(this).getProperty("facebook");
        
        final CheckBox checkSound = (CheckBox)  layout.findViewById(R.id.CheckBoxSound);
        checkSound.setChecked(sound.equals("1"));
        
        final CheckBox checkFacebook = (CheckBox)  layout.findViewById(R.id.CheckBoxFacebook);
        checkFacebook.setChecked(facebook.equals("1"));
        
        final ImageView imageh = (ImageView) layout.findViewById(R.id.ImageViewHelp);
        imageh.setImageResource(R.drawable.ic_menu_help);
        
        final ImageView images = (ImageView) layout.findViewById(R.id.ImageViewSound);
        images.setImageResource(R.drawable.ic_menu_sound);
        
        final ImageView imagef = (ImageView) layout.findViewById(R.id.ImageViewFacebook);
        imagef.setImageResource(R.drawable.ic_menu_facebook);
        
        final Button button2 = (Button) layout.findViewById(R.id.ButtonHelp);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 showHelpDialog();
            }
        });
        checkFacebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				if(checkFacebook.isChecked()){
					instanceProp.setProperty("facebook", "1");					
				}else{
					instanceProp.setProperty("facebook", "0");					
				}
			}
        });
        
        checkSound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(checkSound.isChecked()){
					instanceProp.setProperty("sound", "1");
					mSenkuThread.turnOnSound();
				}else{
					instanceProp.setProperty("sound", "0");
					mSenkuThread.turnOffSound();
				}
				
				
			}
        });
        
        builder.show();
        
    }
    
    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.dialog_yes, new ConfirmDeleteListener());
        builder.setNegativeButton(R.string.dialog_no, new ConfirmDeleteListener());
        builder.setMessage(R.string.delete_dialog_msg);

        builder.show();
    }
    private void showHelpDialog() {
    	HelpListener listener = new HelpListener();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_dialog_title);
        builder.setIcon(R.drawable.ic_menu_help);
        builder.setCancelable(true);        
        builder.setNegativeButton(R.string.dialog_close, listener);
        builder.setMessage(R.string.help_dialog_msg);

        builder.show();
    }
	
}
