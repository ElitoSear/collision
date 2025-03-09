package elito.collision;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Collision implements ModInitializer {

    public static String MOD_ID = "collision";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("INITIALIZING");
    }
}
