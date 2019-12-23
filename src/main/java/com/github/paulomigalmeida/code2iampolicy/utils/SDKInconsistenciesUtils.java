package com.github.paulomigalmeida.code2iampolicy.utils;

public class SDKInconsistenciesUtils {

    public static String fixIAMServiceName(String value){
        // Unfortunately, AWS SDK generation isn't that standardised :(
        var ret = value;
        switch (value){
            case "monitoring":
                ret = "cloudwatch";
                break;
        }
        return ret;
    }
}
