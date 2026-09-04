package sircow.needleinahaystack.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.needleinahaystack.Constants;
import sircow.needleinahaystack.NeedleHelper;

import java.util.List;

@Mixin(Block.class)
public class HayBlockMixin {
    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void needleinahaystack$onHayBreak(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return;

        GameType gameMode = serverPlayer.gameMode();
        if (gameMode == GameType.CREATIVE || gameMode == GameType.SPECTATOR) return;

        int pity = NeedleHelper.getPity(serverPlayer);
        boolean grant = false;

        if (pity + 1 >= 5000000) grant = true;
        else if (level.getRandom().nextInt(5000000) == 0) grant = true;

        if (grant) {
            NeedleHelper.setPity(serverPlayer, 0);
            needleinahaystack$giveNeedle(serverPlayer, level, pos);

            int totalBroken = pity + 1;
            int pb = NeedleHelper.getPersonalBest(serverPlayer);

            if (pb < 0 || totalBroken < pb) NeedleHelper.setPersonalBest(serverPlayer, totalBroken);

            String hoverKey = totalBroken == 1 ? "event.needleinahaystack.chat_message.hover.singular" : "event.needleinahaystack.chat_message.hover.plural";
            Component message = Component.translatable("event.needleinahaystack.chat_message")
                    .withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(
                            Component.translatable(hoverKey, totalBroken)
                    )));
            player.sendSystemMessage(message);

            var holder = serverPlayer.level().getServer().getAdvancements().get(Constants.id("needle_in_a_haystack"));

            if (holder != null) serverPlayer.getAdvancements().award(holder, "needle_found");
        }
        else NeedleHelper.setPity(serverPlayer, pity + 1);
    }

    @Unique
    private static void needleinahaystack$giveNeedle(ServerPlayer player, Level level, BlockPos pos) {
        ItemStack needle = new ItemStack(Items.STICK);

        needle.set(DataComponents.CUSTOM_NAME, Component.translatable("item.needleinahaystack.needle"));
        needle.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable("item.needleinahaystack.needle_lore"))));
        if (!player.getInventory().add(needle)) Block.popResource(level, pos, needle);
    }
}
