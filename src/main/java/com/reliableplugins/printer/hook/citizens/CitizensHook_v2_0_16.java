package com.reliableplugins.printer.hook.citizens;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;

public class CitizensHook_v2_0_16 implements CitizensHook
{
    public boolean isCitizen(Entity entity)
    {
        return CitizensAPI.getNPCRegistry().isNPC(entity);
    }
}
