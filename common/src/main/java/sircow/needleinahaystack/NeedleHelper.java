package sircow.needleinahaystack;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.CustomData;

public class NeedleHelper {
    private static final String PITY_KEY = Constants.MOD_ID + ":hayPity";
    private static final String PB_KEY = Constants.MOD_ID + ":needlePB";

    public static int getPity(ServerPlayer player) {
        CustomData data = player.get(DataComponents.CUSTOM_DATA);

        if (data == null) return 0;
        return data.copyTag().getIntOr(PITY_KEY, 0);
    }

    public static void setPity(ServerPlayer player, int value) {
        CustomData existing = player.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag = existing != null ? existing.copyTag() : new CompoundTag();

        tag.putInt(PITY_KEY, value);
        player.setComponent(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static int getPersonalBest(ServerPlayer player) {
        CustomData data = player.get(DataComponents.CUSTOM_DATA);

        if (data == null) return -1;
        return data.copyTag().getIntOr(PB_KEY, -1);
    }

    public static void setPersonalBest(ServerPlayer player, int value) {
        CustomData existing = player.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag = existing != null ? existing.copyTag() : new CompoundTag();

        tag.putInt(PB_KEY, value);
        player.setComponent(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void handleNeedleCommand(ServerPlayer player) {
        int pity = getPity(player);

        String key = pity == 1 ? "event.needleinahaystack.command.needle.singular" : "event.needleinahaystack.command.needle.plural";
        player.sendSystemMessage(Component.translatable(key, pity));
    }

    public static void handleNeedlePBCommand(ServerPlayer player) {
        int pb = getPersonalBest(player);

        if (pb < 0) player.sendSystemMessage(Component.translatable("event.needleinahaystack.command.needlepb.empty"));
        else {
            String key = pb == 1 ? "event.needleinahaystack.command.needlepb.singular" : "event.needleinahaystack.command.needlepb.plural";
            player.sendSystemMessage(Component.translatable(key, pb));
        }
    }

    public static void handleNeedlePBResetCommand(ServerPlayer player) {
        setPersonalBest(player, -1);
        player.sendSystemMessage(Component.translatable("event.needleinahaystack.command.needlepbreset"));
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("needle")
                .requires(CommandSourceStack::isPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) handleNeedleCommand(player);
                    return 1;
                })
        );
        dispatcher.register(Commands.literal("needlepb")
                .requires(CommandSourceStack::isPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) handleNeedlePBCommand(player);
                    return 1;
                })
        );
        dispatcher.register(Commands.literal("needlepbreset")
                .requires(CommandSourceStack::isPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) handleNeedlePBResetCommand(player);
                    return 1;
                })
        );
    }
}
