package me.earth.pingbypass.api.setting.impl;

import lombok.Data;
import me.earth.pingbypass.api.setting.Complexity;

@Data
public class Complexities implements Complexity {
    public static final Complexity BEGINNER =
            new Complexities("Beginner", 0, "Anyone can understand these settings.");
    public static final Complexity MEDIUM =
            new Complexities("Medium", 1, "These settings require some experience.");
    public static final Complexity EXPERT =
            new Complexities("Expert", 2, "These settings might require knowledge of the code base!");
    public static final Complexity DEV =
            new Complexities("Dev", 3, "If you are not a developer you probably should not touch these.");

    private final String name;
    private final int complexityValue;
    private final String description;

}
