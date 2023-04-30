package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.EventListeners.*;
import org.example.dao.Player.PlayerDao;
import org.example.dao.clan.ClanDao;
import org.example.entities.*;
import org.example.starters.Commands;
import org.example.starters.EnvKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.List;

@SpringBootApplication
public class Main extends ListenerAdapter {

    public static final String fileName = "data.txt";

    public static List<Player> ES = new ArrayList<>();

    public static List<Report> reports = new ArrayList<>();

    public static PlayerDao thePlayerDao;

    public static ClanDao theClanDao;


    public static void main(String[] args) throws LoginException {
        SpringApplication.run(Main.class, args);
        String token = EnvKeys.botToken;
        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.addEventListeners(new Main(), new Commands(), new ClanListener(), new PlayerListener(),
                new CardMaker(), new NoticeListener());
        builder.setActivity(Activity.watching("Server"));
        builder.build();

    }

    @Autowired
    public void doSomePlayerStuff(PlayerDao playerDao){
        this.thePlayerDao = playerDao;
        System.out.println("do some stuff =   " + thePlayerDao);
    }
    @Autowired
    public void doSomeClanStuff(ClanDao clanDao){
        this.theClanDao = clanDao;
        System.out.println("do some clan stuff =   " + theClanDao);
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String key = event.getName();

        switch (key) {

            case "notice" -> notice(event);

        }
    }

    public void notice(SlashCommandInteractionEvent event) {
        OptionMapping msgOpt = event.getOption("message");
        String msg = msgOpt.getAsString();
        event.getChannel().sendMessage(msg).queue();
    }
}