package edu.up.cs301.FCDGame;

import org.junit.Test;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

import static org.junit.Assert.*;

/**
 * Created by carbonar19 on 3/30/2016.
 */
public class FCDStateTest {

    @Test
    public void testHandValue() throws Exception {
        FCDState testState = new FCDState();
        //every possible type of hand
        Card[] testHighCard = {new Card(Rank.ACE, Suit.Club), new Card(Rank.EIGHT, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FOUR, Suit.Diamond)};
        Card[] testPair = {new Card(Rank.ACE, Suit.Club), new Card(Rank.EIGHT, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testTwoPair = {new Card(Rank.ACE, Suit.Club), new Card(Rank.ACE, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Diamond)};
        Card[] testTrip = {new Card(Rank.ACE, Suit.Club), new Card(Rank.FIVE, Suit.Club),
                new Card(Rank.FIVE, Suit.Club), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Diamond)};
        Card[] testStraight = {new Card(Rank.ACE, Suit.Club), new Card(Rank.TWO, Suit.Club),
                new Card(Rank.THREE, Suit.Diamond), new Card(Rank.FOUR, Suit.Heart),
                new Card(Rank.FIVE, Suit.Diamond)};
        Card[] testFlush = {new Card(Rank.ACE, Suit.Diamond), new Card(Rank.EIGHT, Suit.Diamond),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Diamond),
                new Card(Rank.SEVEN, Suit.Diamond)};
        Card[] testFullHouse = {new Card(Rank.ACE, Suit.Club), new Card(Rank.ACE, Suit.Diamond),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.ACE, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testQuad = {new Card(Rank.ACE, Suit.Club), new Card(Rank.FIVE, Suit.Spade),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.FIVE, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testStraightFlush = {new Card(Rank.ACE, Suit.Club), new Card(Rank.TWO, Suit.Club),
                new Card(Rank.THREE, Suit.Club), new Card(Rank.FOUR, Suit.Club),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testRoyal = {new Card(Rank.ACE, Suit.Club), new Card(Rank.KING, Suit.Club),
                new Card(Rank.QUEEN, Suit.Club), new Card(Rank.JACK, Suit.Club),
                new Card(Rank.TEN, Suit.Club)};
        int testHighCardHandVal = testState.handValue(testHighCard);
        int testPairHandVal = testState.handValue(testPair);
        int testTwoPairHandVal = testState.handValue(testTwoPair);
        int testTripHandVal = testState.handValue(testTrip);
        int testStraightHandVal = testState.handValue(testStraight);
        int testFlushHandVal = testState.handValue(testFlush);
        int testFullHouseHandVal = testState.handValue(testFullHouse);
        int testQuadHandVal = testState.handValue(testQuad);
        int testStraightFlushHandVal = testState.handValue(testStraightFlush);
        int testRoyalHandVal = testState.handValue(testRoyal);

        assertEquals("Did not value high card correctly", 0, testHighCardHandVal);
        assertEquals("Did not value pair correctly", 1, testPairHandVal);
        assertEquals("Did not value two pair correctly", 2, testTwoPairHandVal);
        assertEquals("Did not value trip correctly", 3, testTripHandVal);
        assertEquals("Did not value straight correctly", 4, testStraightHandVal);
        assertEquals("Did not value flush correctly", 5, testFlushHandVal);
        assertEquals("Did not value full house correctly", 6, testFullHouseHandVal);
        assertEquals("Did not value quad correctly", 7, testQuadHandVal);
        //TODO  fix --> assertEquals("Did not value straight flush correctly", 8, testStraightFlushHandVal);
        //TODO  fix --> assertEquals("Did not value royal correctly", 9, testRoyalHandVal);


    }

    @Test
    public void testSubHandValue() throws Exception {
        FCDState testState = new FCDState();
        //every possible type of hand
        Card[] testHighCard = {new Card(Rank.ACE, Suit.Club), new Card(Rank.EIGHT, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FOUR, Suit.Diamond)};
        Card[] testPair = {new Card(Rank.ACE, Suit.Club), new Card(Rank.EIGHT, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testTwoPair = {new Card(Rank.ACE, Suit.Diamond), new Card(Rank.ACE, Suit.Club),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testTrip = {new Card(Rank.ACE, Suit.Club), new Card(Rank.FIVE, Suit.Club),
                new Card(Rank.FIVE, Suit.Club), new Card(Rank.JACK, Suit.Heart),
                new Card(Rank.FIVE, Suit.Diamond)};
        Card[] testStraight = {new Card(Rank.ACE, Suit.Club), new Card(Rank.TWO, Suit.Club),
                new Card(Rank.THREE, Suit.Diamond), new Card(Rank.FOUR, Suit.Heart),
                new Card(Rank.FIVE, Suit.Diamond)};
        Card[] testFlush = {new Card(Rank.ACE, Suit.Diamond), new Card(Rank.EIGHT, Suit.Diamond),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.JACK, Suit.Diamond),
                new Card(Rank.SEVEN, Suit.Diamond)};
        Card[] testFullHouse = {new Card(Rank.ACE, Suit.Club), new Card(Rank.ACE, Suit.Diamond),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.ACE, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testQuad = {new Card(Rank.ACE, Suit.Club), new Card(Rank.FIVE, Suit.Spade),
                new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.FIVE, Suit.Heart),
                new Card(Rank.FIVE, Suit.Club)};
        Card[] testStraightFlush = {new Card(Rank.ACE, Suit.Club), new Card(Rank.TWO, Suit.Club),
                new Card(Rank.THREE, Suit.Club), new Card(Rank.FOUR, Suit.Club),
                new Card(Rank.FIVE, Suit.Club)};

        int testHighCardHandVal = testState.subHandValue(testHighCard);
        int testPairHandVal = testState.subHandValue(testPair);
        int testTwoPairHandVal = testState.subHandValue(testTwoPair);
        int testTripHandVal = testState.subHandValue(testTrip);
        int testStraightHandVal = testState.subHandValue(testStraight);
        int testFlushHandVal = testState.subHandValue(testFlush);
        int testFullHouseHandVal = testState.subHandValue(testFullHouse);
        int testQuadHandVal = testState.subHandValue(testQuad);
        int testStraightFlushHandVal = testState.subHandValue(testStraightFlush);

        assertEquals("Did not value high card correctly", 15, testHighCardHandVal);
        assertEquals("Did not value pair correctly", 5, testPairHandVal);
        assertEquals("Did not value two pair correctly", 15, testTwoPairHandVal);
        assertEquals("Did not value trip correctly", 5, testTripHandVal);
        assertEquals("Did not value straight correctly", 1, testStraightHandVal);
        assertEquals("Did not value flush correctly", 15, testFlushHandVal);
        assertEquals("Did not value full house correctly", 15, testFullHouseHandVal);
        assertEquals("Did not value quad correctly", 5, testQuadHandVal);
        assertEquals("Did not value straight flush correctly", 1, testStraightFlushHandVal);
    }


    @Test
    public void testModifyPot() throws Exception {
        FCDState testState = new FCDState();
        int testPot = testState.getPot();

        //start with adding 0
        testState.modifyPot(0);
        assertEquals(testPot, testState.getPot());

        //add
        testState.modifyPot(10);
        assertEquals(testPot + 10, testState.getPot());

        //subtract
        testState.modifyPot(-10);
        assertEquals(testPot, testState.getPot());
    }

    @Test
    public void testModifyPlayerMoney() throws Exception {
        FCDState testState = new FCDState();
        int testPlayer1Money = testState.getPlayerMoney(0);
        int testPlayer2Money = testState.getPlayerMoney(1);

        //start with adding 0
        testState.modifyPlayerMoney(0, 0);
        assertEquals(testPlayer1Money, testState.getPlayerMoney(0));
        testState.modifyPlayerMoney(1, 0);
        assertEquals(testPlayer2Money, testState.getPlayerMoney(1));

        //add
        testState.modifyPlayerMoney(0, 10);
        assertEquals(testPlayer1Money + 10, testState.getPlayerMoney(0));
        testState.modifyPlayerMoney(1, 10);
        assertEquals(testPlayer2Money + 10, testState.getPlayerMoney(1));

        //subtract
        testState.modifyPlayerMoney(0, -10);
        assertEquals(testPlayer1Money, testState.getPlayerMoney(0));
        testState.modifyPlayerMoney(1, -10);
        assertEquals(testPlayer2Money, testState.getPlayerMoney(1));

    }

    @Test
    public void testModifyPlayerBet() throws Exception {
        FCDState testState = new FCDState();
        int testPlayer1Bet = testState.getPlayerBet(0);
        int testPlayer2Bet = testState.getPlayerBet(1);

        //start with adding 0
        testState.modifyPlayerBet(0, 0);
        assertEquals(testPlayer1Bet, testState.getPlayerBet(0));
        testState.modifyPlayerBet(1, 0);
        assertEquals(testPlayer2Bet, testState.getPlayerBet(1));

        //add
        testState.modifyPlayerBet(0, 10);
        assertEquals(testPlayer1Bet + 10, testState.getPlayerBet(0));
        testState.modifyPlayerBet(1, 10);
        assertEquals(testPlayer2Bet + 10, testState.getPlayerBet(1));

        //subtract
        testState.modifyPlayerBet(0, -10);
        assertEquals(testPlayer1Bet, testState.getPlayerBet(0));
        testState.modifyPlayerBet(1, -10);
        assertEquals(testPlayer2Bet, testState.getPlayerBet(1));
    }

    @Test
    public void testPlayerWins() throws Exception {
        FCDState testState = new FCDState();
        testState.setPlayerMoney(1000, 0);
        testState.setPlayerMoney(1000, 1);
        testState.setPot(100);

        //test player 1 winning
        testState.playerWins(0);
        assertEquals(1100, testState.getPlayerMoney(0));

        //test player 2 winning
        testState.playerWins(1);
        assertEquals(1100, testState.getPlayerMoney(1));

        //test player 2 winning again
        testState.playerWins(1);
        assertEquals(1200, testState.getPlayerMoney(1));

    }

    @Test
    public void testPlayerFolds() throws Exception {
        //new state, setting playerFolds to false
        FCDState testState = new FCDState();
        testState.setPlayerMoney(1000, 0);
        testState.setPlayerMoney(1000, 1);
        testState.setPot(100);

        //test player 1 folding
        testState.playerFolds(0);
        assertEquals(true, testState.isPlayerFold(0));

        //test player 2 folding
        testState.playerFolds(1);
        assertEquals(true, testState.isPlayerFold(1));

    }

    @Test
    public void testPlayerCalls() throws Exception {
        FCDState testState = new FCDState();
        testState.setPlayerBet(10, 0);
        testState.setPlayerBet(10, 1);
        testState.setPlayerMoney(1000, 0);
        testState.setPlayerMoney(1000, 1);
        testState.setPot(100);

        //test player 1 calling
        testState.playerCalls(0, 10);
        assertEquals(20, testState.getPlayerBet(0));

        //test player 1 calling again
        testState.playerCalls(0, 20);
        assertEquals(40, testState.getPlayerBet(0));

        //test player 2 calling
        testState.playerCalls(1, 10);
        assertEquals(20, testState.getPlayerBet(1));

    }

    @Test
    public void testPlayerRaises() throws Exception {
        FCDState testState = new FCDState();
        testState.setPlayerBet(10, 0);
        testState.setPlayerBet(10, 1);
        testState.setPlayerMoney(1000, 0);
        testState.setPlayerMoney(1000, 1);
        testState.setPot(100);

        //test player 1 raising
        testState.playerRaises(0, 10, 10);
        assertEquals(30, testState.getPlayerBet(0));

        //test player 1 raising again
        testState.playerRaises(0, 20, 20);
        assertEquals(70, testState.getPlayerBet(0));

        //test player 2 raising
        testState.playerRaises(1, 10, 10);
        assertEquals(30, testState.getPlayerBet(1));
    }


    @Test
    public void testShuffle() throws Exception {
        FCDState testState = new FCDState();

        //shuffle
        testState.shuffle();

        //ranks and suits
        Rank[] cardVals = {Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN,
                Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING};
        Suit[] cardSuits = {Suit.Club, Suit.Diamond, Suit.Heart, Suit.Spade};

        int cardCount = 0;//counter
        boolean allCardsEqual = true;//is set to false if two unequal cards are found
        Card[] testDeck = new Card[52];//a brand new test deck

        //initializes the test deck
        for (int i = 0; i < 4; i++) { //for each suit
            for (int j = 0; j < 13; j++) { //for each rank
                testDeck[cardCount] = new Card(cardVals[j], cardSuits[i]);
                cardCount++;
            }
        }

        //checks to make sure cards are different (were shuffled)
        cardCount = 0;
        for (int i = 0; i < 4; i++) { //for each suit
            for (int j = 0; j < 13; j++) { //for each rank
                if (testState.getDeck().get(cardCount) != testDeck[cardCount]) {//if cards are equal
                    allCardsEqual = false;
                }
                cardCount++;
            }
        }

        //make sure the deck was shuffled (fails if shuffle happens to put all cards in order,
        //but extremely rare)
        assertEquals(false, allCardsEqual);
    }

}
