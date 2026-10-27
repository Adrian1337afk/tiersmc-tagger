package dev.tiersmctagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.tiersmctagger.TaggerConfig;
import dev.tiersmctagger.ServerNames;
import dev.tiersmctagger.TierDisplay;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListHudMixin {
    @ModifyReturnValue(method = "getNameForDisplay", at = @At("RETURN"))
    private Component tiersmctagger$addTiers(Component original, PlayerInfo entry) {
        if (TaggerConfig.get().showInTabList) {
            // ServerNames maps a server's fake tab rows (e.g. mcpvp.club) to the real player
            return TierDisplay.decorate(ServerNames.tabPlayer(entry), original);
        }
        return original;
    }
}
