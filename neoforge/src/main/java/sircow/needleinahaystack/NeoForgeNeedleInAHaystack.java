package sircow.needleinahaystack;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeNeedleInAHaystack {
    public NeoForgeNeedleInAHaystack(IEventBus eventBus) {
        CommonClass.init();
        NeoForge.EVENT_BUS.addListener(NeoForgeNeedleInAHaystack::onRegisterCommands);
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        NeedleHelper.registerCommands(event.getDispatcher());
    }
}
