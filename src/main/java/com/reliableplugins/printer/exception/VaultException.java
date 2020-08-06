/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.exception;

public class VaultException extends Exception
{
    public VaultException(String messsage)
    {
        super("Vault plugin: " + messsage);
    }
}
