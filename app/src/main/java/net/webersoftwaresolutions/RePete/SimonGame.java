package net.webersoftwaresolutions.RePete;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Random;

import static android.util.Log.d;
import static net.webersoftwaresolutions.RePete.R.id.play;

/**
 * Created by jaWeber on 11/2/17.
 *
 *  https://stackoverflow.com/questions/25807205/findviewbyid-in-non-activity-class
 *
 * https://github.com/lopspower/CircularProgressBar
 */

public class SimonGame extends MainActivity {
    Activity activity;
    Context context;
    public static String PACKAGE_NAME;

    Boolean playing;                // Bool indicates playing (only true when it's time for
                                    //   user to repeat sequence).
    int[] sequence;                 // integer array containing correct sequence for round
    int indexMatched;               // current index up to which point user has matched
    int buttonTaps;                 // count of button taps
    int roundCount;                 // how many rounds has user played?
    int level;                      // what level is user currently playing?
    int levelThreshold;             // what is the level threshold?
    int matchesThisLevel;           // how many matches for user at this level?
    int[] userResp;
    int respIndex;
    static boolean silence = false;
    static String[] mySoundFile;
    String[] GameSounds;
    ImageButton coloredButtons[] = new ImageButton[4];

    int len = 4;                    // constant for how many colors/tones are used in  the game
    final int COUNT = 25;           // Game sequence length limit
    final int MAX_LEVEL = 10;       // Game max level achievable.

    final static String[] glockSounds = {"c06", "c18", "d08", "g01"};
    final static String[] colorWords = {"blue_m","green_f","yellow_f","red_m"};
    final String[] mygameSounds ={"mistake","winning"};

    CountDownTimer threeSecTimer, sequenceTimer;    // timers
    TextView msg1, msg2;                            // textviews
    TextView levelTxt, levelSub, levelVal;
    TextView roundTxt, roundSub, roundVal;
    TextView threshVal, matchTxt, matchSub;
    TextView matches, Re, PETE, newgamelab, againlab;

    ImageView bigSmiley, middleView;                // imageviews
    ImageButton but1, but2, but3, but4;             // imagebutons
    ImageButton playb, again;

    CircularProgressBar cpb;        // declare the timer visual
    int animationDuration = 3000;   // 3000ms = 3 seconds

