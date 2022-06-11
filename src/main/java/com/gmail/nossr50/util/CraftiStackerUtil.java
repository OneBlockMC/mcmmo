package com.gmail.nossr50.util;

import com.craftimize.craftistacker.CraftiStacker;
import org.bukkit.plugin.Plugin;

public class CraftiStackerUtil {

    private static Plugin CRAFTISTACKER = null;

    public static boolean using() {
        return CRAFTISTACKER != null;
    }

    public static CraftiStacker get() {
        if (CRAFTISTACKER == null) CRAFTISTACKER = CraftiStacker.getPlugin(CraftiStacker.class);
        return (CraftiStacker) CRAFTISTACKER;
    }
}
