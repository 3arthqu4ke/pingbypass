package me.earth.pingbypass.api.config.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

// SOFTTODO: move to security-lib?
public class PropertyHelperTest {
    @Test
    public void testString() {
        var test = genAndTestProperty(PropertyHelper::string, "test", "test");
        Assertions.assertEquals("t", test.parse("t"));
    }

    @Test
    public void testArray() {
        var test = PropertyHelper.array("test", ";", "1", "2");
        assertEquals("test", test.getName());
        assertEquals(2, test.getDefaultValue().length);
        assertEquals("1", test.getDefaultValue()[0]);
        assertEquals("2", test.getDefaultValue()[1]);
        assertArrayEquals(new String[]{"3", "4"}, test.parse("3;4"));
        assertNull(test.parse(null));
        assertNull(test.parse(""));
    }

    @Test
    public void testNumber() {
        var test = genAndTestProperty(PropertyHelper::number, "test2", 1);
        assertThrows(NumberFormatException.class, () -> test.parse("test"));
        Assertions.assertEquals(5, test.parse("5"));
    }

    @Test
    public void testBool() {
        var test = genAndTestProperty(PropertyHelper::bool, "test", false);
        Assertions.assertTrue(test.parse("true"));
        Assertions.assertFalse(test.parse("false"));
        Assertions.assertTrue(test.parse("TRUE"));
    }

    @Test
    public void testConstant() {
        var test = PropertyHelper.constant("test", SomeEnum.class, SomeEnum.ONE);
        testValues(test, "test", SomeEnum.ONE);
        assertEquals(SomeEnum.ONE, test.parse("ONE"));
        assertEquals(SomeEnum.TWO, test.parse("Two"));
        assertEquals(SomeEnum.THREE, test.parse("three"));
        assertNull(test.parse("?"));
    }

    private <T> Property<T> genAndTestProperty(BiFunction<String, T, Property<T>> gen, String name, T def) {
        var property = gen.apply(name, def);
        testValues(property, name, def);
        return property;
    }

    private <T> void testValues(Property<T> property, String name, T def) {
        assertNull(property.parse(null));
        assertEquals(name, property.getName());
        assertEquals(def, property.getDefaultValue());
    }

    enum SomeEnum {
        ONE,
        TWO,
        THREE
    }

}
