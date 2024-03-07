package me.earth.pingbypass.api.command.screen;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.ParseResultUtil;
import me.earth.pingbypass.api.event.chat.CommandSuggestionEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static net.minecraft.ChatFormatting.*;

/**
 * A custom {@link net.minecraft.client.gui.components.CommandSuggestions}.
 */
final class CustomCommandSuggestions {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
    private static final Style UNPARSED_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);
    private static final Style LITERAL_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY);
    private static final List<Style> ARGUMENT_STYLES = Stream.of(AQUA, YELLOW, GREEN, LIGHT_PURPLE, GOLD)
                                                             .map(Style.EMPTY::withColor)
                                                             .collect(ImmutableList.toImmutableList());
    private final Minecraft mc;
    private final Screen screen;
    private final EditBox input;
    private final Font font;
    private final int lineStartOffset;
    private final int suggestionLineLimit;
    private final boolean anchorToBottom;
    private final int fillColor;
    private final List<FormattedCharSequence> commandUsage = Lists.newArrayList();
    private int commandUsagePosition;
    private int commandUsageWidth;
    @Nullable
    private ParseResults<CommandSource> currentParse;
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Nullable
    private CustomCommandSuggestions.SuggestionsList suggestions;
    private final PingBypass pingBypass;
    private final CommandSource suggestionProvider;
    private boolean allowSuggestions;
    boolean keepSuggestions;

    public CustomCommandSuggestions(Minecraft mc, Screen screen, EditBox input, Font font, int lineStartOffset,
                                    int suggestionLineLimit, boolean anchorToBottom, int fillColor,
                                    PingBypass pingBypass, CommandSource suggestionProvider) {
        this.mc = mc;
        this.screen = screen;
        this.input = input;
        this.font = font;
        this.lineStartOffset = lineStartOffset;
        this.suggestionLineLimit = suggestionLineLimit;
        this.anchorToBottom = anchorToBottom;
        this.fillColor = fillColor;
        this.pingBypass = pingBypass;
        this.suggestionProvider = suggestionProvider;
        input.setFormatter(this::formatChat);
    }

    public void setAllowSuggestions(boolean allowSuggestions) {
        this.allowSuggestions = allowSuggestions;
        if (!allowSuggestions) {
            this.suggestions = null;
        }
    }

    public boolean keyPressed(int button, int x, int y) {
        if (this.suggestions != null && this.suggestions.keyPressed(button, x, y)) {
            return true;
        } else if (this.screen.getFocused() == this.input && button == 258) {
            this.showSuggestions(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double by) {
        return this.suggestions != null && this.suggestions.mouseScrolled(Mth.clamp(by, -1.0, 1.0));
    }

    public boolean mouseClicked(double x, double y, int button) {
        return this.suggestions != null && this.suggestions.mouseClicked((int)x, (int)y, button);
    }

    public void showSuggestions(boolean bl) {
        if (this.pendingSuggestions != null && this.pendingSuggestions.isDone()) {
            Suggestions suggestions = this.pendingSuggestions.join();
            if (!suggestions.isEmpty()) {
                int i = 0;
                for(Suggestion suggestion : suggestions.getList()) {
                    i = Math.max(i, this.font.width(suggestion.getText()));
                }

                int j = Mth.clamp(this.input.getScreenX(suggestions.getRange().getStart()), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - i);
                int k = this.anchorToBottom ? this.screen.height - 12 : 72;
                this.suggestions = new SuggestionsList(j, k, i, this.sortSuggestions(suggestions), bl);
            }
        }

    }

    public void hide() {
        this.suggestions = null;
    }

    private List<Suggestion> sortSuggestions(Suggestions suggestions) {
        String string = this.input.getValue().substring(0, this.input.getCursorPosition());
        int i = getLastWordIndex(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        List<Suggestion> list = Lists.newArrayList();
        List<Suggestion> list2 = Lists.newArrayList();

        for(Suggestion suggestion : suggestions.getList()) {
            if (!suggestion.getText().startsWith(string2) && !suggestion.getText().startsWith("minecraft:" + string2)) {
                list2.add(suggestion);
            } else {
                list.add(suggestion);
            }
        }

        list.addAll(list2);
        return list;
    }

    public void updateCommandInfo() {
        String string = this.input.getValue();
        if (this.currentParse != null && !this.currentParse.getReader().getString().equals(string)) {
            this.currentParse = null;
        }

        if (!this.keepSuggestions) {
            this.input.setSuggestion(null);
            this.suggestions = null;
        }

        this.commandUsage.clear();
        StringReader reader = new StringReader(string);
        var event = new CommandSuggestionEvent(
                reader, keepSuggestions, input, suggestions, pendingSuggestions,
                currentParse == null ? null : ParseResultUtil.dummy(), currentParse, false);
        PingBypassApi.getEventBus().post(event);
        if (event.isCancelled()) {
            currentParse = event.getCustomParse();
            pendingSuggestions = event.getPendingSuggestions();
            if (pendingSuggestions != null && event.isUpdatingPendingSuggestions()) {
                pendingSuggestions.thenRun(() -> {
                    if (pendingSuggestions != null && pendingSuggestions.isDone()) {
                        updateUsageInfo();
                    }
                });
            }

            return;
        }

        String sub = string.substring(0, this.input.getCursorPosition());
        int lastWord = getLastWordIndex(sub);
        Collection<String> tabs = suggestionProvider.getCustomTabSugggestions();
        this.pendingSuggestions = SharedSuggestionProvider.suggest(tabs, new SuggestionsBuilder(sub, lastWord));
    }

    private static int getLastWordIndex(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return 0;
        } else {
            int i = 0;
            Matcher matcher = WHITESPACE_PATTERN.matcher(string);

            while(matcher.find()) {
                i = matcher.end();
            }

            return i;
        }
    }

    private static FormattedCharSequence getExceptionMessage(CommandSyntaxException commandSyntaxException) {
        Component lv = ComponentUtils.fromMessage(commandSyntaxException.getRawMessage());
        String string = commandSyntaxException.getContext();
        return string == null
                ? lv.getVisualOrderText()
                : Component.translatable("command.context.parse_error", lv, commandSyntaxException.getCursor(), string).getVisualOrderText();
    }

    private void updateUsageInfo() {
        if (pendingSuggestions == null || currentParse == null) {
            return;
        }

        if (this.input.getCursorPosition() == this.input.getValue().length()) {
            if (this.pendingSuggestions.join().isEmpty() && !this.currentParse.getExceptions().isEmpty()) {
                int i = 0;

                for(Entry<CommandNode<CommandSource>, CommandSyntaxException> entry : this.currentParse.getExceptions().entrySet()) {
                    CommandSyntaxException commandSyntaxException = entry.getValue();
                    if (commandSyntaxException.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
                        ++i;
                    } else {
                        this.commandUsage.add(getExceptionMessage(commandSyntaxException));
                    }
                }

                if (i > 0) {
                    this.commandUsage.add(getExceptionMessage(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create()));
                }
            } else if (this.currentParse.getReader().canRead()) {
                this.commandUsage.add(getExceptionMessage(Objects.requireNonNull(Commands.getParseException(this.currentParse))));
            }
        }

        this.commandUsagePosition = 0;
        this.commandUsageWidth = this.screen.width;
        if (this.commandUsage.isEmpty()) {
            this.fillNodeUsage();
        }

        this.suggestions = null;
        if (this.allowSuggestions && this.mc.options.autoSuggestions().get()) {
            this.showSuggestions(false);
        }

    }

    private void fillNodeUsage() {
        assert this.currentParse != null;
        CommandContextBuilder<CommandSource> commandContextBuilder = this.currentParse.getContext();
        SuggestionContext<CommandSource> suggestionContext = commandContextBuilder.findSuggestionContext(this.input.getCursorPosition());
        Map<CommandNode<CommandSource>, String> map = pingBypass.getCommandManager().getSmartUsage(suggestionContext.parent, suggestionProvider);

        List<FormattedCharSequence> list = Lists.newArrayList();
        int i = 0;
        Style lv = Style.EMPTY.withColor(ChatFormatting.GRAY);
        for(Entry<CommandNode<CommandSource>, String> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof LiteralCommandNode)) {
                list.add(FormattedCharSequence.forward(entry.getValue(), lv));
                i = Math.max(i, this.font.width(entry.getValue()));
            }
        }

        if (!list.isEmpty()) {
            this.commandUsage.addAll(list);
            this.commandUsagePosition = Mth.clamp(this.input.getScreenX(suggestionContext.startPos), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - i);
            this.commandUsageWidth = i;
        }
    }

    private FormattedCharSequence formatChat(String string, int i) {
        return this.currentParse != null ? formatText(this.currentParse, string, i) : FormattedCharSequence.forward(string, Style.EMPTY);
    }

    @Nullable
    static String calculateSuggestionSuffix(String string, String string2) {
        return string2.startsWith(string) ? string2.substring(string.length()) : null;
    }

    private static FormattedCharSequence formatText(ParseResults<CommandSource> parseResults, String string, int i) {
        List<FormattedCharSequence> list = Lists.newArrayList();
        int j = 0;
        int k = -1;
        CommandContextBuilder<CommandSource> commandContextBuilder = parseResults.getContext().getLastChild();

        for(ParsedArgument<CommandSource, ?> parsedArgument : commandContextBuilder.getArguments().values()) {
            if (++k >= ARGUMENT_STYLES.size()) {
                k = 0;
            }

            int l = Math.max(parsedArgument.getRange().getStart() - i, 0);
            if (l >= string.length()) {
                break;
            }

            int m = Math.min(parsedArgument.getRange().getEnd() - i, string.length());
            if (m > 0) {
                list.add(FormattedCharSequence.forward(string.substring(j, l), LITERAL_STYLE));
                list.add(FormattedCharSequence.forward(string.substring(l, m), ARGUMENT_STYLES.get(k)));
                j = m;
            }
        }

        if (parseResults.getReader().canRead()) {
            int n = Math.max(parseResults.getReader().getCursor() - i, 0);
            if (n < string.length()) {
                int o = Math.min(n + parseResults.getReader().getRemainingLength(), string.length());
                list.add(FormattedCharSequence.forward(string.substring(j, n), LITERAL_STYLE));
                list.add(FormattedCharSequence.forward(string.substring(n, o), UNPARSED_STYLE));
                j = o;
            }
        }

        list.add(FormattedCharSequence.forward(string.substring(j), LITERAL_STYLE));
        return FormattedCharSequence.composite(list);
    }

    public void render(GuiGraphics arg, int i, int j) {
        if (!this.renderSuggestions(arg, i, j)) {
            this.renderUsage(arg);
        }
    }

    public boolean renderSuggestions(GuiGraphics arg, int i, int j) {
        if (this.suggestions != null) {
            this.suggestions.render(arg, i, j);
            return true;
        } else {
            return false;
        }
    }

    public void renderUsage(GuiGraphics arg) {
        int i = 0;

        for(Iterator<FormattedCharSequence> itr = this.commandUsage.iterator(); itr.hasNext(); ++i) {
            FormattedCharSequence lv = itr.next();
            int j = this.anchorToBottom ? this.screen.height - 14 - 13 - 12 * i : 72 + 12 * i;
            arg.fill(this.commandUsagePosition - 1, j, this.commandUsagePosition + this.commandUsageWidth + 1, j + 12, this.fillColor);
            arg.drawString(this.font, lv, this.commandUsagePosition, j + 2, -1);
        }

    }

    public Component getNarrationMessage() {
        return this.suggestions != null ? CommonComponents.NEW_LINE.copy().append(this.suggestions.getNarrationMessage()) : CommonComponents.EMPTY;
    }

    public class SuggestionsList {
        private final Rect2i rect;
        private final String originalContents;
        private final List<Suggestion> suggestionList;
        private int offset;
        private int current;
        private Vec2 lastMouse;
        private boolean tabCycles;
        private int lastNarratedEntry;

        SuggestionsList(int i, int j, int k, List<Suggestion> list, boolean bl) {
            this.lastMouse = Vec2.ZERO;
            int l = i - 1;
            int m = anchorToBottom ? j - 3 - Math.min(list.size(), suggestionLineLimit) * 12 : j;
            this.rect = new Rect2i(l, m, k + 1, Math.min(list.size(), suggestionLineLimit) * 12);
            this.originalContents = input.getValue();
            this.lastNarratedEntry = bl ? -1 : 0;
            this.suggestionList = list;
            this.select(0);
        }

        public void render(GuiGraphics arg, int i, int j) {
            int k = Math.min(this.suggestionList.size(), suggestionLineLimit);
            boolean bl = this.offset > 0;
            boolean bl2 = this.suggestionList.size() > this.offset + k;
            boolean bl3 = bl || bl2;
            boolean bl4 = this.lastMouse.x != (float)i || this.lastMouse.y != (float)j;
            if (bl4) {
                this.lastMouse = new Vec2((float)i, (float)j);
            }

            if (bl3) {
                arg.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), fillColor);
                arg.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, fillColor);
                int m;
                if (bl) {
                    for(m = 0; m < this.rect.getWidth(); ++m) {
                        if (m % 2 == 0) {
                            arg.fill(this.rect.getX() + m, this.rect.getY() - 1, this.rect.getX() + m + 1, this.rect.getY(), -1);
                        }
                    }
                }

                if (bl2) {
                    for(m = 0; m < this.rect.getWidth(); ++m) {
                        if (m % 2 == 0) {
                            arg.fill(this.rect.getX() + m, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + m + 1, this.rect.getY() + this.rect.getHeight() + 1, -1);
                        }
                    }
                }
            }

            boolean bl5 = false;

            for(int n = 0; n < k; ++n) {
                Suggestion suggestion = this.suggestionList.get(n + this.offset);
                arg.fill(this.rect.getX(), this.rect.getY() + 12 * n, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * n + 12, fillColor);
                if (i > this.rect.getX() && i < this.rect.getX() + this.rect.getWidth() && j > this.rect.getY() + 12 * n && j < this.rect.getY() + 12 * n + 12) {
                    if (bl4) {
                        this.select(n + this.offset);
                    }

                    bl5 = true;
                }

                arg.drawString(font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * n, n + this.offset == this.current ? -256 : -5592406);
            }

            if (bl5) {
                Message message = this.suggestionList.get(this.current).getTooltip();
                if (message != null) {
                    arg.renderTooltip(font, ComponentUtils.fromMessage(message), i, j);
                }
            }
        }

        public boolean mouseClicked(int i, int j, @SuppressWarnings("unused") int button) {
            if (!this.rect.contains(i, j)) {
                return false;
            } else {
                int l = (j - this.rect.getY()) / 12 + this.offset;
                if (l >= 0 && l < this.suggestionList.size()) {
                    this.select(l);
                    this.useSuggestion();
                }

                return true;
            }
        }

        public boolean mouseScrolled(double d) {
            int i = (int)(
                    mc.mouseHandler.xpos()
                    * (double) mc.getWindow().getGuiScaledWidth()
                    / (double) mc.getWindow().getScreenWidth()
            );
            int j = (int)(
                    mc.mouseHandler.ypos()
                    * (double) mc.getWindow().getGuiScaledHeight()
                    / (double) mc.getWindow().getScreenHeight()
            );
            if (this.rect.contains(i, j)) {
                this.offset = Mth.clamp((int)((double)this.offset - d), 0, Math.max(this.suggestionList.size() - suggestionLineLimit, 0));
                return true;
            } else {
                return false;
            }
        }

        @SuppressWarnings("unused")
        public boolean keyPressed(int i, int j, int k) {
            if (i == 265) {
                this.cycle(-1);
                this.tabCycles = false;
                return true;
            } else if (i == 264) {
                this.cycle(1);
                this.tabCycles = false;
                return true;
            } else if (i == 258) {
                if (this.tabCycles) {
                    this.cycle(Screen.hasShiftDown() ? -1 : 1);
                }

                this.useSuggestion();
                return true;
            } else if (i == 256) {
                hide();
                return true;
            } else {
                return false;
            }
        }

        public void cycle(int i) {
            this.select(this.current + i);
            int j = this.offset;
            int k = this.offset + suggestionLineLimit - 1;
            if (this.current < j) {
                this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - suggestionLineLimit, 0));
            } else if (this.current > k) {
                this.offset = Mth.clamp(
                        this.current + lineStartOffset - suggestionLineLimit,
                        0,
                        Math.max(this.suggestionList.size() - suggestionLineLimit, 0)
                );
            }

        }

        public void select(int i) {
            this.current = i;
            if (this.current < 0) {
                this.current += this.suggestionList.size();
            }

            if (this.current >= this.suggestionList.size()) {
                this.current -= this.suggestionList.size();
            }

            Suggestion suggestion = this.suggestionList.get(this.current);
            input.setSuggestion(calculateSuggestionSuffix(input.getValue(), suggestion.apply(this.originalContents)));
            if (this.lastNarratedEntry != this.current) {
                mc.getNarrator().sayNow(this.getNarrationMessage());
            }

        }

        public void useSuggestion() {
            Suggestion suggestion = this.suggestionList.get(this.current);
            keepSuggestions = true;
            input.setValue(suggestion.apply(this.originalContents));
            int i = suggestion.getRange().getStart() + suggestion.getText().length();
            input.setCursorPosition(i);
            input.setHighlightPos(i);
            this.select(this.current);
            keepSuggestions = false;
            this.tabCycles = true;
        }

        Component getNarrationMessage() {
            this.lastNarratedEntry = this.current;
            Suggestion suggestion = this.suggestionList.get(this.current);
            Message message = suggestion.getTooltip();
            return message != null
                    ? Component.translatable("narration.suggestion.tooltip", this.current + 1, this.suggestionList.size(), suggestion.getText(), message)
                    : Component.translatable("narration.suggestion", this.current + 1, this.suggestionList.size(), suggestion.getText());
        }
    }
}
