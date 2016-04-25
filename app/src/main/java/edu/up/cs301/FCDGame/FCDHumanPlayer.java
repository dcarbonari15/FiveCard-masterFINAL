package edu.up.cs301.FCDGame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The human controlled player. Has GUI.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */
public class FCDHumanPlayer extends GameHumanPlayer implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    //instance variables
    private Activity myActivity; //activity used for accessing
    private Menu myMenu; //obsolete
    protected FCDState state; //the current state
    protected String cardBack = "default"; //cardback
    protected int cardBackImage = R.drawable.card_b; //carback image

    protected ImageView player1Card[] = new ImageView[5]; //the home player's card images
    protected boolean[] cardSelected = new boolean[5]; //selected cards of home player
    protected ArrayList<Integer> discards = new ArrayList(); //slot numbers of selected cards

    protected ImageView deck; //deck image

    protected ImageView player2Card[] = new ImageView[5]; //opponents card images

    //buttons
    protected Button callButton;
    protected Button foldButton;
    protected Button throwButton;
    protected Button raiseButton;
    protected Button checkButton;
    protected SeekBar raiseSeekBar;

    //text views
    protected TextView potValue;
    protected TextView player1Money;
    protected TextView player2Money;
    protected TextView raiseValue;
    protected TextView lastBetDisplay;
    protected TextView gameStateDisplay;

    //most recent bet
    private int lastBet;
    private int clickCount = 0;//obsolete


    /**
     * constructor
     *
     * @param name
     */
    public FCDHumanPlayer(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return null;
    }

    /**
     * updates the GUI when a new game state is received
     *
     * @param info
     */
    public void receiveInfo(GameInfo info) {
        Log.i("HumanReceiveInfo", "received info");
        if(info instanceof FCDState) {
            Log.i("HumanReceiveFCdState", "received FCdState");
            this.state = (FCDState) info;

            //general text views
            potValue.setText("$" + state.getPot());
            player1Money.setText("$" + state.getLobby().get(this.playerNum).getMoney());
            gameStateDisplay.setText("Current Game Stage: " + state.getGameStage());

            //fold
            if(state.getLastAction().equals("fold")){
                if(state.getActivePlayer() == this.playerNum) {
                    lastBetDisplay.setText("You Fold");
                }else{
                    lastBetDisplay.setText("Player " + state.getActivePlayer() + " folds");
                }

                //raise
            }else if(state.getLastAction().equals("raise")){
                if(state.getLastBet() != -1) {
                    if (state.getActivePlayer() == this.playerNum) {
                        lastBetDisplay.setText("You raise $" + state.getLastBet());
                    } else {
                        lastBetDisplay.setText("Player " + state.getActivePlayer() + " raises $" +
                                state.getLastBet());
                    }
                }else{
                    if (state.getActivePlayer() == this.playerNum) {
                        lastBetDisplay.setText("You raise $" + state.getPot());
                    } else {
                        lastBetDisplay.setText("Player " + state.getActivePlayer() + " raises $" +
                                state.getPot());
                    }
                }

                //check
            }else if(state.getLastAction().equals("check")){
                if(state.getActivePlayer() == this.playerNum) {
                    lastBetDisplay.setText("You check");
                }else{
                    lastBetDisplay.setText("Player " + state.getActivePlayer() + " checks");
                }

                //call
            }else if(state.getLastAction().equals("call")){
                if(state.getActivePlayer() == this.playerNum) {
                    lastBetDisplay.setText("You Call");
                }else{
                    lastBetDisplay.setText("Player " + state.getActivePlayer() + " calls");
                }


                //no action taken yet
            }else{
                lastBetDisplay.setText("No action taken yet");
            }

            //determine opposing player
            if (this.playerNum == 0){
                player2Money.setText("$" + state.getLobby().get(1).getMoney());
            } else {
                player2Money.setText("$" + state.getLobby().get(0).getMoney());
            }

            //update for betting stages
            if(state.getGameStage() == 1 || state.getGameStage() == 3) {
                if (state.getLastBet() == -1) {
                    if(state.getLobby().get(this.playerNum).getMoney() < 10){
                        raiseSeekBar.setMax(state.getLobby().get(this.playerNum).getMoney()+1);
                        raiseValue.setText("$" + (raiseSeekBar.getProgress()));
                        lastBet = state.getLastBet();
                    }else {
                        raiseSeekBar.setMax(state.getLobby().get(this.playerNum).getMoney() - 10);
                        raiseValue.setText("$" + (raiseSeekBar.getProgress() + 10));
                        lastBet = state.getLastBet();
                    }
                } else {
                    lastBet = state.getLastBet();
                    raiseSeekBar.setMax(state.getLobby().get(this.playerNum).getMoney() - lastBet);
                    raiseValue.setText("$" + (raiseSeekBar.getProgress() + lastBet));
                }
            }else{ //update for non betting stages
                raiseSeekBar.setMax(state.getLobby().get(this.playerNum).getMoney() - 10);
                raiseValue.setText("$" + (raiseSeekBar.getProgress() + 10));
                lastBet = state.getLastBet();
            }

            //show opponent's cards in showdown
            Log.i("revealing", Integer.toString(state.getGameStage()));
            int opponentNum;
            if (this.playerNum == 0){
                opponentNum = 1;
            } else {
                opponentNum = 0;
            }
            if (state.getGameStage() == 4){//if show down
                for (int i = 0; i < 5; i++) {
                    Card card = state.getLobby().get(opponentNum).getCard(i);
                    player2Card[i].setImageResource(card.getResIdx(card.getSuit(), card.getRank()));
                }
            } else {//not showdown yet
                for (int i = 0; i < 5; i++) {
                    Card card = state.getLobby().get(opponentNum).getCard(i);
                    player2Card[i].setImageResource(cardBackImage);
                }
            }

            //updates card images
            for(int i = 0; i < 5; i++){
                Log.i("CardImage", (player1Card[i].getDrawable()).toString());
                setCardImage(i, state.getLobby().get(this.playerNum).getCard(i));
                Log.i("CardImage", (player1Card[i].getDrawable()).toString());
                Log.i("CardImage", "card image updated");
            }

        }else{//do nothing
            return;
        }

    }


    /*
    * send info override from GameHumanPlayer
    * I don't know, maybe this will make throw work, but Android Studio's emulator takes 3 hours to start so I cant test
     */
    @Override
    public void sendInfo(GameInfo info){
        super.sendInfo(info);
    }

    /**
     * sets the default gui
     *
     * @param activity
     */
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.five_card_draw);

        //home player's cards
        player1Card[0] = (ImageView) activity.findViewById(R.id.playerCard1);
        player1Card[0].setOnClickListener(this);
        player1Card[1] = (ImageView) activity.findViewById(R.id.playerCard2);
        player1Card[1].setOnClickListener(this);
        player1Card[2] = (ImageView) activity.findViewById(R.id.playerCard3);
        player1Card[2].setOnClickListener(this);
        player1Card[3] = (ImageView) activity.findViewById(R.id.playerCard4);
        player1Card[3].setOnClickListener(this);
        player1Card[4] = (ImageView) activity.findViewById(R.id.playerCard5);
        player1Card[4].setOnClickListener(this);

        //opponents cards
        player2Card[0] = (ImageView) activity.findViewById(R.id.player2Card1);
        player2Card[1] = (ImageView) activity.findViewById(R.id.player2Card2);
        player2Card[2] = (ImageView) activity.findViewById(R.id.player2Card3);
        player2Card[3] = (ImageView) activity.findViewById(R.id.player2Card4);
        player2Card[4] = (ImageView) activity.findViewById(R.id.player2Card5);

        //deck
        deck = (ImageView) activity.findViewById(R.id.deck);
        deck.setOnClickListener(this);

        //action buttons
        callButton = (Button) activity.findViewById(R.id.callButton);
        callButton.setOnClickListener(this);
        //callButton.setVisibility(View.INVISIBLE);
        foldButton = (Button) activity.findViewById(R.id.foldButton);
        foldButton.setOnClickListener(this);
        throwButton = (Button) activity.findViewById(R.id.throwButton);
        throwButton.setOnClickListener(this);
        checkButton = (Button) activity.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
        // checkButton.setVisibility(View.INVISIBLE);
        raiseButton = (Button) activity.findViewById(R.id.raiseButton);
        raiseButton.setOnClickListener(this);

        //bet amount seek bar
        raiseSeekBar = (SeekBar) activity.findViewById(R.id.raiseSeekBar);
        raiseSeekBar.setOnSeekBarChangeListener(this);

        //textviews
        potValue = (TextView) activity.findViewById(R.id.potValue);
        gameStateDisplay = (TextView) activity.findViewById(R.id.gameStateDisplay);
        player1Money = (TextView) activity.findViewById(R.id.player1Money);
        player2Money = (TextView) activity.findViewById(R.id.player2Money);
        raiseValue = (TextView) activity.findViewById(R.id.raiseValue);
        lastBetDisplay = (TextView) activity.findViewById(R.id.lastBetDisplay);

        for(int i = 0; i < 5; i++){
            cardSelected[i] = false;
        }





        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
        // if (state != null) {
        receiveInfo(state);
        // }
    }

    /**
     * handles the user clicking on buttons and cards
     *
     * @param v
     */
    public void onClick(View v) {


        //check if clicked on a card, and turn it red
        if (state.getGameStage() == 2) {//if throw stage
            for (int i = 0; i < 5; i++) {
                if (v == player1Card[i]) {
                    if (cardSelected[i] == false) {
                        discards.add(i);
                        Log.i("DiscardAdd; numCards:", Integer.toString(discards.size()));
                        player1Card[i].setColorFilter(0xAAFF0000);
                        cardSelected[i] = true;
                    } else {
                        for (int j = 0; j < discards.size(); j++) {
                            if (discards.get(j) == i) {
                                discards.remove(j);
                                Log.i("DiscardRemve; numCards:", Integer.toString(discards.size()));
                            }
                        }
                        player1Card[i].setColorFilter(0x00000000);
                        cardSelected[i] = false;
                    }
                    clickCount++;
                }
            }
        }

        //call
        if (v == callButton){
            lastBetDisplay.setText("You tried to Call");
            if(state.getPot() < 0){
                return;
            }else {
                //  state.playerCalls(this.playerNum, state.getPot());
                state.setLastAction("call");
                game.sendAction(new FCDCallAction(this));
            }
        }

        //check
        if(v == checkButton){
            lastBetDisplay.setText("You tried to Check");
            state.setLastAction("check");
            game.sendAction(new FCDCheckAction(this));
        }

        //fold
        if (v == foldButton){
            state.playerFolds(0);
            state.setLastAction("fold");
            game.sendAction(new FCDFoldAction(this));
        }

        //throw
        if (v == throwButton){
            //state.playerDiscards(state.getActivePlayer(), state.getPlayer1Hand(state.getActivePlayer()));//TODO
            int[] temp = new int[5];
            int i = 0;
            for( ; i < discards.size(); i++){
                temp[i] = discards.get(i);
            }
            for (; i < 5; i++) {
                temp[i] = -1;
            }

            //clear selection of cards
            for (i = 0; i < 5; i++){
                player1Card[i].setColorFilter(0x00000000);
                cardSelected[i] = false;
            }
            for (int derp = 0; derp < 2; derp++) {
                for (i = 0; i < discards.size(); i++) {
                    discards.remove(0);
                }
            }
            discards.clear();


            //Log.i("HumanPlayerThrowing:", state.g);
            Log.i("temp:",Integer.toString(temp[1]));
            game.sendAction(new FCDThrowAction(this, temp));
        }

        //raise
        if (v == raiseButton){
            Log.e("Raising:", ((String) (raiseValue.getText())).substring(1, raiseValue.length()));
            state.playerRaises(state.getActivePlayer(), state.getLastBet(), Integer.parseInt(
                    ((String) (raiseValue.getText())).substring(1, raiseValue.length())));
            state.setLastAction("raise");
            game.sendAction(new FCDRaiseAction(this, Integer.parseInt(
                    ((String) (raiseValue.getText())).substring(1, raiseValue.length())) + 1));
            Log.e("setting last bet", "" + state.getLastBet());
            state.setLastBet(Integer.parseInt(((String)(raiseValue.getText())).substring(1,
                    raiseValue.length())) + 1);
            Log.e("set last bet", "" + state.getLastBet());
        }

        //cardback toggling
        if (v == deck){
            //cycle through cardbacks
            if (cardBack.equals("default")){
                cardBackImage = (R.drawable.nuxback);
                cardBack = "nux";
            }
            else if (cardBack.equals("nux")){
                cardBackImage = (R.drawable.vegdahlback);
                cardBack = "vegdahl";
            }
            else{
                cardBackImage = (R.drawable.card_b);
                cardBack = "default";
            }

            //updates cardback images
            deck.setImageResource(cardBackImage);
            if (cardBack.equals("default")){
                deck.setImageResource(R.drawable.deck);
            }
            for (int i = 0; i < 5; i ++){
                if (state.getGameStage() != 4) {
                    player2Card[i].setImageResource(cardBackImage);
                }
            }
        }
        return;
    }

    /**
     * handles seekbar changes
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //raise
        if (seekBar == raiseSeekBar){
            //updates bet value text view
            if(state.getGameStage() == 1 && state.getActivePlayer() == this.playerNum) {
                if (state.getLastBet() == -1) {
                    raiseSeekBar.setMax(state.getLobby().get(state.getActivePlayer()).getMoney() - 10);
                    raiseValue.setText("$" +(raiseSeekBar.getProgress() + 10));
                    lastBet = state.getLastBet();
                } else {
                    lastBet = state.getLastBet();
                    raiseSeekBar.setMax(state.getLobby().get(state.getActivePlayer()).getMoney() - lastBet);
                    raiseValue.setText("$" + (raiseSeekBar.getProgress() + lastBet));
                }
            }else{
                lastBet = state.getLastBet();
                raiseSeekBar.setMax(state.getLobby().get(state.getActivePlayer()).getMoney() - lastBet);
                raiseValue.setText("$" + (raiseSeekBar.getProgress() + lastBet));
            }
        }
    }

    /**
     * reveals the opponents cards at end of round
     */
    public void revealOpponentsCards(){
        //show opponent's cards in showdown
        Log.i("revealingM", Integer.toString(state.getGameStage()));
        getTimer().setInterval(1);
        getTimer().start();
    }

    /**
     * handles the timer to reveal the opponent's cards
     */
    @Override
    public void timerTicked(){
        int opponentNum;
        if (this.playerNum == 0){
            opponentNum = 1;
        } else {
            opponentNum = 0;
        }
        for (int i = 0; i < 5; i++) {
            Card card = state.getLobby().get(opponentNum).getCard(i);
            player2Card[i].setImageResource(card.getResIdx(card.getSuit(), card.getRank()));
        }
        getTimer().stop();
    }


    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * updates a card image for the home player when given a rank and suit
     *
     * @param slot
     * @param card
     */
    public void setCardImage(int slot, Card card){
        player1Card[slot].setImageResource(card.getResIdx(card.getSuit(),card.getRank()));

    }
}
