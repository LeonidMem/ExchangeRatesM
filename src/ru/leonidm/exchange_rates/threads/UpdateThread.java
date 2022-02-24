package ru.leonidm.exchange_rates.threads;

import org.glassfish.grizzly.utils.Pair;
import ru.leonidm.exchange_rates.ExchangeRatesM;
import ru.leonidm.exchange_rates.Utils;
import ru.leonidm.exchange_rates.files.ConfigYML;
import ru.leonidm.exchange_rates.files.CurrenciesYML;
import ru.leonidm.telegram_utils.threads.RepeatingThread;

import java.util.List;

public class UpdateThread extends RepeatingThread {

    public UpdateThread() {
        super(ConfigYML.UPDATE_DELAY * 1000L, true);
    }

    @Override
    public void runAfterDelay() {
        StringBuilder messageBuilder = new StringBuilder(ConfigYML.TITLE.formatted(Utils.getFormattedCurrentTime())).append('\n');

        CurrenciesYML.updateCurrentRates();
        List<Pair<String, Double>> currentRates = CurrenciesYML.getCurrentRates();
        List<Pair<String, Double>> previousRates = CurrenciesYML.getPreviousRates();

        int previousSize = previousRates.size();

        for(int i = 0; i < currentRates.size(); i++) {
            Pair<String, Double> current = currentRates.get(i);

            if(i < previousSize) {
                Pair<String, Double> previous = previousRates.get(i);

                double currentRate = current.getSecond();
                double previousRate = previous.getSecond();

                if(currentRate == -1) {
                    currentRate = previousRate;
                }

                double delta = currentRate - previousRate;

                String arrow;
                if(delta > 0) arrow = "\uD83E\uDC15";
                else if(delta == 0) arrow = "~";
                else arrow = "\uD83E\uDC17";

                messageBuilder.append(ConfigYML.LINE_WITH_PREVIOUS.formatted(current.getFirst(),
                        currentRate, arrow, delta)).append('\n');
            }
            else {
                messageBuilder.append(ConfigYML.LINE.formatted(current.getFirst(), current.getSecond())).append('\n');
            }
        }

        String message = messageBuilder.substring(0, messageBuilder.length() - 1);

        ExchangeRatesM.getInstance().setMessage(message);
    }
}
