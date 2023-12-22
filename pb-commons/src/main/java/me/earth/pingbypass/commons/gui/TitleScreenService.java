package me.earth.pingbypass.commons.gui;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.commons.command.screen.CommandScreen;
import me.earth.pingbypass.commons.event.gui.GuiScreenEvent;
import me.earth.pingbypass.commons.mixins.gui.IScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Objects;

public class TitleScreenService extends SubscriberImpl {
    private final PingBypass pingBypass;

    private TitleScreenService(PingBypass pingBypass) {
        this.pingBypass = pingBypass;
        listen(new Listener<GuiScreenEvent.TitleScreen<TitleScreen>>() {
            @Override
            public void onEvent(GuiScreenEvent.TitleScreen<TitleScreen> event) {
                addButtonToTitleScreen(Objects.requireNonNull(event.getScreen()));
            }
        });
    }

    public void addButtonToTitleScreen(TitleScreen screen) {
        int x = 2; int y = 0; int w = 2;
        for (Renderable button : ((IScreen) screen).getRenderables()) {
            if (button instanceof AbstractWidget widget
                    && widget.getMessage().getContents() instanceof TranslatableContents translatable
                    && "narrator.button.accessibility".equals(translatable.getKey())) {
                x = widget.getX();
                y = widget.getY();
                w = widget.getHeight();
                break;
            }
        }

        ((IScreen) screen).invokeAddRenderableWidget(ImageButtonUtil.getIconButton(x, y - w - 4, button -> {
            if (pingBypass.getMinecraft().screen instanceof CommandScreen commandScreen) {
                commandScreen.close();
            } else {
                pingBypass.getMinecraft().setScreen(new CommandScreen(screen, pingBypass));
            }
        }));
    }

    public static TitleScreenService create(PingBypass pingBypass) {
        var service = new TitleScreenService(pingBypass);
        if (pingBypass.getMinecraft().screen instanceof TitleScreen titleScreen) {
            service.addButtonToTitleScreen(titleScreen);
        }

        return service;
    }

}
