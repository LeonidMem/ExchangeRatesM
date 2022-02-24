package ru.leonidm.exchange_rates.files;

import org.glassfish.grizzly.utils.Pair;
import ru.leonidm.exchange_rates.entities.Currency;
import ru.leonidm.telegram_utils.config.ConfigurationSection;
import ru.leonidm.telegram_utils.config.YamlConfiguration;
import ru.leonidm.telegram_utils.logger.Logger;
import ru.leonidm.telegram_utils.utils.RRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesYML {

    private static final List<Currency> currencies = new ArrayList<>();

    public static void load() {
        YamlConfiguration config = new YamlConfiguration("currencies.yml");
        for(String key : config.getKeys()) {
            ConfigurationSection section = config.getSection(key);
            if(section == null) {
                Logger.warn("[currencies.yml] Currency \"%s\" is badly configured!", key);
                continue;
            }

            String name = section.getString("name");
            String link = section.getString("link");

            List<String> between = section.getStringList("between");
            if(between.size() != 2) {
                Logger.warn("[currencies.yml:%s] \"Between\"'s value is badly configured!", key);
                continue;
            }

            String after = between.get(0);
            String before = between.get(1);

            currencies.add(new Currency(name, new RRequest(link),
                    after, before));
        }
    }

    private static List<Pair<String, Double>> previousRates = Collections.emptyList();
    private static List<Pair<String, Double>> currentRates = Collections.emptyList();

    public static List<Pair<String, Double>> getPreviousRates() {
        return previousRates;
    }

    public static List<Pair<String, Double>> getCurrentRates() {
        return currentRates;
    }

    public static void updateCurrentRates() {
        previousRates = new ArrayList<>(currentRates);
        currentRates = currencies.stream()
                .map(currency -> {
                    double rate = currency.getRate();
                    if(rate == -1) {
                        for(Pair<String, Double> previous : previousRates) {
                            if(previous.getFirst().equals(currency.name())) {
                                rate = previous.getSecond();
                                break;
                            }
                        }
                    }

                    return new Pair<>(currency.name(), rate);
                })
                .collect(Collectors.toList());
    }
}
