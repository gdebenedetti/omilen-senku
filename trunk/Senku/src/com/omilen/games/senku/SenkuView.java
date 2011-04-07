package com.omilen.games.senku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.omilen.games.senku.score.ScoreUtil;


public class SenkuView extends SurfaceView implements SurfaceHolder.Callback {
    class SenkuThread extends Thread {
           	
        
        /*
         * State-tracking constants
         */
        public static final int STATE_END = 1;
        public static final int STATE_READY = 2;
        public static final int STATE_RUNNING = 3;
        public static final int STATE_PAUSE = 4;
        public static final int STATE_END2 = 5;
        public static final int STATE_LOADING = 6;
        public static final int MODE_TOUCH = 0;
        public static final int MODE_CURSOR = 1;
        public static final String KEY_FICHA_WIDTH  = "FICHA_WIDTH";
        public static final String KEY_FICHA_HEIGHT = "FICHA_HEIGHT";
        public static final String ALREADY_SET_SCORE = "ALREADY_SET_SCORE";
        public static final String SOUND_ON = "SOUND_ON";
        public static final String FINAL_COUNT = "FINAL_COUNT";
        public static final String BOARD_SELECTED = "BOARD_SELECTED";
        public static final String CONTROL_MODE = "CONTROL_MODE";
        public static final String PEG_SELECTED = "PEG_SELECTED";
        
        public static final int MAX_GRILL_LENGTH = 480;
//        public static final double PERCENT_OF_PEG = 0.17;
//        public static final double PERCENT_OF_CURSOR = 0.08;
        
        protected SenkuSoundPool sounds;        
        protected SenkuModel game = null;
              
        /*
         * Member (state) fields
         */
        /** The drawable to use as the background*/
        private Bitmap mBackgroundImage; 
        
        /** Message handler used by thread to interact with TextView */
        private Handler mHandler;
        
        /*Position in pixels of each board position, the board is symmetric so we calculate for only one dimension
         * this will be calculated when the screen changes its size (at start) */
        private int[] startPosition = new int[SenkuModel.ANCHO];
        private int[] startPositionCursor = new int[SenkuModel.ANCHO];

        private Bitmap[] mBoard = new Bitmap[3];
        private Bitmap[] mPegs = new Bitmap[SenkuPegs.NUMBER_OF_PEGS];
        private Bitmap[] mAuxPegs = new Bitmap[SenkuPegs.NUMBER_OF_PEGS]; //For The red Board
        private Bitmap mFondoCarteles;        
        private Bitmap[] mSombraFicha = new Bitmap[3];
        private Bitmap[] mCursor = new Bitmap[5];
        
        private Paint mLinePaint;
        private Paint mLinePaintBad;
        
        /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        private int mMode;
        private int controlMode = MODE_TOUCH; //could be Touch or cursor 

        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;
        private int cursorImage = 0;
        private boolean alreadySetScore = false; //flag to avoid score be writen twice        
        double mLastTime = System.currentTimeMillis();

        private int startX = 0;
        private int startY = 0;
        private int fingerCursorX = 0;
        private int fingerCursorY = 0;
        private int fingerCursorGrillaX = 0;
        private int fingerCursorGrillaY = 0;
         

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;
		private int finalCount;
		private int lengthGrilla = 0;
		private int lengthFicha = 0;
		private int lengthSombra = 0;
		private int corrimientoAlSeleccionar = 0;
		private float percent = 1;
		private int cellLength = 0;
		private int boardSelected = 0;
		private int pegSelected = 0;
		
        public SenkuThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
        	
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;
            this.game = new SenkuModel();
            Resources res = context.getResources();                   		  
            mBoard[0] = BitmapFactory.decodeResource(res,R.drawable.board);
            mBoard[1] = BitmapFactory.decodeResource(res,R.drawable.board_easy);
            mBoard[2] = BitmapFactory.decodeResource(res,R.drawable.board_euro);
            
