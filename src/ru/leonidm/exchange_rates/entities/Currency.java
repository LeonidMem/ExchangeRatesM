package ru.leonidm.exchange_rates.entities;

import ru.leonidm.exchange_rates.files.ConfigYML;
import ru.leonidm.telegram_utils.logger.Logger;
import ru.leonidm.telegram_utils.utils.RRequest;

public record Currency(String name, RRequest request, String after, String before) {

    public double getRate() {
        try {
            RRequest request = this.request.clone().send(RRequest.Method.GET, true);
            if(request.getResponseCode() != 200) {
                Thread.sleep(ConfigYML.TIMEOUT_DELAY * 1000L);
                Logger.warn("Site temporary banned this IP!");
                return -1;
            }

            String response = request.getResponseString(true).replace(",", ".");

            String rawValue = response.split(after, 2)[1].split(before, 2)[0];
            int index = rawValue.indexOf('.');
            if(rawValue.length() > index + 3) {
                rawValue = rawValue.substring(0, index + 3);
            }

            return Double.parseDouble(rawValue);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
