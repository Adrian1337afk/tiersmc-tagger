package dev.tiersmctagger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/** In-game settings, opened with /tiers, /tiersmc or the config keybind. Per-site options live in {@link TaggerSitesScreen}. */
public class TaggerConfigScreen extends Screen {
    static final int BUTTON_W = 150;
    static final int BUTTON_H = 20;
    static final int GAP = 4;

    private final @Nullable Screen parent;
    private TextFieldWidget search;

    public TaggerConfigScreen(@Nullable Screen parent) {
        super(Text.literal("TiersMC Tagger Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        TaggerConfig config = TaggerConfig.get();
        int left = width / 2 - BUTTON_W - GAP / 2;
        int right = width / 2 + GAP / 2;
        // Content is 8 rows plus a gap; centre it, but never above the title
        int y = Math.max(30, (height - 8 * (BUTTON_H + GAP) - 8) / 2 + 6);

        // Top row: Tiers (per-site options) | player search box
        addDrawableChild(ButtonWidget.builder(Text.literal("Tiers"),
                        b -> client.setScreen(new TaggerSitesScreen(this)))
                .dimensions(left, y, BUTTON_W, BUTTON_H).build());
        search = addDrawableChild(TaggerProfileScreen.searchField(textRenderer, right, y, BUTTON_W, search));
        y += BUTTON_H + GAP;
        addDrawableChild(ButtonWidget.builder(Text.literal("Leaderboards"),
                        b -> client.setScreen(new TaggerLeaderboardScreen(this)))
                .dimensions(left, y, BUTTON_W, BUTTON_H).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Online players"),
                        b -> client.setScreen(new TaggerLobbyScreen(this)))
                .dimensions(right, y, BUTTON_W, BUTTON_H).build());
        y += BUTTON_H + GAP;

        y = toggle(left, y, "Enabled", config.enabled, v -> config.enabled = v, false);
        y = toggle(right, y, "Gamemode icons", config.showIcons, v -> config.showIcons = v, true);
        y = toggle(left, y, "Nametags", config.showInNametags, v -> config.showInNametags = v, false);
        y = toggle(right, y, "Tab list", config.showInTabList, v -> config.showInTabList = v, true);
        y = toggle(left, y, "Site labels", config.showSiteLabel, v -> config.showSiteLabel = v, false);
        y = toggle(right, y, "Colored separator", config.coloredSeparator, v -> config.coloredSeparator = v, true);
        y = toggle(left, y, "Show retired", config.showRetired, v -> config.showRetired = v, false);
        y = toggle(right, y, "Own name in F5", config.showOwnNametag, v -> config.showOwnNametag = v, true);
        // Opponent HUD toggle shares its slot with a small "Move" button
        addDrawableChild(CyclingButtonWidget.onOffBuilder(config.opponentHud)
                .build(left, y, BUTTON_W - 44, BUTTON_H, Text.literal("Opponent HUD"), (b, v) -> {
                    config.opponentHud = v;
                    TaggerConfig.save();
                }));
        addDrawableChild(ButtonWidget.builder(Text.literal("Move"), b -> client.setScreen(new TaggerHudEditScreen(this)))
                .dimensions(left + BUTTON_W - 42, y, 42, BUTTON_H).build());
        y = toggle(right, y, "Clickable chat names", config.clickableNames, v -> config.clickableNames = v, true);
        y += 8;

        int bottom = Math.min(y, height - 28);
        addDrawableChild(ButtonWidget.builder(Text.literal("Refresh tiers"), b -> TierApi.clearCache())
                .dimensions(left, bottom, BUTTON_W, BUTTON_H).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(right, bottom, BUTTON_W, BUTTON_H).build());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (TaggerProfileScreen.searchEntered(search, input)) {
            TaggerConfig.save();
            client.setScreen(new TaggerProfileScreen(search.getText()));
            return true;
        }
        return super.keyPressed(input);
    }

    /** Adds an on/off button; returns the next row's y once the right column is filled. */
    private int toggle(int x, int y, String name, boolean value, Consumer<Boolean> setter, boolean endsRow) {
        addDrawableChild(CyclingButtonWidget.onOffBuilder(value)
                .build(x, y, BUTTON_W, BUTTON_H, Text.literal(name), (b, v) -> {
                    setter.accept(v);
                    TaggerConfig.save();
                }));
        return endsRow ? y + BUTTON_H + GAP : y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFFFF);
        // Small watermark in the bottom-right corner
        context.drawText(textRenderer, "qdran.de", width - textRenderer.getWidth("qdran.de") - 4, height - 12, 0x80AAAAAA, false);
    }

    @Override
    public void close() {
        TaggerConfig.save();
        MinecraftClient.getInstance().setScreen(parent);
    }
}
