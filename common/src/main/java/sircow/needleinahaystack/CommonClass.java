package sircow.needleinahaystack;

import sircow.needleinahaystack.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("needleinahaystack")) {
            Constants.LOG.info("Initialising {}", Constants.MOD_NAME);
        }
    }
}
