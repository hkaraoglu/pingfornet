/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hkaraoglu
 */
public class ValidatorUtils
{
   
    
    public static Boolean isIpAddress(String text)
    {
        String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        return ValidatorUtils.checkPattern(IPADDRESS_PATTERN, text);
    }

    public static Boolean isHostname(String text)
    {
       // String HOSTNAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\\\.)+[A-Za-z]{2,6}$";
      //  return ValidatorUtils.checkPattern(HOSTNAME_PATTERN, text);
        return true;
    }
    
    public static boolean isPort(String text)
    {
        if(isNumber(text))
        {
            int port = Integer.parseInt(text);
            return (port <= 65536);
        }
        return false;
    }
    
    public static boolean isNumber(String text)
    {
        String numberPattern = "^(0|[1-9][0-9]*)$";
        return checkPattern(numberPattern, text);
    }
    
    private static Boolean checkPattern(String regex, String text)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

   
}
