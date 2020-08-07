/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.utils;

public class StringUtil
{
    public static String getSpaces(int num)
    {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < num; i++)
        {
            result.append(" ");
        }
        return result.toString();
    }
}
