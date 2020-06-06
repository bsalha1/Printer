package com.reliableplugins.printer.config;

import com.reliableplugins.printer.utils.BukkitUtil;

public class MessageConfig extends Config
{
    public MessageConfig()
    {
        super("messages.yml");
    }

    @Override
    public void load()
    {
        for(Message message : Message.values())
        {
            message.setMessage(getString(BukkitUtil.color(message.getConfigKey()),
                    message.getLoneMessage().replace("§", "&")));
        }

        save();
    }
}
