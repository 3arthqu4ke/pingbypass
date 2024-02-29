package me.earth.pingbypass.api.command.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.DelegatingCommandSource;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static me.earth.pingbypass.api.input.Keys.*;

/**
 * A custom {@link net.minecraft.client.gui.screens.ChatScreen} which also displays a {@link ChatComponent}.
 */
// TODO: tooltips don't seem to get displayed?
public class CommandScreen extends Screen {
    private static final Component USAGE_TEXT = Component.translatable("chat_screen.usage");
    private static final double MOUSE_SCROLL_SPEED = 7.0;
    private static final int TOOLTIP_MAX_WIDTH = 210;

    private final CommandSource suggestionProvider;
    private final ChatComponent chatComponent;
    private final PingBypass pingBypass;
    private final Minecraft minecraft;
    private final Screen background;
    private CustomCommandSuggestions commandSuggestions;
    private String historyBuffer = "";
    private int historyPos = -1;
    private int tickCount;
    private String initial = "";
    private EditBox input;

    public CommandScreen(Screen background, PingBypass pingBypass) {
        super(Component.literal("Commands"));
        this.background = background;
        this.pingBypass = pingBypass;
        this.minecraft = pingBypass.getMinecraft();
        this.suggestionProvider = new DelegatingCommandSource(minecraft, pingBypass);
        this.chatComponent = minecraft.gui.getChat();
    }

