package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class Main extends ListenerAdapter {
    private static final String fileName = "data.txt";

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault("OTY1OTUwMDg5ODEyMTI3NzY1.GbQI2s.ySIR_x7_wc00wdhop7mQ_waX8VKQ6Qn4eDZGiI", GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.addEventListeners(new Main());
        builder.setActivity(Activity.watching("Server"));
        builder.build();

        Path path = Paths.get(fileName);
        Path path2 = Paths.get("PlayersData.txt");
        boolean exits = Files.exists(path);
        if (exits) {
            try {
                Scanner scanner = new Scanner(Files.newBufferedReader(path));
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    String title = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String id = scanner.nextLine();
                    Report report = new Report(title, id);
                    reports.add(report);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean exits2 = Files.exists(path2);
        if (exits2) {
            try {
                Scanner scanner = new Scanner(Files.newBufferedReader(path2));
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    String id = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String name = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String raid = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String cr = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String rank = scanner.nextLine();
                    Player p = new Player(id, name, raid, cr, rank);
                    ES.add(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static List<Report> reports = new ArrayList<>();
    static List<Player> ES = new ArrayList<>();





    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name", "give a name for report", true);
        OptionData user = new OptionData(OptionType.USER, "member", "select member", true);
        OptionData cr = new OptionData(OptionType.STRING, "corruption_resistance", "enter your raid corruption resistance", true);
        OptionData option1 = new OptionData(OptionType.STRING, "message", "enter data", true);
        OptionData playerName = new OptionData(OptionType.STRING, "name", "enter player name", true);
        OptionData hp = new OptionData(OptionType.STRING, "hp", "enter player hp", true);
        OptionData armor = new OptionData(OptionType.STRING, "armor", "enter armor", true);
        OptionData card = new OptionData(OptionType.USER, "card", "enter member", true);
        OptionData raid = new OptionData(OptionType.STRING, "raid", "enter raid lvl", true);
        OptionData rank = new OptionData(OptionType.STRING, "rank", "enter rank", true);
        OptionData gmt = new OptionData(OptionType.STRING, "timezone", "enter ur time zone", true);

        event.getGuild().updateCommands().addCommands(Commands.slash("createreport", "create message ").addOptions(create))
                .addCommands(Commands.slash("addplayer", "add player").addOptions(user, cr))
                .addCommands(Commands.slash("removeplayer", "remove player").addOptions(playerName))
                .addCommands(Commands.slash("reportlist", "shows all reports created so far"))
                .addCommands(Commands.slash("clanstatus", "clan detail"))
                .addCommands(Commands.slash("card", "player card").addOptions(card))
                .addCommands(Commands.slash("heropower", "set your hero power").addOptions(hp))
                .addCommands(Commands.slash("armor", "set your armor points").addOptions(armor))
                .addCommands(Commands.slash("addnondiscordplayer", " add non discord player").addOptions(playerName, raid, cr, rank))
                .addCommands(Commands.slash("gmt", "set your gmt").addOptions(gmt))
                .addCommands(Commands.slash("notice", "shows details").addOptions(option1)).queue();

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name", "give a name for report");
        OptionData user = new OptionData(OptionType.USER, "member", "select member");
        OptionData cr = new OptionData(OptionType.STRING, "cr", "enter your raid corruption resistance");
        OptionData option1 = new OptionData(OptionType.STRING, "message", "enter data");
        event.getJDA().updateCommands().addCommands(Commands.slash("create_report", "create message ").addOptions(create))
                .addCommands(Commands.slash("addplayer", "add player").addOptions(user, cr))
                .addCommands(Commands.slash("report_list", "shows all reports created so far"))
                .addCommands(Commands.slash("notice", "enter message to be sent back ").addOptions(option1)).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String key = event.getName();

        if (key.equals("createreport")) {
            createReport(event);
        } else if (key.equals("addplayer")) {
            addPlayer(event);
        }
        else if(key.equals("addnondiscordplayer")){
            addNonDiscordPlayer(event);
        }
        else if (key.equals("removeplayer")) {
            removePlayer(event);
        }
        else if (key.equals("notice")) {
            notice(event);
        }
        else if (key.equals("reportlist")) {
            reportList(event);
        }
        else if (key.equals("clanstatus")) {
            clanStatus(event);
        }
        else if (key.equals("heropower")) {
            heroPower(event);
        }
        else if (key.equals("armor")) {
            armor(event);
        }
        else if (key.equals("gmt")) {
           setGmt(event);
        }
        else if (key.equals("card")) {
            card(event);
        }
    }
    public void createReport(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("name");
        String name = opt1.getAsString();
        event.getMessageChannel().sendMessage(name).queue(new Consumer<Message>() {
            @Override
            public void accept(Message message) {
                String id = message.getId();
                Report report = new Report(name, id);
                reports.add(report);
                Path path = Paths.get(fileName);
                boolean exits = Files.exists(path);
                if (!exits) {
                    try (BufferedWriter file = new BufferedWriter(new FileWriter(fileName))) {
                        for (Report i : reports) {
                            file.write(i.getName() + "," + i.getId() + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try (BufferedWriter file = Files.newBufferedWriter(path)) {
                        for (Report i : reports) {
                            file.write(i.getName() + "," + i.getId() + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        event.reply("created successfully").setEphemeral(true).queue();
    }

    public void addPlayer(SlashCommandInteractionEvent event) {

        OptionMapping opt2 = event.getOption("member");
        User user = opt2.getAsUser();
        boolean access = checkPermission(event, user);
        if (access) {
            OptionMapping opt3 = event.getOption("corruption_resistance");
            String cr = opt3.getAsString();
            System.out.println(user + " " + user.getId() + " " + user.getAsTag());
            Player player = null;
            String nickname = event.getGuild().retrieveMemberById(user.getId()).complete().getNickname();
            List<Role> roles = event.getGuild().retrieveMember(user).complete().getRoles();
            boolean duplicate = false;
            String reportName;
            if (ES.isEmpty()) {
                player = new Player(user, nickname, roles, cr);
                ES.add(player);
                savePlayerData();
            } else {
                for (Player i : ES) {
                    if (i.getMemberId().equals(user.getId())) {
                        i.setPlayer(user, nickname, roles, cr);
                        player = i;
                        savePlayerData();
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate == false) {
                    player = new Player(user, nickname, roles, cr);
                    ES.add(player);
                    savePlayerData();
                }
            }

            reportName = player.getRaidLvl();

            for (Report report : reports) {
                if (report.getName().equalsIgnoreCase(reportName)) {
                    String rId = report.getId();
                    String s = "";
                    for (Player p : ES) {
                        if (p.getRaidLvl().equals(reportName)) {
                            s += "[ ` " + p.getName() + "`" + "       " + "` " + p.getRaidLvl() + " `" + "      " + "` " + p.getRank() + "`" +
                                    "       " + "`" + p.getCr() + "`" + "<:emoji_cr:938080002648461323>" + " ]" + "\n";
                        }
                    }
                    if (s.length() > 2000) {
                        event.reply(" Message exceeds 2000 character, cannot add player anymore to this message " + "\n" +
                                " wait for  <@&772173310247043132> to resole this issue").queue();
                    } else {
                        event.reply(user.getAsTag() + " is added successfully ").setEphemeral(true).queue();
                        event.getChannel().editMessageById(rId, s).queue();
                    }
                    break;
                }
            }
        }
    }

    public void removePlayer(SlashCommandInteractionEvent event) {
        boolean access = checkPermission(event, null);
        if (access) {
            OptionMapping opt2 = event.getOption("name");
            String name = opt2.getAsString();
            for (Player player : ES) {
                if (player.getName().trim().equalsIgnoreCase(name)) {
                    String reportName = player.getRaidLvl();
                    ES.remove(player);
                    savePlayerData();
                    String s = "";
                    for (Player p : ES) {
                        if (p.getRaidLvl().equals(reportName)) {
                            s += "[ ` " + p.getName() + "`" + "       " + "` " + p.getRaidLvl() + " `" + "      " + "` " + p.getRank() + "`" +
                                    "       " + "`" + p.getCr() + "`" + "<:emoji_cr:938080002648461323>" + " ]" + "\n";
                        }
                    }
                    for (Report report : reports) {
                        if (report.getName().equalsIgnoreCase(reportName)) {
                            String rId = report.getId();
                            event.reply(name + " removed successfully").setEphemeral(true).queue();
                            event.getChannel().editMessageById(rId, s).queue();
                        }
                    }
                    break;
                }
            }
        }
    }

     public void addNonDiscordPlayer(SlashCommandInteractionEvent event){
         OptionMapping opt1 = event.getOption("name");
         OptionMapping opt2 = event.getOption("raid");
         OptionMapping opt3 = event.getOption("corruption_resistance");
         OptionMapping opt4 = event.getOption("rank");
         Player ndPlayer = new Player("ND",opt1.getAsString(), opt2.getAsString(), opt3.getAsString(), opt4.getAsString());
         ES.add(ndPlayer);
    }

    public void notice(SlashCommandInteractionEvent event) {
        OptionMapping msgOpt = event.getOption("message");
        String msg = msgOpt.getAsString();
        event.getChannel().sendMessage(msg).queue();
    }

    public void reportList(SlashCommandInteractionEvent event) {
        Path path = Paths.get(fileName);
        try {
            ObjectInputStream file = new ObjectInputStream(Files.newInputStream(path));
            boolean eof = false;
            while (!eof) {
                try {
                    Report report = (Report) file.readObject();
                    System.out.println(report.getName());
                    event.getMessageChannel().sendMessage(report.getName() + "\n").queue();
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void clanStatus(SlashCommandInteractionEvent event) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Dream Catcher Clan");
        builder.setThumbnail("https://i.imgur.com/G8c6cHD.png");
        int size = ES.size();
        int a = 50 - size;
        builder.setDescription(" " + size + "/50\n" + " Available spot in clan is " + a);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
        event.reply("done:)");
    }

    public void heroPower(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("hp");
        String hp = opt1.getAsString();
        for (Player p : ES) {
            if (p.getMemberId().equals(event.getUser().getId())) {
                if(p.setHeroPower(hp)){
                 event.reply(p.getName().trim()+ " your HP = " + hp + " is set successfully " ).setEphemeral(true).queue();
                }
            }
        }
    }

    public void armor(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("armor");
        String armor = opt1.getAsString();
        for (Player p : ES) {
            if(p.getMemberId().equals(event.getUser().getId())) {
                if(p.setArmor(armor)){
                    event.reply(p.getName().trim()+ " your armor = " + armor + " is set successfully ").setEphemeral(true).queue();
                }
            }
        }
    }

    public void setGmt(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("timezone");
        String gmt = opt1.getAsString();
        for (Player p : ES) {
            if(p.getMemberId().equals(event.getUser().getId())) {
                if(p.setGmt(gmt)){
                    event.reply(p.getName().trim()+ " Time zone  = " + gmt  + " is set successfully ").setEphemeral(true).queue();
                }
            }
        }
    }

    public void card(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("card");
        User user = opt1.getAsUser();
        for (Player p : ES) {
            if (p.getMemberId().equals(user.getId())) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Samurai Rank Card");
//                builder.setImage("https://i.imgur.com/G8c6cHD.png");
                builder.setThumbnail(user.getAvatarUrl());
                builder.setDescription(
                        "Name " + p.getName() + "\n" +
                                "Hero Power " + p.getHeroPower() + "\n" +
                                "Raid Lvl " + p.getRaidLvl() + "\n" +
                                "Armor " + p.getArmor() + "\n" +
                                "Corruption Resistance " + p.getCr() + "<:emoji_cr:938080002648461323>" + "\n" +
                                "GMT " + p.getGmt() + "\n"
                );
                event.getChannel().sendMessageEmbeds(builder.build()).queue();
                break;
            }
        }
    }

    public void savePlayerData() {

        try (BufferedWriter file = new BufferedWriter(new FileWriter("PlayersData.txt"))) {
            for (Player i : ES) {
                file.write(i.getMemberId() + "," + i.getName() + "," + i.getRaidLvl() +
                        "," + i.getCr() + "," + i.getRank() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkPermission(SlashCommandInteractionEvent event, User user){
        if (event.getUser() == user) {
            return true;
        } else {
            for (Player i : ES) {
                if (i.getMemberId().equals(event.getMember().getId())) {
                    if (i.getRank().trim().equalsIgnoreCase("shogun")) {
                        return true;
                    }
                }
            }
        } return false;
    }

}