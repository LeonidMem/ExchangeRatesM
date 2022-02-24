package ru.leonidm.exchange_rates;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.leonidm.exchange_rates.files.ConfigYML;
import ru.leonidm.exchange_rates.files.CurrenciesYML;
import ru.leonidm.telegram_utils.logger.Logger;
import ru.leonidm.telegram_utils.utils.ConfigUtils;

public class Main {

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            Logger.info("Loading config...");
            ConfigUtils.load("config.yml", ConfigYML.class);

            Logger.info("Loading currencies...");
            CurrenciesYML.load();

            Logger.info("Initializing telegram bot...");
            botsApi.registerBot(ExchangeRatesM.getInstance());

            Logger.info("Enabled!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