    @Override
    protected void init() {
        assert minecraft != null;
        historyPos = chatComponent.getRecentChat().size();
        input = new EditBox(minecraft.fontFilterFishy, 2, height - 12, width - 4, 12, Component.literal("CommandsChat")) {
            @Override
            protected @NotNull MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(commandSuggestions.getNarrationMessage());
            }

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int x, int y, float delta) {
                if (this.isVisible()) {
                    // make background black
                    graphics.fill(this.getX() - 1, this.getY() - 2, this.getX() + this.width, this.getY() + this.height - 2, 0xff000000);
                    super.renderWidget(graphics, x, y, delta);
                }
            }
        };

        input.setMaxLength(256);
        input.setBordered(false);
        input.setValue(initial);
        input.setResponder(this::onEdited);
        addWidget(input);
        commandSuggestions = new CustomCommandSuggestions(minecraft, this, input, font, 1, 10, true, 0xD0000000, pingBypass, suggestionProvider);
        commandSuggestions.updateCommandInfo();
        setInitialFocus(input);
        chatComponent.addMessage(Component.empty());
    }

    @Override
    public void resize(@NotNull Minecraft arg, int i, int j) {
        background.resize(arg, i, j);
        String string = input.getValue();
        init(arg, i, j);
        setChatLine(string);
        commandSuggestions.updateCommandInfo();
    }

    @Override
    public void removed() {
        assert minecraft != null;
        chatComponent.resetChatScroll();
    }

    @Override
    public void tick() {
        tickCount++;
        //input.tick();
    }

    @Override
    public boolean keyPressed(int button, int x, int y) {
        Objects.requireNonNull(minecraft, "Minecraft was null!");
        if (commandSuggestions.keyPressed(button, x, y)) {
            return true;
        } else if (super.keyPressed(button, x, y)) {
            return true;
        } else if (button == KEY_ESCAPE) {
            close();
            return true;
        }  else if (button == KEY_ENTER || button == KEY_KP_ENTER) {
            handleChatInput(input.getValue());
            input.setValue("");
            return true;
        } else if (button == KEY_UP) {
            moveInHistory(-1);
            return true;
        } else if (button == KEY_DOWN) {
            moveInHistory(1);
            return true;
        } else if (button == KEY_PAGE_UP) {
            chatComponent.scrollChat(chatComponent.getLinesPerPage() - 1);
            return true;
        } else if (button == KEY_PAGE_DOWN) {
            chatComponent.scrollChat(-chatComponent.getLinesPerPage() + 1);
            return true;
        } else {
            // return background.keyPressed(button, x, y);
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        double by = Mth.clamp(scrollY, -1.0, 1.0);
        if (!commandSuggestions.mouseScrolled(by)) {
            if (!hasShiftDown()) {
                by *= MOUSE_SCROLL_SPEED;
            }

            chatComponent.scrollChat((int) by);
        }

        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (commandSuggestions.mouseClicked((int) x, (int )y, button)) {
            return true;
        } else {
            if (button == 0) {
                if (chatComponent.handleChatQueueClicked(x, y)) {
                    return true;
                }

                Style componentStyleAt = getComponentStyleAt(x, y);
                if (componentStyleAt != null && handleComponentClicked(componentStyleAt)) {
                    initial = input.getValue();
                    return true;
                }
            }

            return input.mouseClicked(x, y, button) || super.mouseClicked(x, y, button);
        }
    }

    @Override
    protected void insertText(@NotNull String text, boolean set) {
        if (set) {
            input.setValue(text);
        } else {
            input.insertText(text);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int x, int y, float delta) {
        // background.render(graphics, x, y, delta); would've been nice to just display this on the TitleScreen but bugs
        super.render(graphics, x, y, delta);
        chatComponent.render(graphics, tickCount, x, y);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        setFocused(input);
        // TODO: this is extremely dark!
        graphics.fill(2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.options.getBackgroundColor(Integer.MIN_VALUE));
        input.render(graphics, x, y, delta);
        commandSuggestions.render(graphics, x, y);
        GuiMessageTag messageTagAt = chatComponent.getMessageTagAt(x, y);
        if (messageTagAt != null && messageTagAt.text() != null) {
            graphics.renderTooltip(font, font.split(messageTagAt.text(), TOOLTIP_MAX_WIDTH), x, y);
        } else {
            Style componentStyleAt = getComponentStyleAt(x, y);
            if (componentStyleAt != null && componentStyleAt.getHoverEvent() != null) {
                graphics.renderComponentHoverEffect(font, componentStyleAt, x, y);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void updateNarrationState(NarrationElementOutput out) {
        out.add(NarratedElementType.TITLE, getTitle());
        out.add(NarratedElementType.USAGE, USAGE_TEXT);
        String string = input.getValue();
        if (!string.isEmpty()) {
            out.nest().add(NarratedElementType.TITLE, Component.translatable("chat_screen.message", string));
        }
    }

    public void close() {
        minecraft.setScreen(background);
    }

    @Nullable
    private Style getComponentStyleAt(double d, double e) {
        return chatComponent.getClickedComponentStyleAt(d, e);
    }

    private void moveInHistory(int by) {
        int index = historyPos + by;
        int chatSize = chatComponent.getRecentChat().size();
        index = Mth.clamp(index, 0, chatSize);
        if (index != historyPos) {
            if (index == chatSize) {
                historyPos = chatSize;
                input.setValue(historyBuffer);
            } else {
                if (historyPos == chatSize) {
                    historyBuffer = input.getValue();
                }

                input.setValue(chatComponent.getRecentChat().get(index));
                commandSuggestions.setAllowSuggestions(false);
                historyPos = index;
            }
        }
    }

    private void onEdited(String string) {
        commandSuggestions.setAllowSuggestions(!input.getValue().equals(""));
        commandSuggestions.updateCommandInfo();
    }

    private void setChatLine(String string) {
        input.setValue(string);
    }

    private void handleChatInput(String message) {
        String normalized = normalizeChatMessage(message);
        if (!normalized.isEmpty()) {
            chatComponent.addMessage(Component.literal(normalized));
            CommandManager commandManager = pingBypass.getCommandManager();
            String prefix = commandManager.getPrefix();
            if (message.startsWith(prefix)) {
                try {
                    commandManager.execute(message.substring(prefix.length()), suggestionProvider);
                } catch (CommandSyntaxException e) {
                    Minecraft.getInstance().gui.getChat().addMessage(ComponentUtils.fromMessage(e.getRawMessage()));
                }
            }
        }
    }

    private String normalizeChatMessage(String message) {
        return StringUtil.trimChatMessage(StringUtils.normalizeSpace(message.trim()));
    }

}

