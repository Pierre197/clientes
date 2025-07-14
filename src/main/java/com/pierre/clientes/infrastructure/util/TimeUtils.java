package com.pierre.clientes.infrastructure.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private TimeUtils(){
        //No instanciable
    }

    public static String currentTimestamp() {
        return OffsetDateTime.now(ZoneOffset.of("-05:00"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }
}
