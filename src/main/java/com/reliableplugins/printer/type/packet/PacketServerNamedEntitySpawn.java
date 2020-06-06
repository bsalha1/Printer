package com.reliableplugins.printer.type.packet;

import java.util.UUID;

public class PacketServerNamedEntitySpawn
{
    private UUID uuid;

    public PacketServerNamedEntitySpawn(UUID uuid)
    {
        this.uuid = uuid;
    }

    public UUID getUuid()
    {
        return uuid;
    }
}
