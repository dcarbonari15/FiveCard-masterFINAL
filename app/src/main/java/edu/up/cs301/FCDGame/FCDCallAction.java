package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by carbonar19 on 4/12/2016.
 */
public class FCDCallAction extends FCDMoveAction{

    private static final long serialVersionUID = 23972039751L;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDCallAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return
     * 		whether this action is a "call" move
     */
    @Override
    public boolean isCall(){
        return true;
    }
}
