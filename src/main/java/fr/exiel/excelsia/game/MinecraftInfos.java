package fr.exiel.excelsia.game;

import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.theshark34.openlauncherlib.minecraft.GameType;

public class MinecraftInfos {
    public static final String SERVER_NAME = "Excelsia";
    public static final String LAUNCHER_VERSION = "1.1.1";

    public static final String GAME_VERSION = "1.16.5";
    public static final VersionType VERSION_TYPE = VersionType.FORGE;
    public static final ForgeVersionBuilder.ForgeVersionType FORGE_VERSION_TYPE = ForgeVersionBuilder.ForgeVersionType.NEW;
    public static final String FORGE_VERSION = "1.16.5-36.2.23";

    public static final GameType OLL_GAME_TYPE = GameType.V1_13_HIGHER_FORGE;
    public static final NewForgeVersionDiscriminator OLL_FORGE_DISCRIMINATOR = new NewForgeVersionDiscriminator(
            "36.2.23",
            MinecraftInfos.GAME_VERSION,
            "20210115.111550"
    );

    public static final String CURSE_MODS_LIST_URL = "http://152.228.219.64/mods_list.json";
}
