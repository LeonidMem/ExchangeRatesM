package ru.leonidm.exchange_rates;

import com.diogonunes.jcolor.Attribute;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.leonidm.exchange_rates.files.ConfigYML;
import ru.leonidm.exchange_rates.threads.UpdateThread;
import ru.leonidm.telegram_utils.TelegramLongPollingBotM;
import ru.leonidm.telegram_utils.logger.Logger;

public class ExchangeRatesM extends TelegramLongPollingBotM {

    private static final ExchangeRatesM instance = new ExchangeRatesM();

    private ExchangeRatesM() {
        super(ConfigYML.UNKNOWN_COMMAND, ConfigYML.USAGE);
        new UpdateThread().start();
    }

    @Override
    public String getBotUsername() {
        return "exchangeratesm_bot";
    }

    @Override
    public String getBotToken() {
        return "Sorry, it's very secret information :(";
    }

    public void setMessage(String text) {
        if(ConfigYML.MESSAGE_ID == 0) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(ConfigYML.CHAT_ID);
            sendMessage.setText(text);

            try {
                Message message = execute(sendMessage);
                int messageId = message.getMessageId();
                Logger.info("New message ID is %s", Attribute.GREEN_TEXT(), messageId);
                ConfigYML.MESSAGE_ID = messageId;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.enableMarkdown(true);
            editMessageText.setChatId(ConfigYML.CHAT_ID);
            editMessageText.setMessageId(ConfigYML.MESSAGE_ID);
            editMessageText.setText(text);

            try {
                execute(editMessageText);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    public static ExchangeRatesM getInstance() {
        return instance;
    }
}