    public SimonGame(Context context, Activity activity) {             // constructor
        this.context = context;
        this.activity = activity;
        PACKAGE_NAME = activity.getPackageName();
        d("JW: Pkg name - ",PACKAGE_NAME);

        playing = false;
        sequence = new int[COUNT];
        indexMatched = 0;
        buttonTaps = 0;
        roundCount = 1;
        level = 1;
        levelThreshold = 15;      // Should be 15 for real.  Other settings (4) for debugging.
        matchesThisLevel = 0;
        userResp = new int[COUNT];
        respIndex = 0;
        silence = false;
        GameSounds = mygameSounds;
        mySoundFile = glockSounds;

        but1 = (ImageButton) activity.findViewById(R.id.color1);
        but2 = (ImageButton) activity.findViewById(R.id.color2);
        but3 = (ImageButton) activity.findViewById(R.id.color3);
        but4 = (ImageButton) activity.findViewById(R.id.color4);

        playb = (ImageButton) activity.findViewById(play);
        again = (ImageButton) activity.findViewById(R.id.again);

        coloredButtons[0] = but1;
        coloredButtons[1] = but2;
        coloredButtons[2] = but3;
        coloredButtons[3] = but4;

        threeSecTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                stop3secTimer();
                roundFinished(2);
                System.out.println("Round Finished: reason 2");
            }
        };

        msg1 = (TextView) activity.findViewById(R.id.messageLabel);
        msg2 = (TextView) activity.findViewById(R.id.messageLabel2);

        newgamelab = (TextView) activity.findViewById(R.id.newgamelabel);
        againlab = (TextView) activity.findViewById(R.id.againlabel);

        levelTxt = (TextView) activity.findViewById(R.id.Level);
        matchTxt = (TextView) activity.findViewById(R.id.Match);
        roundTxt = (TextView) activity.findViewById(R.id.Round);

        levelSub = (TextView) activity.findViewById(R.id.LevelSubText);
        matchSub = (TextView) activity.findViewById(R.id.MatchSubtext);
        roundSub = (TextView) activity.findViewById(R.id.RoundSubText);

        roundVal = (TextView) activity.findViewById(R.id.RoundValue);
        levelVal = (TextView) activity.findViewById(R.id.levelValue);
        threshVal = (TextView) activity.findViewById(R.id.MatchValue);

        Re = (TextView) activity.findViewById(R.id.Re);
        PETE = (TextView) activity.findViewById(R.id.PETE);
        matches = (TextView) activity.findViewById(R.id.matchCount);

        bigSmiley = (ImageView) activity.findViewById(R.id.yellowSmiley);
        middleView = (ImageView) activity.findViewById(R.id.centerImageView);

        cpb = (CircularProgressBar) activity.findViewById(R.id.cpb);
    } // end of constructor

    public static void setSoundFile(int num) {
        switch (num) {
            case 6 : mySoundFile = glockSounds;
                silence = false;
                break;
            case 7 : mySoundFile = colorWords;
                silence = false;
                break;
            case 8 : silence = true;
                break;
            default : mySoundFile = glockSounds;
                Log.e("REPETE ERROR: ", "Error setting soundfile");
                break;
        }
    }
    // **************************************************************************************************************
    // INIT(), RESET(), and SETUPBUTTONS() Functions  - all called when instance is created.
    // **************************************************************************************************************
    public void init() {
        // set fonts for textViews, etc... on game play view
        Typeface treb_ms = Typeface.createFromAsset(activity.getAssets(), "fonts/Trebuchet_MS.ttf");
        Typeface treb_ms_b = Typeface.createFromAsset(activity.getAssets(), "fonts/Trebuchet_MS_Bold.ttf");
        Typeface treb_ms_bi = Typeface.createFromAsset(activity.getAssets(), "fonts/Trebuchet_MS_Bold_Italic.ttf");
        Typeface mfw = Typeface.createFromAsset(activity.getAssets(), "fonts/MarkerFelt-Wide.ttf");

        msg1.setTypeface(treb_ms_b);
        msg2.setTypeface(treb_ms_b);
        msg1.setText("");
        msg2.setText("");

        Re.setTypeface(treb_ms_b);
        PETE.setTypeface(treb_ms_bi);

        newgamelab.setTypeface(mfw);
        againlab.setTypeface(mfw);

        levelTxt.setTypeface(treb_ms_b);
        matchTxt.setTypeface(treb_ms_b);
        roundTxt.setTypeface(treb_ms_b);

        levelSub.setTypeface(treb_ms);
        matchSub.setTypeface(treb_ms);
        roundSub.setTypeface(treb_ms);

        bigSmiley.setVisibility(View.VISIBLE);
        cpb.setVisibility(View.INVISIBLE);

        reset(true);
    } // end of function init()


    public void reset(boolean first) {      // Reset all of the class properties (myGame)
        playing = false;                    // only true when time for user to repeat sequence
        sequence = new int [COUNT];
        indexMatched = 0;
        buttonTaps = 0;
        roundCount = 1;
        level = 1;
        levelThreshold = 15;
        matchesThisLevel = 0;
        userResp = new int [COUNT];
        respIndex = 0;

        // Reset all statistics labels to display new game appropriate info.
        levelVal.setText(Integer.toString(level));
        threshVal.setText(Integer.toString(levelThreshold));
        roundVal.setText(Integer.toString(roundCount));
        matches.setText(Integer.toString(indexMatched));

        // Display NEW GAME label every time unless called from onCreate (just big smiley)
        if (!first) {
            Handler myHandler = new Handler();
            bigSmiley.setVisibility(View.INVISIBLE);
            msg2.setText("NEW GAME");

            // declare runnable to set msg2 back to empty
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    msg2.setText("");
                }
            };
            myHandler.postDelayed(myRunnable, 2000);    // run runnable after 2 seconds.
        }

        // Make sure the match count and middle view smiley are hidden
        matches.setVisibility(View.INVISIBLE);
        middleView.setVisibility(View.INVISIBLE);

    } // end of reset() method


    public void setupButtons() {
        // ****************************************************************
        // declare / define method for when colored buttons are clicked.
        // ****************************************************************
        OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler myHandler = new Handler();
                int btag;

                if (playing) {              // if playing (i.e. player repeating pattern)
                    stop3secTimer();        //   Stop the 3sec timer.
                    switch (v.getId()) {    //   Figure out which colored button was pressed.
                        case R.id.color1:
                            btag = 10;
                            break;
                        case R.id.color2:
                            btag = 11;
                            break;
                        case R.id.color3:
                            btag = 12;
                            break;
                        case R.id.color4:
                            btag = 13;
                            break;
                        default:
                            btag = 10;
                            break;
                    }
                    buttonTaps += 1;                    // update button tap counter
                    userResp[respIndex] = btag;         // add current button to array holding user response so far

                    if (btag != sequence[respIndex]) {  // Check if current button press is correct
                        roundFinished(1);               //   by comparing it to correct place in sequence.
                        d("ROUND OVER: ", "Round over - reason 1 (Oops - no match)");  // (for debugging)
                    } else {
                        highlight(btag);                    // if YES, (we have a match) ...
                        respIndex += 1;                     //    highlight button and increment user index

                        if (buttonTaps == indexMatched + 1) {        //  Check if we're at end of matched sequence (so far)
                            indexMatched += 1;                              //  YES, continue, increment index
                            matches.setText(String.valueOf(indexMatched));  //     and display "matches" in center
                            matches.setVisibility(View.VISIBLE);

                            // Update counter "matches needed for next level"
                            if ((levelThreshold - matchesThisLevel - indexMatched) >= 0) {
                                threshVal.setText(String.valueOf(levelThreshold - matchesThisLevel - indexMatched));
                            } else {
                                threshVal.setText("0");
                            }


                            if (indexMatched < COUNT) {                     //    Check tht we're not at end of seq (25)
//                                Runnable myRunnable = new Runnable() {
//                                    @Override
//                                    public void run() {
                                        playNextSeq();                      //       NO, proceed - adding 1 more element to seq.
//                                    }
//                                };
////                                myHandler.postDelayed(myRunnable, 1000);
//                                myHandler.post(myRunnable);
                            } else {                                        //       YES, call roundFinished() reason 3
                                roundFinished(3);
                                d("ROUND OVER: ", "Round over - reason 3 (Matched entire sequence)");  // (for debugging)
                            }
                        } else {                                           //  NO, keep playing this round (wait for more
                            start3secTimer();                              //      button taps) so start tmer.
                        }
                    }
                }
            }
        };
        but1.setOnClickListener(buttonClick);       // assign the above method to the 4 colored buttons.
        but2.setOnClickListener(buttonClick);
        but3.setOnClickListener(buttonClick);
        but4.setOnClickListener(buttonClick);

        // ****************************************************************
        // declare / define method for when NEW GAME button is clicked.
        // ****************************************************************
        OnClickListener start = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnimation = new AlphaAnimation(1, 0);
                myAnimation.setDuration(100);
                myAnimation.setInterpolator(new LinearInterpolator());
                myAnimation.setRepeatCount(0);
                playb.startAnimation(myAnimation);

                reset(false);
                startRound(true);
            }
        };
        playb.setOnClickListener(start);

        // ****************************************************************
        // declare / define method for when AGAIN button is clicked.
        // ****************************************************************
        OnClickListener againL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation myAnimation = new AlphaAnimation(1, 0);
                myAnimation.setDuration(100);
                myAnimation.setInterpolator(new LinearInterpolator());
                myAnimation.setRepeatCount(0);
                again.startAnimation(myAnimation);

                startRound(false);
            }
        };
        again.setOnClickListener(againL);

    }  //  end setupButtons()

    // **************************************************************************************************************
    // TIMER Functions
    // **************************************************************************************************************
    //
    // start3secTimer
    // ****************************************************************
    public void start3secTimer() {
        if (threeSecTimer == null) {                            // if new timer needed
            threeSecTimer = new CountDownTimer(3000, 1000) {    //   create one - counting down
                @Override                                       //   from 3 seconds by 1 sec.
                public void onTick(long l) {

                }
                @Override
                public void onFinish() {        // when timer gets to zero
                    stop3secTimer();            //   stop/reset timer
                    roundFinished(2);           //   call roundFinished(Took too long)
                    d("TIMER: ", "Timer triggered");
                    d("ROUND OVER: ", "Round Finished: reason 2 (Took too long)");
                }
            };
        }
        pause(250);
        threeSecTimer.start();                                  // Start the timer
        d("TIMER: ", "Starting timer");
        cpb.setProgress(0);                                     // set animation of circular progess bar to 0
        cpb.setProgressWithAnimation(100, animationDuration);   // tell cpb to animate to 100% in 3 seconds
        cpb.setVisibility(View.VISIBLE);                        // make cpb visible
    }

    // ****************************************************************
    // stop3secTimer()
    // ****************************************************************
    public void stop3secTimer() {
        d("TIMER: ", "Stopping timer");
        cpb.setVisibility(View.INVISIBLE);  //  hide timer visual (circular progess bar)

        if (threeSecTimer != null) {        //  make sure timer is stopped.
            threeSecTimer.cancel();
            threeSecTimer = null;
        }
    }

    public static void pause(double t) {
        // *****************************************************
        // wait method
        // This method is passed in an integer representing the number of seconds
        //   to wait.  The Thread.sleep method expects nanoseconds so convert
        //   t to nanoseconds by multiplying by 1000.
        // ****************************************************
        int tNanoSec;

        tNanoSec = (int) t;
//        tNanoSec = (int)(t * 1000);        // The following are somewhat helpful in understanding exceptions.
        try {                       //   http://www.tutorialspoint.com/javaexamples/exception_thread.htm
            Thread.sleep(tNanoSec); //   http://www.tutorialspoint.com/java/java_builtin_exceptions.htm
        } catch (Exception e) {     //   http://www.tutorialspoint.com/java/java_exceptions.htm
            System.out.println("Caught exception: " + e);  // If e is only argument, android studio generates warning
        }                                                  //     so add the text and you're good
    }

    // **************************************************************************************************************
    // startRound() AND roundFinished() and generateSequence()  Functions
    // **************************************************************************************************************
    public void startRound(Boolean first) {

        Handler myHandler = new Handler();
        if (level <= MAX_LEVEL) {                       // Check level property (>10 means game over)
            middleView.setVisibility(View.INVISIBLE);   // hide the middle  smiley
            matches.setVisibility(View.INVISIBLE);      // hide the match count
            generateSequence();                         // generate the sequence to match
            indexMatched = 0;                           // initialize game property tracking how many we've matched
            playNextSeq();                              // play the next sequence after 1 second
            msg1.setText("");                           // reset message label
            if (!first) {                               // update msg2 with roundCount (if not first round)
                String tmpString = "Play Next Round " + roundCount;
                msg2.setText(tmpString);
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        msg2.setText("");
                    }
                };
                myHandler.postDelayed(myRunnable, 2000);    // after 2 secs reset msg2
            }

            roundVal.setText(Integer.toString(roundCount)); // update the roundCount displayed
