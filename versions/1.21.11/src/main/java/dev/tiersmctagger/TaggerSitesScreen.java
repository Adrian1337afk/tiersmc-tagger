package dev.tiersmctagger;

import dev.tiersmctagger.TaggerConfig.HighestMode;
import dev.tiersmctagger.TaggerConfig.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

import static dev.tiersmctagger.TaggerConfigScreen.BUTTON_H;
import static dev.tiersmctagger.TaggerConfigScreen.BUTTON_W;
import static dev.tiersmctagger.TaggerConfigScreen.GAP;

/** "Tiers" page of the settings: where each site's tier goes and which gamemode it shows. */
public class TaggerSitesScreen extends Screen {
    private final Screen parent;

    public TaggerSitesScreen(Screen parent) {
        super(Text.literal("Tier lists"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        TaggerConfig config = TaggerConfig.get();
        int left = width / 2 - BUTTON_W - GAP / 2;
        int right = width / 2 + GAP / 2;
        int rows = TierSource.values().length + 1;
        int y = Math.max(40, (height - rows * (BUTTON_H + GAP) - 8 - BUTTON_H) / 2 + 6);

        // One row per site: position | gamemode
        for (TierSource source : TierSource.values()) {
            TaggerConfig.SiteSettings site = config.site(source);
            Text label = Text.literal(source.displayName).styled(s -> s.withColor(source.color));
            addDrawableChild(CyclingButtonWidget.<Position>builder(TaggerSitesScreen::positionText, site.position)
                    .values(Arrays.asList(Position.values()))
                    .build(left, y, BUTTON_W, BUTTON_H, label, (b, v) -> {
                        site.position = v;
                        TaggerConfig.save();
                    }));
            List<String> modes = GameModes.cycleFor(source);
            addDrawableChild(CyclingButtonWidget.<String>builder(mode -> ModeTexts.styledName(source, mode), site.gamemode)
                    .values(modes)
                    .build(right, y, BUTTON_W, BUTTON_H, Text.literal("Mode"), (b, v) -> {
                        site.gamemode = v;
                        TaggerConfig.save();
                    }));
            y += BUTTON_H + GAP;
        }

        addDrawableChild(CyclingButtonWidget.<HighestMode>builder(TaggerSitesScreen::highestText, config.highestMode)
                .values(Arrays.asList(HighestMode.values()))
                .build(left, y, BUTTON_W * 2 + GAP, BUTTON_H, Text.literal("Show highest tier"), (b, v) -> {
                    config.highestMode = v;
                    TaggerConfig.save();
                }));
        y += BUTTON_H + GAP;

        y += 8;
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(width / 2 - 100, Math.min(y, height - 28), 200, BUTTON_H).build());
    }

    private static Text highestText(HighestMode m) {
        return switch (m) {
            case NEVER -> Text.literal("Never");
            case NOT_FOUND -> Text.literal("If not ranked in mode");
            case ALWAYS -> Text.literal("Always");
        };
    }

    private static Text positionText(Position p) {
        return switch (p) {
            case LEFT -> Text.literal("Left");
            case RIGHT -> Text.literal("Right");
            case OFF -> Text.literal("Off").formatted(Formatting.RED);
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("Site position  ·  gamemode shown").formatted(Formatting.GRAY), width / 2, 27, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        TaggerConfig.save();
        MinecraftClient.getInstance().setScreen(parent);
    }
}
