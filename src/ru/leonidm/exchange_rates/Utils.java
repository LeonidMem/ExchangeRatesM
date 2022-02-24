package ru.leonidm.exchange_rates;

import ru.leonidm.exchange_rates.files.ConfigYML;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public static String getFormattedCurrentTime() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis() + ConfigYML.TIME_DELTA * 1000L));
    }
}
