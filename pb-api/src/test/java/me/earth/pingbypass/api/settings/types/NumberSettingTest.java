package me.earth.pingbypass.api.settings.types;

import me.earth.pingbypass.api.setting.impl.types.DoubleBuilder;
import me.earth.pingbypass.api.setting.impl.types.NumberSetting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberSettingTest {
    @Test
    public void testMinMax() {
        NumberSetting<Double> setting = new DoubleBuilder()
                .withName("Test").withDescription("Test").withValue(0.0).withMin(0.0).withMax(10.0).build();
        
        assertEquals(0.0, setting.getMin());
        assertEquals(10.0, setting.getMax());
        assertEquals(0.0, setting.getValue());
        setting.setValue(5.0);
        assertEquals(5.0, setting.getValue());
        setting.setValue(11.0);
        assertEquals(5.0, setting.getValue());
        setting.setValue(-1.0);
        assertEquals(5.0, setting.getValue());
        setting.setValue(setting.getMax());
        assertEquals(setting.getMax(), setting.getValue());
        setting.setValue(setting.getMin());
        assertEquals(setting.getMin(), setting.getValue());
    }

}
