package com.errant01.cardgame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hand {
    private List<Card> cards;
    private boolean sorted = false;
    private boolean evaluated = false;
    private boolean flush = false;
    private boolean straight = false;
    private List<Card> bigGroup = new ArrayList<>();
    private List<Card> smGroup = new ArrayList<>();
    private HandRank rank;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isSorted() {
        return sorted;
    }

    public boolean isFlush() {
        return flush;
    }

    public boolean isStraight() {
        return straight;
    }

    public List<Card> getBigGroup() {
        return bigGroup;
    }

    public List<Card> getSmGroup() {
        return smGroup;
    }

    public HandRank getRank() {
        return rank;
    }

    public void sort() {
        // Use Java Streams with comparator, faster, more compact than old way
        Comparator<Card> comparator = Comparator.comparing(card -> card.getIntegerValue());
        comparator = comparator.reversed().thenComparing(Comparator.comparing(card -> card.getSuit()));

        Stream<Card> cardStream = cards.stream().sorted(comparator);
        cards = cardStream.collect(Collectors.toList());
        sorted = true;
    }

    // TODO convert to scoring method here for performance, especially if project will involve more than two hands or type of game
    public void evaluate() {
        determineFlush();
        determineStraight();
        if (!straight && !flush) {
            determineGroups();
        }
        evaluated = true;
    }

    public void determineRank() {
        if (!evaluated) {
            evaluate();
        }

        if (isStraight()) {
            if (isFlush()) {
                rank = HandRank.STRAIGHT_FLUSH;
                return;
            } else {
                rank = HandRank.STRAIGHT;
                return;
            }
        } else {
            if (isFlush()) {
                rank = HandRank.FLUSH;
                return;
            }
        }

        if (bigGroup.size() == 4) {
            rank = HandRank.FOUR_OF_KIND;
            return;
        } else if (bigGroup.size() == 3) {
            if (smGroup.size() == 2) {
                rank = HandRank.FULL_HOUSE;
                return;
            } else {
                rank = HandRank.THREE_OF_KIND;
                return;
            }
        } else if (bigGroup.size() == 2) {
            if (smGroup.size() == 2) {
                rank = HandRank.TWO_PAIR;
                return;
            } else {
                rank = HandRank.PAIR;
                return;
            }
        }

        rank = HandRank.HIGH_CARD;
    }

    /**
     * For console display
     * @return String in format "[$card, $card, ...]"
     */
    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Card card: cards) {
            sb.append(card.asString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        if (straight) {
            sb.append(" ").append("isStraight");
        }
        if (flush) {
            sb.append(" ").append("isFlush");
        }
        if (!bigGroup.isEmpty()) {
            sb.append(" ").append(bigGroup.size()).append(" of ").append(bigGroup.get(0).getValue());
        }
        if (!smGroup.isEmpty()) {
            sb.append(" ").append(smGroup.size()).append(" of ").append(smGroup.get(0).getValue());
        }
        return sb.toString();
    }

    // All 5 cards must be in a flush
    private void determineFlush() {
        if (!sorted) {
            sort();
        }
        flush = true;
        String compareSuit = cards.get(0).getSuit();
        // only LinkedList suffers for perf on for with counter loops, so ok to use to skip 0 index
        for (int i = 1; i < cards.size(); i++) {
            if (!cards.get(i).getSuit().equals(compareSuit)) {
                flush = false;
                break;
            }
        }
    }

    // All 5 cards must be in a straight
    private void determineStraight() {
        if (!sorted) {
            sort();
        }
        straight = true;
        // check that all cards are a single decrement from previous
        for (int i = 0; i < cards.size() - 1; i++) {
            if (!isTwoCardSeq(cards.get(i), cards.get(i + 1))) {
                straight = false;
                break;
            }
        }
    }

    private boolean isTwoCardSeq(Card c1, Card c2) {
        return c1.getIntegerValue() - c2.getIntegerValue() == 1;
    }

    // groups are only value based
    private void determineGroups() {
        Map<String, List<Card>> mapOfGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        // this group split is specific to 5 card Poker
        for (String value : mapOfGroups.keySet()) {
            if (mapOfGroups.get(value).size() > 1) {
                if (bigGroup.isEmpty()) {
                    bigGroup.addAll(mapOfGroups.get(value));
                } else {
                    // there can be no more than 2 groups of two or more things, so no more groups will match
                    smGroup.addAll(mapOfGroups.get(value));
                }
            }
        }

        if (hasGroups()) {
            orderGroups();
        }
    }

    private void orderGroups() {
        if ((smGroup.size() > bigGroup.size())
                || (smGroup.size() == bigGroup.size() && (smGroup.get(0).getIntegerValue() > bigGroup.get(0).getIntegerValue()))) {
            // swap
            List<Card> tempGroup = bigGroup;
            bigGroup = smGroup;
            smGroup = tempGroup;
        }
    }

    private boolean hasGroups() {
        return bigGroup.size() > 0;
    }
}