            mPegs[0] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_plastico_roja);
            mAuxPegs[0] = BitmapFactory.decodeResource(res,R.drawable.ficha_plastico_verde);
            mPegs[1]    = BitmapFactory.decodeResource(res,R.drawable.ficha_madera);             
            mPegs[2] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_metalica);             
            mPegs[3]	= BitmapFactory.decodeResource(res,R.drawable.ficha_oro);             
            mPegs[4] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_gema_azul);             
            mPegs[5] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_gema_rosa);
            mAuxPegs[5] = BitmapFactory.decodeResource(res,R.drawable.ficha_gema_verde); 
            mPegs[6] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_diam_red);
            mAuxPegs[6] = BitmapFactory.decodeResource(res,R.drawable.ficha_diam_green);
            mPegs[7] 	= BitmapFactory.decodeResource(res,R.drawable.ficha_bola_ocho);
            
            
            
            
            mSombraFicha[0] = BitmapFactory.decodeResource(res,R.drawable.sombra_circular);
            mSombraFicha[1] = BitmapFactory.decodeResource(res,R.drawable.sombra_pentagonal);
            mSombraFicha[2] = BitmapFactory.decodeResource(res,R.drawable.sombra_diam);
            
            mCursor[0] =  BitmapFactory.decodeResource(res,R.drawable.bcursor_01);
            mCursor[1] =  BitmapFactory.decodeResource(res,R.drawable.bcursor_02);
            mCursor[2] =  BitmapFactory.decodeResource(res,R.drawable.bcursor_03);
                        
            mFondoCarteles = BitmapFactory.decodeResource(res,R.drawable.fondocarteles);
            mBackgroundImage = BitmapFactory.decodeResource(res,R.drawable.fondo);
          
            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setARGB(255, 0, 255, 0);

            mLinePaintBad = new Paint();
            mLinePaintBad.setAntiAlias(true);
            mLinePaintBad.setARGB(255, 120, 180, 0);
            
            //Start the Sounds
            sounds = new SenkuSoundPool(mContext);            
           
        }

        /**
         * Starts the game
         */
        public void doStart(int gameType) {
            synchronized (mSurfaceHolder) {
            	setState(STATE_RUNNING);
            	alreadySetScore = false;
            	if(gameType>=0 && gameType<SenkuGames.GAME_TYPES){
            		game.setCurrentGameType(gameType);
            	}
            	game.start();
            }
        }
        
        public void doStart() {
            synchronized (mSurfaceHolder) {
            	doStart(-1);
            }
        }
        
        public void turnOffSound() {
            synchronized (mSurfaceHolder) {
            	this.sounds.setSoundOn(false);
            }
        }
        
        public void turnOnSound() {
            synchronized (mSurfaceHolder) {
            	this.sounds.setSoundOn(true);
            }
        }
        
        public void setCurrentPeg(int pegtype){
        	synchronized (mSurfaceHolder) {
        		this.pegSelected = pegtype;
        	}
        }
        
        public void setCurrentGame(int currentGameType) {
            synchronized (mSurfaceHolder) {
            	switch (currentGameType) {
				case 0:
				case 1:
				case 2:
					this.boardSelected = 1;
					break;
				case 6:
					this.boardSelected = 2;
					break;
				default:
					this.boardSelected = 0;
					break;
				}
            	game.setCurrentGameType(currentGameType);
            	doStart();
            }
        }
                
        /**
         * Restores game state from the indicated Bundle. Typically called when
         * the Activity is being restored after having been previously
         * destroyed.
         * 
         * @param savedState Bundle containing the game state
         */
        public synchronized void restoreState(Bundle savedState) {
            
            synchronized (mSurfaceHolder) {
            	setState(STATE_PAUSE);               
                alreadySetScore = savedState.getBoolean(ALREADY_SET_SCORE);
                this.sounds.setSoundOn(savedState.getBoolean(SOUND_ON));        
                this.finalCount = savedState.getInt(FINAL_COUNT);
                this.boardSelected = savedState.getInt(BOARD_SELECTED);
                this.controlMode   = savedState.getInt(CONTROL_MODE);
                this.pegSelected   = savedState.getInt(PEG_SELECTED);
                game.restoreState(savedState);                
                setState(STATE_RUNNING);
            }
        }
        
        /**
         * Pauses
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
            }
        }
                
        /**
         * Resumes from a pause.
         */
        public void unpause() {            
            setState(STATE_RUNNING);
        }
        
        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING){
                        	checkStatus();
                        }
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }                
            }
        }

        private void checkStatus() {
			if(game.isEnded()){
				this.finalCount =  game.getCountOfFichas();
				setState(STATE_END);
			}			
		}

		/**
         * Dump game state to the provided Bundle. Typically called when the
         * Activity is being suspended.
         * 
         * @return Bundle with this view's state
         */
        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
                if (map != null) {
                    map.putBoolean(ALREADY_SET_SCORE, alreadySetScore);
                    map.putBoolean(SOUND_ON, this.sounds.isSoundOn());
                    map.putInt(FINAL_COUNT, this.finalCount);
                    map.putInt(BOARD_SELECTED,this.boardSelected);
                    map.putInt(CONTROL_MODE,this.controlMode);
                    map.putInt(PEG_SELECTED,this.pegSelected);                    
                    map = game.saveState(map);          
                }
            }
            return map;
        }
       
        
        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @see #setState(int, CharSequence)
         * @param mode one of the STATE_* constants
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @param mode one of the STATE_* constants
         * @param message string to add to screen or null
         */
        public void setState(int mode, CharSequence message) {
            /*
             * This method optionally can cause a text message to be displayed
             * to the user when the mode changes. Since the View that actually
             * renders that text is part of the main View hierarchy and not
             * owned by this thread, we can't touch the state of that View.
             * Instead we use a Message + Handler to relay commands to the main
             * thread, which updates the user-text View.
             */
            synchronized (mSurfaceHolder) {
            	if(mode == STATE_END && mMode == STATE_END2) return;
                mMode = mode;

                if (mMode == STATE_RUNNING) {
                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", "");
                    b.putInt("viz", View.INVISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                } else {
                    Resources res = mContext.getResources();
                    CharSequence str = "";
                    
                    if (mMode == STATE_END){
                    	if(finalCount == 1){
                    		str = res.getText(R.string.youwin);
                    	}else{
                    		str = res.getText(R.string.gameover);
                    	}
                    
	                    switch (finalCount) {
	                    	case 1: str = str+"\n"+res.getText(R.string.perfect); break;
	                    	case 2: str = str+"\n"+res.getText(R.string.veryGood); break;
	                    	case 3: str = str+"\n"+res.getText(R.string.good); break;
	                    	case 4: str = str+"\n"+res.getText(R.string.regular); break;
	                    	default: str = str+"\n"+res.getText(R.string.bad); break;
						}
	                    str = str+"\n"+res.getText(R.string.remaining_chips)+String.valueOf(finalCount);
	                    //Check if the Score is high and add to the list
	                    if(!alreadySetScore){
	                    	if(finalCount!=1)
	                    		sounds.playSound(SenkuSoundPool.SOUND_GAMEOVER);
	                    	else
	                    		sounds.playSound(SenkuSoundPool.SOUND_WIN);	                    	
		                    if(ScoreUtil.getInstance(mContext).updateScores(game.getCountOfFichas(), game.getScore(), game.getCurrentPegType(), game.getCurrentGameType())){
		                    	//the score was added
		                    	str = str+"\n"+res.getText(R.string.new_high_score)+": \n"+String.valueOf(game.getScore());
		                    }		                    
		                    alreadySetScore = true;
		                    String unlock = SenkuPegs.getInstance().unLockPegs(game.getCurrentGameType(), game.getCountOfFichas());
		                    if(!unlock.equals("")){
		                    	StoreProperties.getInstance().setProperty(unlock, "1");
		                    }
	                    }
	                    
                    }else if (mMode == STATE_LOADING){
                            str = res.getText(R.string.mode_loading);
                    }else if (mMode == STATE_PAUSE){
                        str = res.getText(R.string.mode_pause);
                    }else if (mMode == STATE_END2){
                    	//Hide the GAMEOVER letter
                         str = res.getText(R.string.mode_end2);
                    }
                
                    if (message != null) {
                        str = message + "\n" + str;
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", str.toString());
                    b.putInt("viz", View.VISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
            	int currentMode = this.mMode;
            	setState(STATE_LOADING);
            	lengthGrilla  = width;
            	if(width>height){
            		lengthGrilla=height;
            	}
            	if(lengthGrilla>=MAX_GRILL_LENGTH){
            		lengthGrilla = MAX_GRILL_LENGTH;
            		this.percent = 1;
            	}else{
            		this.percent = (float) (lengthGrilla*1.0 / MAX_GRILL_LENGTH*1.0 );	
            		
            	}
                this.startX = (width  - lengthGrilla) /2;
                this.startY = (height - lengthGrilla)/2;
                
                cellLength  = lengthGrilla/7;
                lengthFicha  = (int)Math.round(mPegs[0].getWidth()*percent);
                lengthSombra = (int)Math.round(mSombraFicha[0].getWidth()*percent);
                               
                mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage, width, height, true);
                if(lengthGrilla<MAX_GRILL_LENGTH){
	                mBoard[0] = Bitmap.createScaledBitmap(mBoard[0], lengthGrilla, lengthGrilla, true);
	                mBoard[1] = Bitmap.createScaledBitmap(mBoard[1], lengthGrilla, lengthGrilla, true);
	                mBoard[2] = Bitmap.createScaledBitmap(mBoard[2], lengthGrilla, lengthGrilla, true);
	                
	                for(int i=0;i<mPegs.length;i++){
	                	mPegs[i] = Bitmap.createScaledBitmap(mPegs[i], lengthFicha, lengthFicha, true);
	                }
	                mAuxPegs[0] = Bitmap.createScaledBitmap(mAuxPegs[0], lengthFicha, lengthFicha, true);
	                mAuxPegs[5] = Bitmap.createScaledBitmap(mAuxPegs[5], lengthFicha, lengthFicha, true);
	                mAuxPegs[6] = Bitmap.createScaledBitmap(mAuxPegs[6], lengthFicha, lengthFicha, true);
	                
	                for(int i=0;i<mSombraFicha.length;i++){
	                	mSombraFicha[i] = Bitmap.createScaledBitmap(mSombraFicha[i], lengthSombra, lengthSombra, true);
	                }
	                
	                int halfAnim = (int)Math.round((this.mCursor.length+1.0) /2.0);
	                for(int i=0; i<halfAnim;i++){
	                	mCursor[i] = Bitmap.createScaledBitmap(mCursor[i], (int)cellLength, (int)cellLength, true);
	                }
                }
                mAuxPegs[4] = mPegs[4];
                mAuxPegs[3] = mPegs[3];
                mAuxPegs[2] = mPegs[2];
                mAuxPegs[1] = mPegs[1];
                mAuxPegs[7] = mPegs[7];
                mFondoCarteles = Bitmap.createScaledBitmap(mFondoCarteles, (int)(lengthGrilla*0.90), (int)(lengthGrilla*0.90), true);
                mCursor[3] = mCursor[2];
                mCursor[4] = mCursor[1];               
                //Set the positions                                                
                long cellBorder   = Math.round((cellLength-lengthFicha)/2.0); //Math.round(cellLength *PERCENT_OF_PEG);
                //long shadowBorder = Math.round((cellLength-lengthSombra)/2.0);// Math.round(cellLength *PERCENT_OF_CURSOR);
                //long cursorBorder = Math.round((cellLength-lengthCursor)/2.0);
                startPosition[0] = (int) cellBorder;
                startPositionCursor[0] = 0;//(int) cursorBorder;
                for(int i=1;i<7;i++){
                	startPosition[i] = startPosition[i-1]+(int)cellLength;
                	startPositionCursor[i] = startPositionCursor[i-1]+(int)cellLength;
                }
           	 	
                corrimientoAlSeleccionar = (int)Math.round(20.0*percent);
                
                
                setState(currentMode);
                
            }
        }
     
        /**
         * Handles a key-down event.
         * 
         * @param keyCode the key that was pressed
         * @param msg the original event object
         * @return true
         */
        boolean doKeyDown(int keyCode, KeyEvent msg) {
        	
            synchronized (mSurfaceHolder) {
            	
            		boolean eatSuccess = false;
            		boolean moveSuccess = false;
            		boolean returnValue = false;
            	if(this.controlMode == MODE_TOUCH){
            			this.controlMode = MODE_CURSOR;
            			this.game.unSelect();
            			this.game.centerCursor();
            			return true;
            	}else if (mMode == STATE_RUNNING) {
                      if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_SPACE) {                    	  
                      	  this.game.toggleSelect();
                      	  returnValue = true;
                      } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                      	if(this.game.isSelected())
                      		eatSuccess  = game.eatEast();
                      	else
                      		moveSuccess = game.moveEast();
                      	returnValue = true;
                      }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                      	if(this.game.isSelected())
                      		eatSuccess  = game.eatSouth();
                      	else
                      		moveSuccess = game.moveSouth();
                      	returnValue = true;
                      }else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                      	if(this.game.isSelected())
                      		eatSuccess  = game.eatWest();
                      	else
                      		moveSuccess = game.moveWest();
                      	returnValue = true;
                      }else if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                      	if(this.game.isSelected())
                      		eatSuccess  = game.eatNorth();
                      	else
                      		moveSuccess = game.moveNorth();
                      	returnValue = true;
                      }
                      
                      if(eatSuccess){
                    	   sounds.playSound(SenkuSoundPool.SOUND_EAT);            	   
                       }else if(!moveSuccess){
                    	   if(returnValue){
           			        	   if(game.isSelected())
           			        		   sounds.playSound(SenkuSoundPool.SOUND_SELECT);
           			        	   else{
           			        		   try{
           			        			   sounds.playSound(SenkuSoundPool.SOUND_DONT);
           			        		   }catch(Exception e){
           			        			   
           			        		   }
           			        	   }			         
                    	   }
                       }
               return returnValue;
            }else if (mMode == STATE_PAUSE ) {
                unpause();
                return false;
            }else if(mMode == STATE_END){
            	setState(STATE_END2);
            	return true;
            }
        }
			return false;
		}

       public int getSombraIndex(){
    	   switch (this.pegSelected) {
				case 4:
				case 5: return 1;
				case 6: return 2;
				default: return 0;
			}
       }

        /**
         * Draws 
         */
        private void doDraw(Canvas canvas) {
          
        	boolean gameSelected = game.isSelected();
        	int currentX = game.getCurrentKeyX();
        	int currentY = game.getCurrentKeyY();
        	int xStart = startX+ startPosition[currentX];
        	int yStart = startY+ startPosition[currentY];
        	int xStartCursor = startX+ startPositionCursor[currentX];
        	int yStartCursor = startY+ startPositionCursor[currentY];
        	Bitmap[] mPegsTodraw = this.mPegs;
        	if(boardSelected == 2){
        		mPegsTodraw = this.mAuxPegs;
        	}
        	
        	canvas.drawBitmap(mBackgroundImage, 0, 0, null);
            canvas.drawBitmap(mBoard[boardSelected], startX, startY, null);
        	
        	if(controlMode == MODE_CURSOR && mMode == STATE_RUNNING && !gameSelected){
        		
	            	double elapsed = (System.currentTimeMillis() - mLastTime);
	            	if(elapsed>150){
	            		mLastTime = System.currentTimeMillis();            		
	            		this.cursorImage = (this.cursorImage+1) % this.mCursor.length;
	            		
	            	}
	            	canvas.drawBitmap(mCursor[this.cursorImage], xStartCursor, yStartCursor, null);
	            
        	
        	}
            
            for(int i =0;i<SenkuModel.ANCHO;i++){
            	for(int j=0;j<SenkuModel.LARGO;j++){
            		if(game.getCelda(i,j)==1 && (!gameSelected || (i!=currentX || j!= currentY))){
            			canvas.drawBitmap(mPegsTodraw[this.pegSelected], startX+startPosition[i], startY+startPosition[j], null);
            		}
            	}            	
            }            
            if(mMode == STATE_RUNNING){
	            
	            if(gameSelected){
	            	if(controlMode == MODE_CURSOR){
	            		canvas.drawBitmap(mSombraFicha[getSombraIndex()], xStart, yStart, null);
	            		canvas.drawBitmap(mPegsTodraw[this.pegSelected], xStart+this.corrimientoAlSeleccionar, yStart+this.corrimientoAlSeleccionar, null);
	            	}else{
	            		if(this.fingerCursorGrillaX >= 0 && this.fingerCursorGrillaX < SenkuModel.ANCHO 
								   && fingerCursorGrillaY>= 0 && fingerCursorGrillaY < SenkuModel.LARGO){
	            			canvas.drawBitmap(mSombraFicha[getSombraIndex()], startX+ startPosition[this.fingerCursorGrillaX], startY+ startPosition[this.fingerCursorGrillaY], null);							   
						}	            		
	            		canvas.drawBitmap(mPegsTodraw[this.pegSelected], this.fingerCursorX, this.fingerCursorY, null);	
	            	}
	            	
	            	
            	 
	            }
            }else if(mMode == STATE_END){
            	int xCartel = (lengthGrilla-mFondoCarteles.getWidth())/2 + startX;  
            	int yCartel = (lengthGrilla-mFondoCarteles.getHeight())/2 + startY;
            	canvas.drawBitmap(mFondoCarteles,xCartel, yCartel, null);            	
            }
        }

		public void doUndo() {
			 synchronized (mSurfaceHolder) {	            	
	            	if(!game.undo()){
	            		sounds.playSound(SenkuSoundPool.SOUND_DONT);
	            	}
	            }
			
		}
		
		public boolean doTouch(MotionEvent event) {
			   synchronized (mSurfaceHolder) {
				 
				   boolean eatSuccess = false;
				   boolean selectSuccess = false;
				   boolean dontSuccess = false;
				   boolean outOfGrilla = false;
				   				   
				   if(this.controlMode == MODE_CURSOR){
					   this.game.unSelect();
					   this.controlMode = MODE_TOUCH;
					   return true;
				   }else if (mMode == STATE_RUNNING) {
					   
					   int loc[] = new int[2];
					   getLocationOnScreen(loc);
					   this.fingerCursorX = ((int)event.getX())-loc[0];
					   this.fingerCursorY = ((int)event.getY())-loc[1];
	                   
					   this.fingerCursorGrillaX = ( this.fingerCursorX-this.startX)/cellLength;					   
					   this.fingerCursorGrillaY = ( this.fingerCursorY-this.startY)/cellLength;
					   if(this.fingerCursorGrillaX < 0 || this.fingerCursorGrillaX >=SenkuModel.ANCHO 
							   || fingerCursorGrillaY< 0 || fingerCursorGrillaY >= SenkuModel.LARGO){
						   outOfGrilla=true;
					   }
					   
					   if(event.getAction()==MotionEvent.ACTION_MOVE && this.game.selected){
						   return true;
						   
					   }else if(event.getAction()==MotionEvent.ACTION_UP){
						   if(!outOfGrilla && this.game.selected && this.game.getCelda(fingerCursorGrillaX, fingerCursorGrillaY)==0){						   
							   eatSuccess = this.game.eatIn(fingerCursorGrillaX, fingerCursorGrillaY);
							   dontSuccess=!eatSuccess;
						   }else{
							   this.game.unSelect();							   
							   dontSuccess = true;
						   }
					   }else if(event.getAction()==MotionEvent.ACTION_DOWN && (!this.game.selected || this.game.getCelda(fingerCursorGrillaX, fingerCursorGrillaY)==1)){
						   if(outOfGrilla){
							   selectSuccess = false;
							   dontSuccess = true;
						   }else{
							   selectSuccess = this.game.setCursor(fingerCursorGrillaX, fingerCursorGrillaY,true);
							   dontSuccess=!selectSuccess;
						   }
					   }
					   					   
		               if(eatSuccess){
		            	   sounds.playSound(SenkuSoundPool.SOUND_EAT);		            	   
		               }else if(selectSuccess){		            	   
					       sounds.playSound(SenkuSoundPool.SOUND_SELECT);
		               }else if(dontSuccess){
		        		   try{
		        			   sounds.playSound(SenkuSoundPool.SOUND_DONT);
		        		   }catch(Exception e){
		        			   
		        		   }
		               }
		               return (selectSuccess || eatSuccess);

	            }else if (mMode == STATE_PAUSE ) {
	                unpause();
	                return false;
	            }else if(mMode == STATE_END){
	            	setState(STATE_END2);
	            	return true;
	            }				
			   }
			   return false;
		}

		public Bitmap getPegImage(int pegIndex) {
			   synchronized (mSurfaceHolder) {
				   if(pegIndex<0 || pegIndex>=SenkuPegs.NUMBER_OF_PEGS)
					   return null;
				   return this.mPegs[pegIndex];
			   }		
		}
		
		public Bitmap[] getPegImages() {
			   synchronized (mSurfaceHolder) {				   
				   return this.mPegs;
			   }		
		}
		
		
    } //end thread

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;

    /** The thread that actually draws the animation */
    private SenkuThread thread;
    private Bundle savedState = null;
    
    public Bundle saveState(Bundle map) {
        this.savedState = thread.saveState(map);
        return savedState;
    }

    public SenkuView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);        
        // create thread only; it's started in surfaceCreated()
        thread = new SenkuThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });       
        setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public SenkuThread getThread() {
        return thread;
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }
    

	/**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return false;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    	 if (!hasWindowFocus) thread.pause();
    	 else thread.unpause();
    }
    
    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
    	/*JMR: I added the try except and all the code on the catch block.
    	 *     This solves the error of the application when pass to background and back
    	 * */
    	try{
    		thread.setRunning(true);
    		thread.start();
    	}catch(Exception e){    		 
    		 thread = new SenkuThread(holder, mContext, new Handler() {
    	            @Override
    	            public void handleMessage(Message m) {
    	                mStatusText.setVisibility(m.getData().getInt("viz"));
    	                mStatusText.setText(m.getData().getString("text"));
    	            }
    	        });
    		 setFocusable(true);
    		 thread.setRunning(true);
     		 thread.start();
     		 if(savedState != null)
     			 thread.restoreState(this.savedState);
     		 else
     			 thread.doStart();	
    		 
    	}
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode    	
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
	
}