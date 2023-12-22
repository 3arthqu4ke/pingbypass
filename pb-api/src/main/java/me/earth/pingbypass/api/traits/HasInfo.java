package me.earth.pingbypass.api.traits;

import lombok.Data;

@Data
public class HasInfo implements Nameable, HasDescription {
    private final String name;
    private final String description;

}
