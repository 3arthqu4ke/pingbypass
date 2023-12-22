package me.earth.pingbypass.api.traits;

/**
 * An Interface for all Objects that can have a name. For most use-cases in this
 * client the name should never change.
 */
public interface Nameable {
    /**
     * @return the name for this Object.
     */
    String getName();

    default String getNameLowerCase() {
        return this.getName().toLowerCase();
    }

    default int compareAlphabetically(Nameable other) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getName(), other.getName());
    }

    static <T extends Nameable> T getByName(Iterable<T> nameables, String name) {
        for (T t : nameables) {
            if (name.equals(t.getName())) {
                return t;
            }
        }

        return null;
    }

    static <T extends Nameable> T getByNameIgnoreCase(Iterable<T> nameables, String name) {
        for (T t : nameables) {
            if (name.equalsIgnoreCase(t.getName())) {
                return t;
            }
        }

        return null;
    }

}
