package com.reliableplugins.printer.exception;

public class VaultException extends Exception
{
    public VaultException(String messsage)
    {
        super("Vault plugin: " + messsage);
    }
}
