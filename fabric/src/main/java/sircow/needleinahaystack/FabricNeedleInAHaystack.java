package sircow.needleinahaystack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FabricNeedleInAHaystack implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, selection) -> NeedleHelper.registerCommands(dispatcher));
    }
}
