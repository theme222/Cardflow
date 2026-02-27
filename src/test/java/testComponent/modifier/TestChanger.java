package testComponent.modifier;

import component.card.Card;
import component.modifier.changer.*;
        import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestChanger {

    private Card plasticCard;
    private Card metalCard;
    private Card rubberCard;

    @BeforeEach
    void setUp() {
        // TODO: สร้าง Dummy GameLevel และเตรียมไพ่ Plastic, Metal, Rubber ไว้ใช้เทส
        GameLevel dummyLevel = new GameLevel("Dummy", 9, 9, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new component.GameTile[9][9], new HashSet<>());
        GameLevel.setInstance(dummyLevel);
        plasticCard = new Card(Card.Suit.SPADE, 5, Card.Material.PLASTIC);
        metalCard = new Card(Card.Suit.HEART, 5, Card.Material.METAL);
        rubberCard = new Card(Card.Suit.CLUB, 5, Card.Material.RUBBER);
    }

    // ==========================================
    // 1. SETTER TESTS (เทสพวกเปลี่ยนค่า เปลี่ยนสี เปลี่ยนวัสดุ)
    // ==========================================

    @Test
    void testMaterialSetter() {
        // TODO: เทสการเปลี่ยน Material และเช็ค ChangeType
        MaterialSetter materialSetter1 = new MaterialSetter(Card.Material.GLASS);
        assertEquals(Changer.ChangeType.MATERIAL, materialSetter1.getChangeType());
        assertEquals(Card.Material.GLASS, materialSetter1.getChange());

        materialSetter1.modify(plasticCard);
        assertEquals(Card.Material.GLASS, plasticCard.getMaterial());
    }

    @Test
    void testSuitSetter() {
        // TODO: เทสการเปลี่ยน Suit และเช็ค ChangeType
        SuitSetter suitSetter1 = new SuitSetter(Card.Suit.DIAMOND);
        assertEquals(Changer.ChangeType.SUIT, suitSetter1.getChangeType());
        assertEquals(Card.Suit.DIAMOND, suitSetter1.getChange());

        suitSetter1.modify(plasticCard);
        assertEquals(Card.Suit.DIAMOND, plasticCard.getSuit());
    }

    @Test
    void testValueSetter() {
        // TODO: เทสการเปลี่ยน Value และเช็คการ Clamp ผ่าน setChange(1-13)
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

    // ==========================================
    // 2. ARITHMETIC NORMAL TESTS (เทสการคำนวณคณิตศาสตร์ปกติ)
    // ==========================================

    @Test
    void testAdder() {
        // TODO: เทสการบวกเลข และเช็ค setChange ไม่ให้ติดลบ
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
        // TODO: เทสการลบเลข และเช็ค setChange ไม่ให้ติดลบ
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
        // TODO: เทสการคูณเลข และเช็ค setChange ห้ามเป็น 0 หรือติดลบ
        Multiplier multiplier1 = new Multiplier(2);
        assertEquals(Changer.ChangeType.NUMBER, multiplier1.getChangeType());
        assertEquals(2, multiplier1.getChange());

        multiplier1.modify(plasticCard);
        assertEquals(10, plasticCard.getValue());

        multiplier1.modify(plasticCard);
        assertEquals(6, plasticCard.getValue());
    }

    @Test
    void testDivider() {
        // TODO: เทสการหารเลข และเช็ค setChange ห้ามเป็น 0 หรือติดลบ
        Divider divider1 = new Divider(2);
        assertEquals(Changer.ChangeType.NUMBER, divider1.getChangeType());
        assertEquals(2, divider1.getChange());

        divider1.modify(plasticCard);
        assertEquals(2, plasticCard.getValue());

        divider1.modify(plasticCard);
        divider1.modify(plasticCard);
        assertEquals(1, plasticCard.getValue());
    }

    // ==========================================
    // 3. Negative TESTS
    // ==========================================

    @Test
    void testNegativeAdder() {
        // TODO: เทส new Adder(-5) ค่าต้องโดนปัดเป็น 0
        Adder adder1 = new Adder(-5);
        assertEquals(0, adder1.getChange());

        adder1.setChange(6);
        assertEquals(6, adder1.getChange());
        adder1.setChange(-5);
        assertEquals(0, adder1.getChange());
    }

    @Test
    void testNegativeSubtractor() {
        // TODO: เทส new Subtractor(-10) ค่าต้องโดนปัดเป็น 0
        Subtractor subtractor1 = new Subtractor(-10);
        assertEquals(0, subtractor1.getChange());

        subtractor1.setChange(6);
        assertEquals(6, subtractor1.getChange());
        subtractor1.setChange(-5);
        assertEquals(0, subtractor1.getChange());
    }

    @Test
    void testNegativeMultiplier() {
        // TODO: เทส new Multiplier(0) และ (-5) ค่าต้องโดนปัดเป็น 1
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
        // TODO: เทส new Divider(0) และ (-5) ค่าต้องโดนปัดเป็น 1
        Divider divider1 = new Divider(0);
        Divider divider2 = new Divider(-5);
        assertEquals(1, divider1.getChange());
        assertEquals(1, divider2.getChange());

        divider1.setChange(6);
        assertEquals(6, divider1.getChange());
        divider1.setChange(-3);
        assertEquals(1, divider1.getChange());
    }

    // ==========================================
    // 4. MATERIAL SPECIAL RULES (เทสลอจิกพิเศษของไพ่เหล็กและไพ่ยาง)
    // ==========================================

    @Test
    void testMetalImmunityToArithmetic() {
        // TODO: เทสไพ่ METAL โดน Arithmetic (เช่น Adder) แล้วค่าต้องไม่เปลี่ยน
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
        // TODO: เทสไพ่ METAL โดน Setter (เช่น SuitSetter) แล้วค่าต้องเปลี่ยนได้ปกติ
        SuitSetter suitSetter1 = new SuitSetter(Card.Suit.DIAMOND);
        ValueSetter valueSetter1 = new ValueSetter(10);
        MaterialSetter materialSetter1 = new MaterialSetter(Card.Material.PLASTIC);

        suitSetter1.modify(metalCard);
        assertEquals(Card.Suit.DIAMOND, metalCard.getSuit());
        valueSetter1.modify(metalCard);
        assertEquals(10, metalCard.getValue());
        materialSetter1.modify(metalCard);
        assertEquals(Card.Material.PLASTIC, metalCard.getMaterial());
    }

    @Test
    void testRubberDoubleEffect() {
        // TODO: เทสไพ่ RUBBER โดนเอฟเฟกต์ (เช่น Adder) แล้วต้องแสดงผล 2 รอบ (เบิ้ลเอฟเฟกต์)
        Adder adder1 = new Adder(2);
        Subtractor subtractor1 = new Subtractor(1);
        Multiplier multiplier1 = new Multiplier(2);
        Divider divider1 = new Divider(2);
        ValueSetter valueSetter1 = new ValueSetter(10);
        SuitSetter suitSetter1 = new SuitSetter(Card.Suit.SPADE);
        MaterialSetter materialSetter1 = new MaterialSetter(Card.Material.GLASS);

        // เทส Adder (เริ่มที่ 5 -> โดนบวก 2 สองรอบ = 9)
        rubberCard.setValue(5);
        adder1.modify(rubberCard);
        assertEquals(9, rubberCard.getValue());

        // เทส Subtractor (เริ่มที่ 5 -> โดนลบ 1 สองรอบ = 3)
        rubberCard.setValue(5);
        subtractor1.modify(rubberCard);
        assertEquals(3, rubberCard.getValue());

        // เทส Multiplier (เริ่มที่ 3 -> โดนคูณ 2 สองรอบ = 12)
        rubberCard.setValue(3);
        multiplier1.modify(rubberCard);
        assertEquals(12, rubberCard.getValue());

        // เทส Divider (เริ่มที่ 12 -> โดนหาร 2 สองรอบ = 3)
        rubberCard.setValue(12);
        divider1.modify(rubberCard);
        assertEquals(3, rubberCard.getValue());

        // เทส Setters (เปลี่ยนค่าทับ 2 รอบ ผลลัพธ์ต้องออกมาตามที่ตั้งไว้)
        valueSetter1.modify(rubberCard);
        assertEquals(10, rubberCard.getValue());

        suitSetter1.modify(rubberCard);
        assertEquals(Card.Suit.SPADE, rubberCard.getSuit());

        materialSetter1.modify(rubberCard);
        assertEquals(Card.Material.GLASS, rubberCard.getMaterial());

    }
}