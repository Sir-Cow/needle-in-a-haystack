package sircow.needleinahaystack;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeNeedleInAHaystack {
    public ForgeNeedleInAHaystack() {
        CommonClass.init();
        RegisterCommandsEvent.BUS.addListener(ForgeNeedleInAHaystack::onRegisterCommands);
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        NeedleHelper.registerCommands(event.getDispatcher());
    }
}
