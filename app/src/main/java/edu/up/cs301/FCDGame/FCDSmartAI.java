package edu.up.cs301.FCDGame;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * This is the class that will contain the smart AI. Instead of just randomly choosing an action,
 * this AI will find out the value of their hand, what move their opponent took and make a decision
 * based on that information.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version April 2016
 */
public class FCDSmartAI extends GameComputerPlayer{
    private double pause;
    private int handVal;
    private int lastPotVal = 0;
    private int newPotVal = 0;
    private boolean IRaised = false;
    private GameAction lastTriedMove;

    private FCDState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public FCDSmartAI(String name) {


        this(name, 2.0);
    }

    public FCDSmartAI(String name, Double pause){
        super(name);

        this.pause = pause * 1000;
    }

    /**
     * this is the receiveinfo method where the comupter player take in the information that they
     * are given and makes a decision based on it.
     *
     * @param info --> the gameInfo that the local game sends to the AI
     */

    @Override
    protected void receiveInfo(GameInfo info) {
        if(!(info instanceof FCDState)){
            if(info instanceof IllegalMoveInfo){
                if(lastTriedMove instanceof FCDCallAction){
                    game.sendAction(new FCDCheckAction(this));
                    return;
                }else if(lastTriedMove instanceof FCDCheckAction){
                    game.sendAction(new FCDCallAction(this));
                    return;
                }
            }
            return;
        }
        //saving the current game state
        savedState = (FCDState)info;

        pause();


        //choosing an action if the gamestage is in one of the betting stages
        if(savedState.getGameStage() == 1 || savedState.getGameStage() == 3) {
            newPotVal = savedState.getPot();
            Log.e("game stage", savedState.getGameStage() + "");
            Log.e("outside if", savedState.getLastAction() );
            //if the human player raises
            if (savedState.getLastAction().equals("raise")) {
                Log.e("In last action Raise", savedState.getLastAction());
                handVal = savedState.handValue(savedState.getLobby().get(this.playerNum).getHand());
                if((handVal > 1) && (handVal < 5)){
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 4));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 4);
                }else if(handVal > 4){
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 2));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 2);
                }else{
                    if(newPotVal - lastPotVal > 100) {
                        Log.e("in last bet if > 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI folds");
                        game.sendAction(new FCDFoldAction(this));
                    }else{
                        Log.e("in last bet if < 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI calls");
                        game.sendAction(new FCDCallAction(this));
                    }
                }
                lastPotVal = newPotVal;
                //if the human player checks
            } else if (savedState.getLastAction().equals("check")) {
                newPotVal = savedState.getPot();
                Log.e("in if last action check", savedState.getLastAction());
                int handVal = savedState.handValue(savedState.getLobby().get(this.playerNum).getHand());
                if((handVal > 1) && (handVal < 5)){
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 4));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 4);
                }else if(handVal > 4){
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 2));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 2);
                }else{
                    if(newPotVal - lastPotVal > 100) {
                        Log.e("in last bet if > 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI folds");
                        game.sendAction(new FCDFoldAction(this));
                    }else{
                        Log.e("in last bet if < 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI calls");
                        game.sendAction(new FCDCallAction(this));
                    }
                }
                lastPotVal = newPotVal;
                //if the computer player goes first
            } else if (savedState.getLastAction().equals("no action taken")){
                newPotVal = savedState.getPot();
                Log.e("in if no last action", savedState.getLastAction());
                int handVal = savedState.handValue(savedState.getLobby().get(this.playerNum).getHand());
                if((handVal > 1) && (handVal < 5)) {
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 4));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 4);
                }else if(handVal > 4) {
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 2));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 2);
                }else{
                    if(newPotVal - lastPotVal > 100) {
                        Log.e("in last bet if > 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI folds");
                        game.sendAction(new FCDFoldAction(this));
                    }else{
                        Log.e("in last bet if < 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI calls");
                        game.sendAction(new FCDCallAction(this));
                    }
                }
                lastPotVal = newPotVal;
                //if the human player calls
            }else if (savedState.getLastAction().equals("call")){
                newPotVal = savedState.getPot();
                Log.e("in if no last action", savedState.getLastAction());
                int handVal = savedState.handValue(savedState.getLobby().get(this.playerNum).getHand());
                if((handVal > 1) && (handVal < 5)) {
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 4));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 4);
                }else if(handVal > 4) {
                    Log.e("sending action", "smart AI raises");
                    game.sendAction(new FCDRaiseAction(this, savedState.getLobby().get(this.playerNum)
                            .getMoney() / 2));
                    savedState.setLastBet(savedState.getLobby().get(this.playerNum).getMoney() / 2);
                }else{
                    if(newPotVal - lastPotVal > 100) {
                        Log.e("in last bet if > 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI folds");
                        game.sendAction(new FCDFoldAction(this));
                    }else{
                        Log.e("in last bet if < 100", "last bet: " + (newPotVal - lastPotVal));
                        Log.e("sending action", "smart AI calls");
                        game.sendAction(new FCDCallAction(this));
                    }
                }
                lastPotVal = newPotVal;
            }else{
                pause();
                game.sendAction(new FCDCheckAction(this));
            }
        }
        //if the gamestage is in the throwing cards stage
        else if(savedState.getGameStage() == 2) {
            int[] indexToThrow = new int[5];
            int numCardsToThrow = (int) (Math.random() * 4) + 1;
            for (int i = 1; i <= numCardsToThrow; i++) {
                indexToThrow[i] = i;
            }
            Card[] playerhand = savedState.getPlayer1Hand(this.playerNum);
            Card[] handToThrow = savedState.getPlayer1Hand(this.playerNum);
            for (int i = 0; i < 5; i++) {
                if (i == indexToThrow[i]) {
                    handToThrow[i] = playerhand[i];
                }
            }
            Log.i("AI Move", "AI threw Cards");
            pause();
            game.sendAction(new FCDThrowAction(this, indexToThrow));
        }
        if(savedState.getGameStage() == 4){
            lastPotVal = 0;
            newPotVal = 0;
        }
    }

    /**
     * this is a helper method to add sleeps or pauses in the game so it is easier to see what the
     * AI is doing
     */
    private void pause(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //don't care
        }
    }
}


