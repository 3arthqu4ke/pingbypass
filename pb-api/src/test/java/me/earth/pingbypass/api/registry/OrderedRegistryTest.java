package me.earth.pingbypass.api.registry;

import me.earth.pingbypass.api.registry.impl.OrderedRegistryImpl;
import me.earth.pingbypass.api.traits.Nameable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderedRegistryTest {
    @Test
    public void testOrderedRegistry() {
        OrderedRegistry<Nameable> nameables = new OrderedRegistryImpl<>();
        nameables.register(() -> "Fourth");
        nameables.registerBefore(() -> "Second", nameables.getByName("Fourth").orElseThrow());
        nameables.registerAfter(() -> "Third", nameables.getByName("second").orElseThrow());
        nameables.registerFirst(() -> "First");
        nameables.register(() -> "Fifth");

        List<String> strings = nameables.stream().map(Nameable::getName).toList();
        assertEquals(5, strings.size());
        assertEquals("First", strings.get(0));
        assertEquals("Second", strings.get(1));
        assertEquals("Third", strings.get(2));
        assertEquals("Fourth", strings.get(3));
        assertEquals("Fifth", strings.get(4));
    }

}
