package com.omilen.games.senku;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	private static final int MENU_SOUND  = 4;
	private static final int MENU_SOUND_OFF  = 5;
	private static final int MENU_GAME_TYPE  = 6;
	private static final int MENU_PEG_TYPE  = 7;
	private static final int MENU_OPTIONS  = 8;
	private static final int MENU_FACEBOOK  = 9;
		
	/** A handle to the thread that's actually running the animation. */
    private SenkuThread mSenkuThread;

    /** A handle to the View in which the game is running. */
    private SenkuView mSenkuView;
	
       
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
       
        mSenkuThread.doStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);    	
        menu.add(0, MENU_START, 0, R.string.menu_start).setIcon(R.drawable.start);
        menu.add(0, MENU_UNDO, 0, R.string.menu_undo).setIcon(R.drawable.undo);
        menu.add(0, MENU_SCORES, 0, R.string.menu_score).setIcon(R.drawable.hiscores);
//      menu.add(0, MENU_OPTIONS, 0, R.string.menu_options).setIcon(R.drawable.options);        
//      menu.add(0, MENU_GAME_TYPE, 0, R.string.menu_game_type).setIcon(R.drawable.board);
//      menu.add(0, MENU_GAME_TYPE, 0, R.string.menu_game_type).setIcon(R.drawable.soundoff);
        SubMenu subMenuPegType  = menu.addSubMenu(0, MENU_PEG_TYPE, 0, R.string.menu_peg_type).setIcon(R.drawable.peg);
        SubMenu subMenuGameType = menu.addSubMenu(0, MENU_GAME_TYPE, 0, R.string.menu_game_type).setIcon(R.drawable.board);
        SubMenu subMenuoptions  = menu.addSubMenu(0, MENU_OPTIONS, 0, R.string.menu_options).setIcon(R.drawable.options);
        subMenuGameType.add(1, 10, 0, "Cruz (piece of cake)");
        subMenuGameType.add(1, 11, 1, "Mas (very easy)");
        subMenuGameType.add(1, 12, 2, "Hogar (easy)");
        subMenuGameType.add(1, 13, 3, "Piramide (not so easy)");
        subMenuGameType.add(1, 14, 4, "Diamante (medium)");
        subMenuGameType.add(1, 15, 5, "Normal (difficult)");
        subMenuGameType.add(1, 16, 6, "Muerte Europea (Very hard)");
        
        subMenuPegType.add(2, 17, 0, "plastic");
        subMenuPegType.add(2, 18, 1, "wood");
        subMenuPegType.add(2, 19, 2, "silver");
        subMenuPegType.add(2, 20, 3, "gold");
        subMenuPegType.add(2, 21, 4, "emerald");
        subMenuPegType.add(2, 22, 5, "diamond");
        subMenuPegType.add(2, 23, 6, "eigth ball");
        
        subMenuoptions.add(3, MENU_HELP,     0, R.string.menu_help).setIcon(R.drawable.help);
        subMenuoptions.add(3, MENU_SOUND,    1, R.string.menu_sound).setIcon(R.drawable.sound);
        subMenuoptions.add(3, MENU_FACEBOOK, 2, R.string.menu_facebook).setIcon(R.drawable.facebook);
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
            case MENU_SOUND: 
            	mSenkuThread.turnOnSound();
            	break;
            case MENU_SOUND_OFF:
            	mSenkuThread.turnOffSound();
            	break;
            case MENU_GAME_TYPE: 
            	mSenkuThread.turnOnSound();
            	break;
            case MENU_PEG_TYPE:
            	mSenkuThread.turnOffSound();
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
    
    private class HelpListener implements OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton ) {
            switch (whichButton) {
                case AlertDialog.BUTTON1: {                  
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
        builder.setIcon(R.drawable.help);
        builder.setCancelable(true);        
        builder.setNegativeButton(R.string.dialog_close, listener);
        builder.setMessage(R.string.help_dialog_msg);

        builder.show();
    }
	
}