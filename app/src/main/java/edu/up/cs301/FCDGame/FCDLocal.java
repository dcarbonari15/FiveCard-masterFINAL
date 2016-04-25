package edu.up.cs301.FCDGame;

import android.util.Log;

import java.io.Serializable;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * This is the class that will contain the the Local Game. This is where the action handler and
 * the actions that a "dealer" in a real game of poker will be carried out.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version April 2016
 */
public class FCDLocal extends LocalGame implements FCDGame{

    public FCDState state;
    private boolean foldCase;


    public FCDLocal(){
        state = new FCDState();
        state.shuffle();
        state.shuffle();
        this.dealCards();
    }


    /**
     * checks if the game is over and if there is a winner.
     * @return returns a string to be displayed as a message when there is a winner of the game.
     */
    @Override
    protected String checkIfGameOver() {
        if(!(state.getGameStage() == 4)){
            return null;
        }else{
            Log.e("End Game", "checked and Round is over");
            Log.e(state.getLobby().size()+"", "lobby size");
            for(Player p:state.getLobby()){
                Log.e(state.getLobby().size()+"", "lobby size");
                if(p.getMoney() == 500 * state.getLobby().size()){
                    Log.e(""+p.getMoney(), "checked and Game is over");
                    Log.e("player " + state.getLobby().indexOf(p), " wins!");
                    return "player " + (state.getLobby().indexOf(p) + 1) + " wins!";
                }
            }
        }
        return null;

    }

    /**
     * checks if the round is over and if there is a winner. This resets the pot and awards the
     * appropriate players with their winnings.
     * @return returns a string as a message when there is a winner of the round.
     *
     */
    protected String checkIfRoundOver(){
        Log.e("stage ", "in check it round over");
        //if all but 1 player has folded
        if(foldCase){
            int winnerIndex = 0;
            for(Player p:state.getLobby()){
                if(!(p.isFold())){
                    winnerIndex = state.getLobby().indexOf(p);
                }
            }
            int winningPlayerMoney = state.getLobby().get(winnerIndex).getMoney();
            state.getLobby().get(winnerIndex).setMoney(winningPlayerMoney + state.getPot());
            state.setPot(0);
            Log.e("stage ","foldcase");
            if(checkIfGameOver() == null) {
                Log.e("stage ", state.getGameStage() + "");
                state.advanceGameStage();
                Log.e("stage ", state.getGameStage() + "");
                state.remakeDeck();
                state.shuffle();
                state.shuffle();
                dealCards();
                foldCase = false;
            }
            state.setActivePlayer(0);
        }
        Log.e("stage ", "cehcking for 4");
        Log.e("stage ", state.getGameStage() + "");
        if(!(state.getGameStage() == 4)){
            Log.e("stage not 4", state.getGameStage()+"");
            return null;
        }else{
            Log.e("stage ", "4 found");
            Log.e("stage ", state.getGameStage()+"");
            int nextPlayerHand;
            int bestHand = 0;
            int bestHandIndex = 0;
            int bestSubHand = 0;
            //find out which player won
            for(Player p: state.getLobby()){
                nextPlayerHand = state.handValue(p.getHand());
                if(nextPlayerHand >= bestHand){
                    if(nextPlayerHand == bestHand){
                        int subhandVal = state.subHandValue(p.getHand());
                        if(subhandVal > bestSubHand){
                            bestSubHand = subhandVal;
                            bestHand = nextPlayerHand;
                            bestHandIndex = state.getLobby().indexOf(p);
                        }
                    }else {
                        bestHand = nextPlayerHand;
                        bestHandIndex = state.getLobby().indexOf(p);
                    }
                }
            }
            //give the winning player the money
            int winningPlayerMoney = state.getLobby().get(bestHandIndex).getMoney();
            state.getLobby().get(bestHandIndex).setMoney(winningPlayerMoney + state.getPot());
            state.setPot(0);
            boolean derp = false;
            if(!derp) {
                for (int i = 0; i < players.length; i++) {
                    if (players[i] instanceof FCDHumanPlayer) {
                        ((FCDHumanPlayer) players[i]).revealOpponentsCards();
                    }
                }
                Log.e("stage ", state.getGameStage() + "");
                checkIfGameOver();
                for (Player p : state.getLobby()) {
                    Log.i("sending state", p.toString());
                    sendUpdatedStateTo(p);
                }
                derp = true;
            }
            if((checkIfGameOver() == null) && derp) {
                Log.e("stage ", state.getGameStage() + "");
                state.advanceGameStage();
                Log.e("stage ", state.getGameStage() + "");
                state.remakeDeck();
                state.shuffle();
                state.shuffle();
                dealCards();
            }
            Log.e("exit", "check if game over");
            return "player "  + bestHandIndex + " wins $" + state.getPot() + "!!!";
        }
    }