//            matches.setText(indexMatched);
        }
    }

    public void roundFinished(int reason) {
        playing = false;
        String msg1Text = "";

        switch (reason) {
            case 1 : msg1Text = "Oops! No Match!";
                middleView.setImageResource(R.drawable.red_frowny);
                playGameSound(0);
                break;
            case 2: msg1Text = "Took Too Long!";
                middleView.setImageResource(R.drawable.blue_distressed2);
                playGameSound(0);
                break;
            case 3: msg1Text = "AWESOME!  \n Entire Sequence Matched!";
                middleView.setImageResource(R.drawable.blue_smiley);
                playGameSound(1);
                break;
            default : Log.e("Error:","Error finishing up round"); break;
        }
        msg1.setText(msg1Text);
        middleView.bringToFront();
        middleView.setVisibility(View.VISIBLE);

        matchesThisLevel += indexMatched;
        if ((levelThreshold - matchesThisLevel) >= 0) {
            threshVal.setText(String.valueOf(levelThreshold - matchesThisLevel));
        } else {
            threshVal.setText("0");
        }

        roundCount += 1;
        manageLevel();
        if (level > MAX_LEVEL) {
            msg1.setText("HIGHEST level completed!");
            middleView.setImageResource(R.drawable.repete_face);
            playGameSound(1);
            msg2.setText("");
        }

    } // end of roundFinished()

    public void manageLevel() {
        if ((roundCount == 4) || (matchesThisLevel >= levelThreshold)) {
            if (matchesThisLevel >= levelThreshold) {
                level += 1;
                levelThreshold +=3;
                if (level <= MAX_LEVEL) {
                    levelVal.setText(String.valueOf(level));
                    msg2.setText("Next Level Achieved!");
                }
            }else {
                msg2.setText("Repeat Level");
            }
            matchesThisLevel = 0;
            roundCount = 1;
            roundVal.setText(String.valueOf(roundCount));
            threshVal.setText(String.valueOf(levelThreshold));
        }
    }

    public void generateSequence() {
        // *********************************************************************************
        // Generate a random number (0 - num (4) of elements to be repeatd) count (25) times.
        // Use that random number as index into char set and append char
        // at that index to generated sequence string.
        // ********************************************************************************
//        int[] testsequence = {12,13,10,12,13,13,11,10};
//        this.sequence = testsequence;

        // These used for debugging
//        sequence = [12,13,10,11]
//        this.sequence = {12,13,10,12,13,13,11,10};
//        self.sequence = [10,10,10,10,10,10,10,10]

        Random rand = new Random();
        int randNum;                   // random number betw 0 and count of elements to be repeatd

        for (int i = 0; i < COUNT; i++)  {        // count (25) times
            // generate random number (len is 4 for classic mode) and add 10
            //    (button tags are 10,11,12,13)
            randNum =  rand.nextInt(len) + 10;
            this.sequence[i] = randNum;        // insert into sequence array
//            System.out.println(randNum + ", ");
        }

    } // end of function generateSequence()

