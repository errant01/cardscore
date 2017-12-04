package com.errant01.cardgame;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HandTest {

    @Test
    public void sort() throws Exception {
        Hand h = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        h.sort();
        assertTrue(h.isSorted());
        // tests value sort and suit sort
        assertEquals(h.getCards().get(0).getValue(), TestHands.TWO_PAIR[4].getValue());
        assertEquals(h.getCards().get(0).getSuit(), TestHands.TWO_PAIR[4].getSuit());
        assertEquals(h.getCards().get(1).getValue(), TestHands.TWO_PAIR[2].getValue());
        assertEquals(h.getCards().get(1).getSuit(), TestHands.TWO_PAIR[2].getSuit());
    }

    @Test
    public void evaluateFlush() throws Exception {
        Hand falseHand = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        falseHand.evaluate();
        assertFalse(falseHand.isFlush());

        Hand trueHand = new Hand(Arrays.asList(TestHands.FLUSH));
        trueHand.evaluate();
        assertTrue(trueHand.isFlush());
    }

    @Test
    public void evaluateStraight() throws Exception {
        Hand falseHand = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        falseHand.evaluate();
        assertFalse(falseHand.isFlush());

        Hand trueHand = new Hand(Arrays.asList(TestHands.STRAIGHT));
        trueHand.evaluate();
        assertTrue(trueHand.isStraight());
    }

    @Test
    public void determineNoGroups() throws Exception {
        Hand noGroup = new Hand(Arrays.asList(TestHands.HIGH_CARD));
        noGroup.evaluate();
        assertTrue(noGroup.getBigGroup().isEmpty());
        assertTrue(noGroup.getSmGroup().isEmpty());
    }

    @Test
    public void determineOneGroup() throws Exception {
        Hand noGroup = new Hand(Arrays.asList(TestHands.ONE_PAIR));
        noGroup.evaluate();
        assertFalse(noGroup.getBigGroup().isEmpty());
        assertTrue(noGroup.getSmGroup().isEmpty());
    }

    @Test
    public void determineTwoGroup() throws Exception {
        Hand noGroup = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        noGroup.evaluate();
        assertFalse(noGroup.getBigGroup().isEmpty());
        assertFalse(noGroup.getSmGroup().isEmpty());
    }

    @Test
    public void determineBigGroupIsBigger() throws Exception {
        Hand noGroup = new Hand(Arrays.asList(TestHands.FULL_HOUSE));
        noGroup.evaluate();
        assertTrue(noGroup.getBigGroup().size() > noGroup.getSmGroup().size());
    }

    @Test
    public void determineBigGroupIsHigherWhenPairs() throws Exception {
        Hand noGroup = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        noGroup.evaluate();
        assertTrue(noGroup.getBigGroup().get(0).getIntValue() > noGroup.getSmGroup().get(0).getIntValue());
    }

    @Test
    public void rankStraightFlush() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.STRAIGHT_FLUSH));
        hand.determineRank();
        assertEquals(HandRank.STRAIGHT_FLUSH, hand.getRank());
    }

    @Test
    public void rankFourOfaKind() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.FOUR_KIND));
        hand.determineRank();
        assertEquals(HandRank.FOUR_OF_KIND, hand.getRank());
    }

    @Test
    public void rankFullHouse() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.FULL_HOUSE));
        hand.determineRank();
        assertEquals(HandRank.FULL_HOUSE, hand.getRank());
    }

    @Test
    public void rankFlush() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.FLUSH));
        hand.determineRank();
        assertEquals(HandRank.FLUSH, hand.getRank());
    }

    @Test
    public void rankStraight() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.STRAIGHT));
        hand.determineRank();
        assertEquals(HandRank.STRAIGHT, hand.getRank());
    }

    @Test
    public void rankThreeOfaKind() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.THREE_KIND));
        hand.determineRank();
        assertEquals(HandRank.THREE_OF_KIND, hand.getRank());
    }

    @Test
    public void rankTwoPair() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.TWO_PAIR));
        hand.determineRank();
        assertEquals(HandRank.TWO_PAIR, hand.getRank());
    }

    @Test
    public void rankOnePair() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.ONE_PAIR));
        hand.determineRank();
        assertEquals(HandRank.PAIR, hand.getRank());
    }

    @Test
    public void rankHighCard() throws Exception {
        Hand hand = new Hand(Arrays.asList(TestHands.HIGH_CARD));
        hand.determineRank();
        assertEquals(HandRank.HIGH_CARD, hand.getRank());
    }
}