    /**
     * send the updated gamestate to the specified player
     * @param p --> the player to receive the gamestate
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        if(state == null){
            return;
        }

        FCDState stateForPlayer = new FCDState(state);

        p.sendInfo(stateForPlayer);

        Log.i("sendUpdatedState", "Sent the new state");

    }

    /**
     * This check to see if the specified player can make a move(if its their turn)
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return true if they can make a move false if they cant
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx < 0 || state.getLobby().size() < playerIdx){
            return false;
        }else{
            return state.getActivePlayer() == playerIdx;
        }
    }


    /**\
     *
     * This is the action handler. This method takes in all the actions that are sent to the local
     * game. It find out what action it is and carried out the appropriate updates to the gamestage
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return returns true if that move can be taken and was taken false otherwise
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());
        if(!(action instanceof FCDMoveAction)){
            return false;
        }
        FCDMoveAction move = (FCDMoveAction) action;

        int playerIndex = getPlayerIdx(move.getPlayer());
        if(playerIndex < 0){
            return false;
        }

        if(move.isFold()){
            Log.i("Fold", "isFold");
            state.getLobby().get(playerIndex).fold();
            int foldCount = 0;
            state.setLastAction("fold");
            for(Player p: state.getLobby()){
                if(p.isFold()){
                    foldCount++;
                }
            }
            if(foldCount == state.getLobby().size() -1){
                state.setGameStage(4);
                foldCase = true;
                checkIfRoundOver();
                return true;
            }
            updateGameEssentials(state, playerIndex);
            return true;
        }else if(move.isBet()){
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Bet", "isBet");
                state.setLastAction("raise");
                int playersMoney = state.getLobby().get(playerIndex).getMoney();
                int pot = state.getPot();
                if ((playersMoney) - ((FCDBetAction) move).getbet() < 0) {
                    return false;
                } else {
                    state.getLobby().get(playerIndex).setMoney(playersMoney - ((FCDBetAction) move).getbet());
                    state.setPot(pot + ((FCDBetAction) move).getbet());
                    updateGameEssentials(state, playerIndex);
                    return true;
                }
            }

        }else if(move.isCall()){
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Call", "isCall");
                int tableBet = state.getPot();
                int playerMoney = state.getPlayerMoney(playerIndex);
                int pot = state.getPot();
                if (playerMoney <= 0) {
                    return false;
                } else if (playerMoney - tableBet < 0) {
                    state.setPot(state.getPot() + playerMoney);
                    state.setPlayerMoney(0, playerIndex);
                    updateGameEssentials(state, playerIndex);
                    state.setLastAction("call");
                    return true;
                } else {
                    Log.i("Call", "player calling with enough money");
                    state.setPot(pot + tableBet);
                    state.setPlayerMoney(state.getPlayerMoney(playerIndex) - tableBet, playerIndex);
                    updateGameEssentials(state, playerIndex);
                    state.setLastAction("call");
                    return true;
                }
            }
        }else if(move.isThrow()){
            Log.i("Throw", "isThrow");
            if(state.getGameStage() != 2){
                return false;
            }
            int[] cardsToDiscard = ((FCDThrowAction)move).getIndexOfThrow();
            Card[] cards = new Card[cardsToDiscard.length];
            for(int i = 0; i < cardsToDiscard.length; i++){
                cards[i] = state.getLobby().get(playerIndex).getCard(cardsToDiscard[i]);
            }
            Log.i("PlayerIdx:", Integer.toString(playerIndex));
            Log.i("ThrowingCards:",Integer.toString(cards.length));
            this.playerDiscards(action, playerIndex, cards);
            updateGameEssentials(state, playerIndex);
            return true;
        }else if(move.isCheck()){
            Log.e("Check", "about to check");
            state.setLastAction("check");
            updateGameEssentials(state, playerIndex);
            Log.e("update","updated essentials");
            return true;
        }else if(move.isRaise()) {
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Raise", "isRaise");
                if(state.getPot() == 500*state.getLobby().size()){
                    updateGameEssentials(state, playerIndex);
                }
                if(state.getLastAction().equals("raise")){
                    if(state.getPot() != 500*state.getLobby().size()) {
                        state.downCount();
                    }else{
                        if(state.getGameStage() == 1){
                            state.setGameStage(2);
                        }
                    }
                }
                int tableBet = state.getLastBet();
                int playerMoney = state.getPlayerMoney(playerIndex);
                int pot = state.getPot();
                if ((playerMoney + ((FCDRaiseAction) move).getAmountRaised()) - tableBet < 0) {
                    return false;
                } else {
                    state.setPot(pot + tableBet + ((FCDRaiseAction) move).getAmountRaised());
                    state.setPlayerMoney(playerMoney - tableBet - ((FCDRaiseAction) move).getAmountRaised()
                            , playerIndex);
                    updateGameEssentials(state, playerIndex);
                    state.setLastAction("raise");
                    return true;
                }
            }
        }else if(move.isPass()){
            updateGameEssentials(state, playerIndex);
        }

        return false;

    }

    /**
     * this is a helper method that is called after each action is performed. This method will
     * change the active player and if approriate update the gamestage.
     *
     * @param state --> the current gamestate
     * @param playerIndex --> the current active player
     */
    private void updateGameEssentials(FCDState state, int playerIndex){
        if(playerIndex == 0) {
            state.setActivePlayer(1);
        }else{
            state.setActivePlayer(0);
        }


        state.setCount();
        if(state.getMoveCount() % state.getLobby().size() == 0) {
            if(checkIfGameOver() == null) {
                Log.e("stage ", "advancing");
                state.advanceGameStage();
                Log.e("stage ", state.getGameStage() + "");
            }
        }
        Log.e("stage ", state.getGameStage()+"");
        checkIfRoundOver();
        Log.e("stage ", state.getGameStage() + "");
    }

