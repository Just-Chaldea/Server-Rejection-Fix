
package gg.chaldea.serene.seasons.rejection;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(SereneSeasonsPatch.MOD_ID)
public class SereneSeasonsPatch {

    public static final String MOD_ID = "sereneseasonspatch";

    private static SereneSeasonsPatch instance;

    public SereneSeasonsPatch() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        instance = this;
    }

    public static SereneSeasonsPatch getInstance() {
        return instance;
    }
}
