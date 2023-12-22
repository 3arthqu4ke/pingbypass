package me.earth.pingbypass.api.event.listeners.generic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

public class TypeHelperTest {
    private static final List<Set<Integer>> LIST_SET_INTEGER = emptyList();

    @Test
    public void testExceptions() {
        CannotFindTypeException e = assertThrows(
            CannotFindTypeException.class,
            () -> TypeHelper.getTypeInfo(Object.class));
        assertTrue(e.getMessage().endsWith("seem to be generic!"));

        e = assertThrows(
            CannotFindTypeException.class,
            () -> TypeHelper.getTypeInfo(LIST_SET_INTEGER.getClass()));
        assertTrue(e.getMessage().endsWith("not a Class or ParameterizedType"));
    }

    @Test
    public void testGetTypeInfoDefault() {
        TestClass<Integer> t = new TestClass<>() { };
        TypeInfo<?> info = TypeHelper.getTypeInfo(t.getClass());
        assertEquals(Integer.class, info.type());
        assertNull(info.generic());
    }

    @Test
    public void testGetTypeInfoWithGeneric() {
        TestClass<List<Integer>> t = new TestClass<>() { };
        TypeInfo<List<Integer>> info = TypeHelper.getTypeInfo(t.getClass());
        assertEquals(List.class, info.type());
        assertEquals(Integer.class, info.generic());
    }

    @Test
    public void testGetTypeInfoWithUnknownGeneric() {
        TestClass<List<?>> t = new TestClass<>() { };
        TypeInfo<List<?>> info = TypeHelper.getTypeInfo(t.getClass());
        assertEquals(List.class, info.type());
        assertNull(info.generic());
    }

    @Test
    public void testGetFirstTypeInfoOnly(){
        TestClass2<String, Long> t = new TestClass2<>() { };
        TypeInfo<String> info = TypeHelper.getTypeInfo(t.getClass());
        assertEquals(String.class, info.type());
        assertNull(info.generic());
    }

    @Test
    public void testGetFirstGenericInfoOnly() {
        TestClass<Map<Boolean, Integer>> t = new TestClass<>() { };
        TypeInfo<Map<Boolean, ?>> info = TypeHelper.getTypeInfo(t.getClass());
        assertEquals(Map.class, info.type());
        assertEquals(Boolean.class, info.generic());
    }

    @SuppressWarnings("unused")
    private static abstract class TestClass<T> { }

    @SuppressWarnings("unused")
    private static abstract class TestClass2<T, U> { }

}