    /**
     * handles a player discarding 1 or multiple cards (throw)
     *
     * @param sourcePlayer
     *      the player doing the discarding
     * @param card
     *      the array of cards being discarded
     */
    public void playerDiscards(GameAction action, int sourcePlayer, Card[] card){
        if (card.length > 0 && state.getDeck().size() >= card.length) { //if player is able to discard

            FCDMoveAction move = (FCDMoveAction) action;
            Log.i("Discard", "a player is attempting to discard");

            //for each card in discard array
            for (int i = 0; i < card.length; i++) {
                //for each card in player's hand
                for (int j = 0; j < state.getLobby().get(sourcePlayer).getHand().length; j++) {
                    //check if the two cards match
                    if (card[i] == state.getLobby().get(sourcePlayer).getCard(j)) {
                        Log.i("changingCard", "Changing a player's card");
                        Log.i("discardCard:", card[i].toString());
                        Log.i("cardInHand:", state.getLobby().get(sourcePlayer).getCard(j).toString());

                        //set the discarded card to the top card on the deck
                        state.getLobby().get(sourcePlayer).setCard(j, state.getDeck().get(0));
                        Log.i("SourcePlayer:", Integer.toString(sourcePlayer));
                        //remove top card
                        state.getDeck().remove(0);
                    }
                }
            }
            Log.i("SendingState", "sending new state after throwing");
            //send state to the player
            state.getLobby().get(sourcePlayer).sendInfo(new FCDState(state));
            Log.i("SentState", "sent new state after throwing");
        }
    }

    /**
     * deals the cards to all players
     *
     */
    public void dealCards() {
        //for each card in a hand
        for (int i = 0; i < 5; i++){
            //for each player
            for (int j = 0; j < state.getLobby().size(); j++) {
                state.getLobby().get(j).setCard(i, state.getDeck().get(0));
                state.getDealtCards().add(state.getDeck().get(0));
                state.getDeck().remove(0);
            }
        }
        try{
            Thread.sleep(5000);
        } catch (InterruptedException ie){
            //dont care
        }
        for(int j = 0; j < state.getLobby().size(); j++){
            sendUpdatedStateTo(state.getLobby().get(j));
        }
    }
}
