package org.example.starters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvKeys {

    public static String botToken;

    @Value("${bot.token.key}")
    public void setBotToken(String botToken) {
        EnvKeys.botToken = botToken;
    }


}
