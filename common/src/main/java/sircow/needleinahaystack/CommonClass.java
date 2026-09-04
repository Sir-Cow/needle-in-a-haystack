package sircow.needleinahaystack;

import sircow.needleinahaystack.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("Initialising {}", Constants.MOD_NAME);
        }
    }
}
