/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.economy;

import com.reliableplugins.printer.Printer;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;

public class EconomyManager implements Runnable
{
    private final LinkedBlockingQueue<EconomyRequest> economyRequests;

    public EconomyManager()
    {
        this.economyRequests = new LinkedBlockingQueue<>();
    }

    public void enqueueRequest(Player player, double amount)
    {
        this.economyRequests.add(new EconomyRequest(player, amount));
    }

    @Override
    public void run()
    {
        System.out.println("Printer EconomyManager started");

        while(Printer.INSTANCE.isEnabled())
        {
            if(this.economyRequests.peek() != null)
            {
                EconomyRequest request = this.economyRequests.poll();
                Printer.INSTANCE.getEconomyHook().withdraw(request.player, request.amount);
            }

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("Printer EconomyManager stopped");
    }

    public static class EconomyRequest
    {
        private final double amount;
        private final Player player;

        public EconomyRequest(Player player, double amount)
        {
            this.amount = amount;
            this.player = player;
        }
    }

}