// **************************************************************************************************************
// Game play functions: highlight(), playNextSequence(), displayPatternSoFar()
// **************************************************************************************************************

    public void highlight(int btag) {
        int index = btag - 10;
        ImageButton b = coloredButtons[index];

        if (!silence) {
            int soundID = activity.getResources().getIdentifier(mySoundFile[index], "raw", PACKAGE_NAME);

            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
            }
            mp = MediaPlayer.create(context, soundID);
            mp.start();
        }

        Animation myAnimation = new AlphaAnimation(1, 0);
        myAnimation.setDuration(100);
        myAnimation.setInterpolator(new LinearInterpolator());
        myAnimation.setRepeatCount(0);
        b.startAnimation(myAnimation);
    }


    public void playGameSound(int soundNum) {
        if (!silence) {
            int soundID = activity.getResources().getIdentifier(GameSounds[soundNum], "raw", PACKAGE_NAME);
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
            }
            mp = MediaPlayer.create(context, soundID);
            mp.start();
        }
    }

    public void displayPatternSoFar(int index, long wtime) {
        final int pIndex = index;
        final long seqTime = wtime;

        if (sequenceTimer == null) {
            sequenceTimer = new CountDownTimer(seqTime, 850) {
                int i = 0;

                @Override
                public void onTick(long l) {
                    if (i < pIndex) {
                        highlight(sequence[i]);
                        i += 1;
                    }
                }

                @Override
                public void onFinish() {
                    if (sequenceTimer != null) {
                        sequenceTimer.cancel();
                        sequenceTimer = null;
                    }
//                    if (i < pIndex) {
//                        highlight(sequence[i]);
//                    }
                }
            };
        }
        sequenceTimer.start();


//        final Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < pIndex; i++) {
//                    final int value = i;
//                    pause(850);
//                    but1.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            highlight(sequence[value]);
//                        }
//                    });
//                }
//            }
//        };
//        new Thread(myRunnable).start();
    }

    public void playNextSeq() {
        Handler myHandler = new Handler();                      // declare and init variables myHandler
        final long waitTime;                                    //     and waitTime
        waitTime = (indexMatched+1) * 850 + 850;
//        waitTime = ((indexMatched+1) * 850) + 1000;             // compute time to display sequence
                                                                //   850ms/highlight times # being displayed + 1 sec delay
        playing = false;                                        // set playing to false (no more user button presses)
        Runnable myRunnable = new Runnable() {                  // declare and init myRunnable to implement 1 sec delay
            @Override                                           //    before displaying sequence.
            public void run() {
                d("PLAY: ","myRunnable - playNextSeq");
                displayPatternSoFar(indexMatched+1, waitTime); // Call function to display sequence.
            }
        };
        myHandler.postDelayed(myRunnable, 1500);                //  start myRunnable to display sequence after 1.5 sec (delay implemented)

        // need to delay this until pattern is displayed
        Runnable setDelay = new Runnable() {                    // declare and init setDelay runnable to implement another delay
            @Override
            public void run() {                                 // do stuff in the runnable after delay (after sequence displayed)
                d("PLAY ","setDelay - playNextSeq");
                playing = true;                                 // Prepare for user to repeat sequence.
                buttonTaps = 0;                                 // set playing to true (user can now press buttons)
                userResp = new int[COUNT];                      // reset counters buttonTaps and respIndex. Init response array
                respIndex = 0;
                start3secTimer();                               //  Lastly, start the 3 second timer.
            }
        };
        myHandler.postDelayed(setDelay, waitTime);         // start setDelay runnable after waitTime delay (after sequence displayed)

    }

}
