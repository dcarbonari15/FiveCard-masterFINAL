package edu.up.cs301.FCDGame;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The dumb computerized game player player. This the calss that will contain the "dumb" AI
 * that will be hard to lose to.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version April 2016
 */
public  class DumbAI extends GameComputerPlayer{
    private double pause;
    private boolean IRaised = false;
    private GameAction lastTriedMove;

    private FCDState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DumbAI(String name) {


        this(name, 2.0);
    }

    public DumbAI(String name, Double pause){
        super(name);

        this.pause = pause * 1000;
    }


    /**
     * this is the receiveinfo method where the comupter player take in the information that they
     * are given and makes a random decision
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

        savedState = (FCDState)info;

        pause();


        Card[] hand = savedState.getPlayer1Hand(this.playerNum);
        int whatToDo;
        int bet;
        if(savedState.getLastBet() == -1){
            bet = (int) (Math.random() * (savedState.getLobby().get(savedState.getActivePlayer()).getMoney()) + 10);
        }else {
            bet = (int) (Math.random() * (savedState.getLobby().get(savedState.getActivePlayer()).getMoney()) + 10)
                    + savedState.getLastBet();
        }
        if(savedState.getGameStage() == 1 || savedState.getGameStage() == 3) {
            if (savedState.getGameStage() == 1) {
                if (savedState.getLastAction().equals("raise") && (savedState.getPot() != 0)) {
                    if (savedState.getLobby().get(this.playerNum).getMoney() <= 250) {
                        game.sendAction(new FCDFoldAction(this));
                    }
                }
            }
            if (savedState.getLastAction().equals("raise")) {
                whatToDo = (int) (Math.random() * 2);
                if (whatToDo == 1) {
                    Log.i("AI Move", "AI calls after player raise");
                    bet = savedState.getLastBet();
                    savedState.playerCalls(this.playerNum, bet);
                    game.sendAction(new FCDCallAction(this));
                    pause();
                    lastTriedMove = new FCDCallAction(this);
                    savedState.setLastAction("call");
                } else {
                    Log.i("AI Move", "AI raises after player raise");
                    pause();
                    if (savedState.getGameStage() == 1) {
                        if (!IRaised) {
                            bet = savedState.getPlayerMoney(this.playerNum) / 2;
                            savedState.playerRaises(this.playerNum, savedState.getPot(), bet + 1);
                            lastTriedMove = new FCDRaiseAction(this, bet + 1);
                            savedState.setLastAction("raise");
                            game.sendAction(new FCDRaiseAction(this, bet + 1));
                            IRaised = true;
                        } else {
                            Log.i("AI Move", "AI calls after player raise");
                            bet = savedState.getLastBet();
                            savedState.playerCalls(this.playerNum, bet);
                            game.sendAction(new FCDCallAction(this));
                            pause();
                            lastTriedMove = new FCDCallAction(this);
                            savedState.setLastAction("call");
                            IRaised = false;
                        }
                    } else {
                        bet = savedState.getPlayerMoney(this.playerNum);
                        savedState.playerRaises(this.playerNum, savedState.getPot(), bet + 1);
                        lastTriedMove = new FCDRaiseAction(this, bet + 1);
                        savedState.setLastAction("raise");
                        game.sendAction(new FCDRaiseAction(this, bet + 1));
                    }
                }
            } else if (savedState.getLastAction().equals("check")) {
                whatToDo = (int) (Math.random() * 3);
                if (whatToDo == 1) {
                    pause();
                    game.sendAction(new FCDCheckAction(this));
                    lastTriedMove = new FCDCheckAction(this);
                    savedState.setLastAction("check");
                    return;
                } else if (whatToDo == 2) {
                    pause();
                    game.sendAction(new FCDRaiseAction(this, bet));
                    lastTriedMove = new FCDRaiseAction(this, bet);
                    savedState.setLastAction("raise");
                } else {
                    pause();
                    int allInOrNot = (int) (Math.random() * 5);
                    if (allInOrNot == 3) {
                        pause();
                        game.sendAction(new FCDRaiseAction(this, savedState.getPlayerMoney(this.playerNum)
                                - savedState.getPot()));
                        lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(this.playerNum)
                                - savedState.getPot());
                        savedState.setLastAction("raise");
                    } else {
                        pause();
                        game.sendAction(new FCDCheckAction(this));
                        lastTriedMove = new FCDCheckAction(this);
                        savedState.setLastAction("check");
                    }
                }
            } else if (savedState.getLastAction().equals("no action taken")) {
                whatToDo = (int) (Math.random() * 3);
                if (whatToDo == 1) {
                    pause();
                    game.sendAction(new FCDCheckAction(this));
                    lastTriedMove = new FCDCheckAction(this);
                    savedState.setLastAction("check");
                    return;
                } else if (whatToDo == 2) {
                    pause();
                    game.sendAction(new FCDRaiseAction(this, bet));
                    lastTriedMove = new FCDRaiseAction(this, bet);
                    savedState.setLastAction("raise");
                } else {
                    pause();
                    int allInOrNot = (int) (Math.random() * 5);
                    if (allInOrNot == 3) {
                        pause();
                        game.sendAction(new FCDRaiseAction(this, savedState.getPlayerMoney(this.playerNum)
                                - savedState.getPot()));
                        lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(this.playerNum)
                                - savedState.getPot());
                        savedState.setLastAction("raise");
                    } else {
                        pause();
                        game.sendAction(new FCDCheckAction(this));
                        lastTriedMove = new FCDCheckAction(this);
                        savedState.setLastAction("check");
                        return;
                    }
                }
            } else if (savedState.getLastAction().equals("call")) {
                whatToDo = (int) (Math.random() * 2);
                if (whatToDo == 1) {
                    pause();
                    savedState.setLastAction("check");
                    game.sendAction(new FCDCheckAction(this));
                    lastTriedMove = new FCDCheckAction(this);
                    return;
                } else {
                    pause();
                    if (savedState.getPlayerMoney(this.playerNum) < 1) {
                        savedState.setLastAction("check");
                        game.sendAction(new FCDCheckAction(this));
                        lastTriedMove = new FCDCheckAction(this);
                        return;
                    } else {
                        bet = (int) (Math.random() * savedState.getPlayerMoney(this.playerNum));
                        savedState.setLastAction("raise");
                        game.sendAction(new FCDRaiseAction(this, bet));
                        lastTriedMove = new FCDRaiseAction(this, bet);
                        return;
                    }
                }
            } else {
                pause();
                game.sendAction(new FCDCheckAction(this));
            }
        }else if(savedState.getGameStage() == 2) {
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
