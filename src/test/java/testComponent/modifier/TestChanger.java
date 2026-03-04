package testComponent.modifier;

import component.GameTile;
import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.modifier.changer.*;
import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestChanger {

    private Card plasticCard;
    private Card metalCard;
    private Card rubberCard;
    private GameLevel level;

    @BeforeEach
    void setUp() {
        plasticCard = new Card(Suit.SPADE, 5, Material.PLASTIC);
        metalCard = new Card(Suit.HEART, 5, Material.METAL);
        rubberCard = new Card(Suit.CLUB, 5, Material.RUBBER);

        TestLevel.initGlobalGameLevel();
    }

    @Test
    void testMaterialSetter() {
        MaterialSetter materialSetter1 = new MaterialSetter(Material.GLASS);
        assertEquals(Changer.ChangeType.MATERIAL, materialSetter1.getChangeType());
        assertEquals(Material.GLASS, materialSetter1.getChange());

        materialSetter1.modify(plasticCard);
        assertEquals(Material.GLASS, plasticCard.getMaterial());
    }

    @Test
    void testSuitSetter() {
        SuitSetter suitSetter1 = new SuitSetter(Suit.DIAMOND);
        assertEquals(Changer.ChangeType.SUIT, suitSetter1.getChangeType());
        assertEquals(Suit.DIAMOND, suitSetter1.getChange());

        suitSetter1.modify(plasticCard);
        assertEquals(Suit.DIAMOND, plasticCard.getSuit());
    }

    @Test
    void testValueSetter() {
        ValueSetter valueSetter1 = new ValueSetter(13);
        assertEquals(Changer.ChangeType.NUMBER, valueSetter1.getChangeType());
        assertEquals(13, valueSetter1.getChange());

        valueSetter1.modify(plasticCard);
        assertEquals(13, plasticCard.getValue());

        valueSetter1.setChange(15);
        assertEquals(13, valueSetter1.getChange());
        valueSetter1.setChange(-1);
        assertEquals(1, valueSetter1.getChange());
    }

    @Test
    void testAdder() {
        Adder adder1 = new Adder(2);
        assertEquals(Changer.ChangeType.NUMBER, adder1.getChangeType());
        assertEquals(2, adder1.getChange());

        adder1.modify(plasticCard);
        assertEquals(7, plasticCard.getValue());

        adder1.modify(plasticCard);
        adder1.modify(plasticCard);
        adder1.modify(plasticCard);
        adder1.modify(plasticCard);
        assertEquals(2, plasticCard.getValue());
    }

    @Test
    void testSubtractor() {
        Subtractor subtractor1 = new Subtractor(2);
        assertEquals(Changer.ChangeType.NUMBER, subtractor1.getChangeType());
        assertEquals(2, subtractor1.getChange());

        subtractor1.modify(plasticCard);
        assertEquals(3, plasticCard.getValue());

        subtractor1.modify(plasticCard);
        subtractor1.modify(plasticCard);
        assertEquals(12, plasticCard.getValue());
    }

    @Test
    void testMultiplier() {
        Multiplier multiplier1 = new Multiplier(2);
        assertEquals(Changer.ChangeType.NUMBER, multiplier1.getChangeType());
        assertEquals(2, multiplier1.getChange());

        multiplier1.modify(plasticCard);
        assertEquals(10, plasticCard.getValue());

        multiplier1.modify(plasticCard);
        assertEquals(7, plasticCard.getValue());
    }

    @Test
    void testDivider() {
        Divider divider1 = new Divider(2);
        assertEquals(Changer.ChangeType.NUMBER, divider1.getChangeType());
        assertEquals(2, divider1.getChange());

        divider1.modify(plasticCard);
        assertEquals(2, plasticCard.getValue());

        divider1.modify(plasticCard);
        divider1.modify(plasticCard);
        assertEquals(13, plasticCard.getValue());
    }

    @Test
    void testNegativeAdder() {
        Adder adder1 = new Adder(-5);
        assertEquals(0, adder1.getChange());

        adder1.setChange(6);
        assertEquals(6, adder1.getChange());
        adder1.setChange(-5);
        assertEquals(0, adder1.getChange());
    }

    @Test
    void testNegativeSubtractor() {
        Subtractor subtractor1 = new Subtractor(-10);
        assertEquals(0, subtractor1.getChange());

        subtractor1.setChange(6);
        assertEquals(6, subtractor1.getChange());
        subtractor1.setChange(-5);
        assertEquals(0, subtractor1.getChange());
    }

    @Test
    void testNegativeMultiplier() {
        Multiplier multiplier1 = new Multiplier(0);
        Multiplier multiplier2 = new Multiplier(-5);
        assertEquals(1, multiplier1.getChange());
        assertEquals(1, multiplier2.getChange());

        multiplier1.setChange(6);
        assertEquals(6, multiplier1.getChange());
        multiplier1.setChange(-3);
        assertEquals(1, multiplier1.getChange());
    }

    @Test
    void testNegativeDivider() {
        Divider divider1 = new Divider(0);
        Divider divider2 = new Divider(-5);
        assertEquals(1, divider1.getChange());
        assertEquals(1, divider2.getChange());

        divider1.setChange(6);
        assertEquals(6, divider1.getChange());
        divider1.setChange(-3);
        assertEquals(1, divider1.getChange());
    }


    @Test
    void testMetalImmunityToArithmetic() {
        Adder adder1 = new Adder(3);
        Subtractor subtractor1 = new Subtractor(5);
        Multiplier multiplier1 = new Multiplier(4);
        Divider divider1 = new Divider(5);

        adder1.modify(metalCard);
        assertEquals(5, metalCard.getValue());
        subtractor1.modify(metalCard);
        assertEquals(5, metalCard.getValue());
        multiplier1.modify(metalCard);
        assertEquals(5, metalCard.getValue());
        divider1.modify(metalCard);
        assertEquals(5, metalCard.getValue());
    }

    @Test
    void testMetalNotImmuneToSetters() {
        SuitSetter suitSetter1 = new SuitSetter(Suit.DIAMOND);
        ValueSetter valueSetter1 = new ValueSetter(10);
        MaterialSetter materialSetter1 = new MaterialSetter(Material.PLASTIC);

        suitSetter1.modify(metalCard);
        assertEquals(Suit.DIAMOND, metalCard.getSuit());
        valueSetter1.modify(metalCard);
        assertEquals(10, metalCard.getValue());
        materialSetter1.modify(metalCard);
        assertEquals(Material.PLASTIC, metalCard.getMaterial());
    }

    @Test
    void testRubberDoubleEffect() {
        Adder adder1 = new Adder(2);
        Subtractor subtractor1 = new Subtractor(1);
        Multiplier multiplier1 = new Multiplier(2);
        Divider divider1 = new Divider(2);
        ValueSetter valueSetter1 = new ValueSetter(10);
        SuitSetter suitSetter1 = new SuitSetter(Suit.SPADE);
        MaterialSetter materialSetter1 = new MaterialSetter(Material.GLASS);

        rubberCard.setValue(5);
        adder1.modify(rubberCard);
        assertEquals(9, rubberCard.getValue());

        rubberCard.setValue(5);
        subtractor1.modify(rubberCard);
        assertEquals(3, rubberCard.getValue());

        rubberCard.setValue(3);
        multiplier1.modify(rubberCard);
        assertEquals(12, rubberCard.getValue());

        rubberCard.setValue(12);
        divider1.modify(rubberCard);
        assertEquals(3, rubberCard.getValue());

        valueSetter1.modify(rubberCard);
        assertEquals(10, rubberCard.getValue());

        suitSetter1.modify(rubberCard);
        assertEquals(Suit.SPADE, rubberCard.getSuit());

        materialSetter1.modify(rubberCard);
        assertEquals(Material.GLASS, rubberCard.getMaterial());
    }
}