package me.earth.pingbypass.api.traits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RenameableImpl implements Renameable {
    private String name;

}
