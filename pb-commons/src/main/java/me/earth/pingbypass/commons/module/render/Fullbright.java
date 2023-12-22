package me.earth.pingbypass.commons.module.render;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;

public class Fullbright extends ModuleImpl {
    public Fullbright(PingBypass pingBypass) {
        super(pingBypass, "Fullbright", Categories.RENDER, "Makes the game as bright as possible");
        listen(new Listener<LightTextureEvent>() {
            @Override
            public void onEvent(LightTextureEvent event) {
                event.setColor(0xFFFFFFFF);
            }
        });
    }

    @Data
    @AllArgsConstructor
    public static class LightTextureEvent {
        private int color;
    }

}
