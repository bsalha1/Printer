/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.task;

import com.reliableplugins.printer.Printer;

import java.util.concurrent.LinkedBlockingQueue;

public class AsyncTaskManager implements Runnable
{
    private final LinkedBlockingQueue<Runnable> tasks;

    public AsyncTaskManager()
    {
        this.tasks = new LinkedBlockingQueue<>();
    }

    public void enqueueTask(Runnable task)
    {
        this.tasks.add(task);
    }

    @Override
    public void run()
    {
        System.out.println("Printer AsyncTaskManager started");

        while(Printer.INSTANCE.isEnabled())
        {
            if(this.tasks.peek() != null)
            {
                Runnable task = this.tasks.poll();
                task.run();
            }

            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("Printer AsyncTaskManager stopped");
    }
}
