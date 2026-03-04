package testComponent.modifier;

import static org.junit.jupiter.api.Assertions.*;

import component.GameTile;
import component.card.Material;
import component.card.Suit;
import component.modifier.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import component.card.Card;
import component.modifier.combinator.*;
import logic.GameLevel;
import util.CardCount;
import util.GridPos;
import util.TestLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TestCombinator {

    private GameLevel level;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();
    }

    @Test
    void testVaporizerRemovesCardFromTile() {
        Vaporizer vaporizer = new Vaporizer();
        GridPos pos = new GridPos(0, 0);
        vaporizer.setGridPos(pos);

        Card card = new Card(Suit.DIAMOND, 5, Material.PLASTIC);
        level.addCard(card,pos);
        level.getTile(pos).setCard(card);
        vaporizer.modify(card);
        //should be empty after vaporization
        assertNull(level.getTile(pos).getCard());
    }

    @Test
    void testDisabledVaporizerWithCorruptedCard() {
        Vaporizer vaporizer = new Vaporizer();
        GridPos pos = new GridPos(2, 2);
        vaporizer.setGridPos(pos);

        Card card = new Card(Suit.DIAMOND, 5, Material.CORRUPTED);
        level.addCard(card,pos);
        level.getTile(pos).setCard(card);
        //Check if Card still exist and Change to Plastic
        vaporizer.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        //Check if the Vaporizer actually disabled
        vaporizer.modify(card);
        assertNotNull(level.getTile(pos).getCard());
    }

    @Test
    void testVaporizerWithStoneCard() {
        Vaporizer vaporizer = new Vaporizer();
        GridPos pos = new GridPos(2, 2);
        vaporizer.setGridPos(pos);

        Card card = new Card(Suit.DIAMOND, 5, Material.STONE);
        level.addCard(card,pos);
        level.getTile(pos).setCard(card);
        //Check if Stone Card still exist
        vaporizer.modify(card);
        assertEquals(Material.STONE, level.getTile(pos).getCard().getMaterial());
        assertNotNull(level.getTile(pos).getCard());
    }

    @Test
    void testSplitterSplittingCard_Odd() {
        Splitter splitter = new Splitter();
        GridPos pos = new GridPos(1,1);
        splitter.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 7, Material.PLASTIC);
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        //Check first card
        splitter.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        assertEquals(3,level.getTile(pos).getCard().getValue());

        level.getTile(pos).setCard(null);

        //Check second card
        splitter.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        assertEquals(4,level.getTile(pos).getCard().getValue());
    }

    @Test
    void testSplitterSplittingCard_Even() {
        Splitter splitter = new Splitter();
        GridPos pos = new GridPos(1,1);
        splitter.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 4, Material.PLASTIC);
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        //Check first card
        splitter.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        assertEquals(2,level.getTile(pos).getCard().getValue());

        level.getTile(pos).setCard(null);

        //Check second card
        splitter.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        assertEquals(2,level.getTile(pos).getCard().getValue());
    }

    @Test
    void testDisabledSplitterWithCorruptedCard() {
        Splitter splitter = new Splitter();
        GridPos pos = new GridPos(1,1);
        splitter.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 7, Material.CORRUPTED);
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        //Check first card
        splitter.modify(card);
        assertEquals(Material.PLASTIC, level.getTile(pos).getCard().getMaterial());
        assertEquals(7,level.getTile(pos).getCard().getValue());

        level.getTile(pos).setCard(null);

        //Check second card (Shouldn't Exist)
        splitter.modify(card);
        assertNull(level.getTile(pos).getCard());
    }

    @Test
    void testSplitterWithStoneCard() {
        Splitter splitter = new Splitter();
        GridPos pos = new GridPos(1,1);
        splitter.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 7, Material.STONE);
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        //Check first card
        splitter.modify(card);
        assertEquals(Material.STONE, level.getTile(pos).getCard().getMaterial());
        assertEquals(7,level.getTile(pos).getCard().getValue());

        level.getTile(pos).setCard(null);

        //Check second card (Shouldn't Exist)
        splitter.modify(card);
        assertNull(level.getTile(pos).getCard());
    }

    @Test
    void testMerger() {
        Merger merger = new Merger();
        GridPos pos = new GridPos(1,1);
        merger.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.PLASTIC);
        Card card2 = new Card(Suit.HEART, 5, Material.RUBBER);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        merger.modify(card);
        //Card should be consumed
        assertNull(level.getTile(pos).getCard());
        //add second card
        level.addCard(card2,pos);
        level.getTile(pos).setCard(card2);
        merger.modify(card2);
        //Suit and Material Should be the same as the second card
        assertEquals(10,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.HEART,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.RUBBER,level.getTile(pos).getCard().getMaterial());
    }

    @Test
    void testMergerWithCorruptedCard() {
        Merger merger = new Merger();
        GridPos pos = new GridPos(1,1);
        merger.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.CORRUPTED);
        Card card2 = new Card(Suit.HEART, 2, Material.PLASTIC);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        merger.modify(card);
        //Card should not be consumed
        assertNotNull(level.getTile(pos).getCard()); //ERROR
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.CLUB,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.PLASTIC,level.getTile(pos).getCard().getMaterial());
        //add second card
        level.addCard(card2,pos);
        level.getTile(pos).setCard(card2);
        merger.modify(card2);
        //Suit and Material Should be the same as the second card
        assertEquals(2,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.HEART,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.PLASTIC,level.getTile(pos).getCard().getMaterial());
    }

    @Test
    void testMergerWithStoneCard() {
        Merger merger = new Merger();
        GridPos pos = new GridPos(1,1);
        merger.setGridPos(pos);

        Card card = new Card(Suit.SPADE, 5, Material.STONE);
        Card card2 = new Card(Suit.HEART, 2, Material.PLASTIC);
        Card card3 = new Card(Suit.CLUB, 5, Material.STONE);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        merger.modify(card);
        //Card should not be consumed
        assertNotNull(level.getTile(pos).getCard());
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.SPADE,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.STONE,level.getTile(pos).getCard().getMaterial());
        //add second card
        level.getTile(pos).setCard(null); //delete card

        level.addCard(card2, pos);
        level.getTile(pos).setCard(card2);
        merger.modify(card2);
        assertNull(level.getTile(pos).getCard());
        //add third card
        level.addCard(card3, pos);
        level.getTile(pos).setCard(card3);
        merger.modify(card3);
        //should not be doing anything
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.CLUB,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.STONE,level.getTile(pos).getCard().getMaterial());
    }

    @Test
    void testDuplicator() {
        Duplicator duplicator = new Duplicator();
        GridPos pos = new GridPos(1,1);
        duplicator.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.PLASTIC);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);

        duplicator.modify(card);

        level.getTile(pos).setCard(null);

        duplicator.modify(card);

        assertEquals(card.getMaterial() ,level.getTile(pos).getCard().getMaterial());
        assertEquals(card.getValue() ,level.getTile(pos).getCard().getValue());
        assertEquals(card.getSuit() ,level.getTile(pos).getCard().getSuit());
    }

    @Test
    void testDuplicatorWithCorruptedCard() {
        Duplicator duplicator = new Duplicator();
        GridPos pos = new GridPos(1,1);
        duplicator.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.CORRUPTED);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);

        duplicator.modify(card);

        assertEquals(Material.PLASTIC ,level.getTile(pos).getCard().getMaterial());
        level.getTile(pos).setCard(null);

        duplicator.modify(card);
        assertNull(level.getTile(pos).getCard());
    }

    @Test
    void testDuplicatorWithStoneCard() {
        Duplicator duplicator = new Duplicator();
        GridPos pos = new GridPos(1,1);
        duplicator.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.STONE);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);

        duplicator.modify(card);

        assertEquals(Material.STONE ,level.getTile(pos).getCard().getMaterial());
        level.getTile(pos).setCard(null);

        duplicator.modify(card);
        // ใบที่สองต้องไม่ถูกสร้าง
        assertNull(level.getTile(pos).getCard());
    }

    @Test
    void testAbsorber() {
        Absorber absorber = new Absorber();
        GridPos pos = new GridPos(1,1);
        absorber.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.PLASTIC);
        Card card2 = new Card(Suit.HEART, 5, Material.RUBBER);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        absorber.modify(card);
        //Card should be consumed
        assertNull(level.getTile(pos).getCard());
        //add second card
        level.addCard(card2,pos);
        level.getTile(pos).setCard(card2);
        absorber.modify(card2);
        //Suit and Material Should be the same as the second card
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.HEART,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.RUBBER,level.getTile(pos).getCard().getMaterial());
    }

    @Test
    void testAbsorberWithCorruptedCard() {
        Absorber absorber = new Absorber();
        GridPos pos = new GridPos(1,1);
        absorber.setGridPos(pos);

        Card card = new Card(Suit.CLUB, 5, Material.CORRUPTED);
        Card card2 = new Card(Suit.HEART, 2, Material.PLASTIC);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        absorber.modify(card);
        //Card should not be consumed
        assertNotNull(level.getTile(pos).getCard()); //ERROR
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.CLUB,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.PLASTIC,level.getTile(pos).getCard().getMaterial());
        //add second card
        level.addCard(card2,pos);
        level.getTile(pos).setCard(card2);
        absorber.modify(card2);
        //Suit and Material Should be the same as the second card
        assertEquals(2,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.HEART,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.PLASTIC,level.getTile(pos).getCard().getMaterial());
    }

    @Test
    void testAbsorberWithStoneCard() {
        Absorber absorber = new Absorber();
        GridPos pos = new GridPos(1,1);
        absorber.setGridPos(pos);

        Card card = new Card(Suit.SPADE, 5, Material.STONE);
        Card card2 = new Card(Suit.HEART, 2, Material.PLASTIC);
        Card card3 = new Card(Suit.CLUB, 5, Material.STONE);
        //add first card
        level.addCard(card, pos);
        level.getTile(pos).setCard(card);
        absorber.modify(card);
        //Card should not be consumed
        assertNotNull(level.getTile(pos).getCard());
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.SPADE,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.STONE,level.getTile(pos).getCard().getMaterial());
        //add second card
        level.getTile(pos).setCard(null); //delete card

        level.addCard(card2, pos);
        level.getTile(pos).setCard(card2);
        absorber.modify(card2); //card should be consumed
        assertNull(level.getTile(pos).getCard());
        //add third card
        level.addCard(card3, pos);
        level.getTile(pos).setCard(card3);
        absorber.modify(card3);
        //should not be doing anything
        assertEquals(5,level.getTile(pos).getCard().getValue());
        assertEquals(Suit.CLUB,level.getTile(pos).getCard().getSuit());
        assertEquals(Material.STONE,level.getTile(pos).getCard().getMaterial());
    }
}