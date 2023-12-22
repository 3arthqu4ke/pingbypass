package me.earth.pingbypass.api.registry;

import me.earth.pingbypass.api.registry.impl.ConflictSolvers;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;
import me.earth.pingbypass.api.traits.Nameable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortedRegistryTest {
    @Test
    public void testSortedRegistry() {
        SortedRegistry<Nameable> nameables = new SortedRegistry<>(ConflictSolvers.replace());
        nameables.register(() -> "Orange");
        nameables.register(() -> "Banana");
        nameables.register(() -> "banana");
        nameables.register(() -> "apple");
        nameables.register(() -> "Apple");
        nameables.register(() -> "pineapple");

        List<String> strings = nameables.stream().map(Nameable::getName).toList();
        assertEquals(4, strings.size());
        assertEquals("Apple", strings.get(0));
        assertEquals("banana", strings.get(1));
        assertEquals("Orange", strings.get(2));
        assertEquals("pineapple", strings.get(3));


    }

}
