package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
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
        boolean exits = Files.exists(path);
        if (exits)  {
            try {
                Scanner scanner= new Scanner(Files.newBufferedReader(path));
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
    }

       static List<Report> reports = new ArrayList<>();
       static List<Player> ES = new ArrayList<>();


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        String key = event.getName();

            if(key.equals("createreport")){
               createReport(event);
            }
            else if(key.equals("addplayer")) {
                addPlayer(event);
                }

            else if(key.equals("removeplayer")) {
                removePlayer(event);
            }

            else if (key.equals("notice")){
               notice(event);

            } else if (key.equals("reportlist")) {
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
            else if (key.equals("katana")) {
                 sword(event);
            }
            else if (key.equals("card")) {
                card(event);
            }

    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name","give a name for report",true);
        OptionData user = new OptionData(OptionType.USER, "member","select member",true);
        OptionData name = new OptionData(OptionType.STRING, "report","enter report name", true);
        OptionData cr = new OptionData(OptionType.STRING, "corruption_resistance","enter your raid corruption resistance",true);
        OptionData option1 = new OptionData(OptionType.STRING, "message","enter data",true);
        OptionData option2 = new OptionData(OptionType.STRING, "name", "enter player name",true);
        OptionData hp = new OptionData(OptionType.STRING, "hp", "enter player hp",true);
        OptionData armor = new OptionData(OptionType.STRING, "armor", "enter armor",true);
        OptionData sword = new OptionData(OptionType.STRING, "fc", "enter fc",true);
        OptionData card = new OptionData(OptionType.USER, "card", "enter member",true);

        event.getGuild().updateCommands().addCommands(Commands.slash("createreport", "create message ").addOptions(create))
        .addCommands(Commands.slash("addplayer", "add player").addOptions(name, user,cr))
        .addCommands(Commands.slash("removeplayer", "remove player").addOptions(name, option2))
        .addCommands(Commands.slash("reportlist", "shows all reports created so far"))
                .addCommands(Commands.slash("clanstatus", "clan detail"))
                .addCommands(Commands.slash("card", "player card").addOptions(card))
                .addCommands(Commands.slash("heropower", "set your hero power").addOptions(hp))
                .addCommands(Commands.slash("armor", "set your armor").addOptions(armor))
                .addCommands(Commands.slash("katana", "set your katana").addOptions(sword))
                .addCommands(Commands.slash("notice", "shows details").addOptions(option1)).queue();

    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name","give a name for report");
        OptionData name = new OptionData(OptionType.STRING, "report","enter report name");
        OptionData user = new OptionData(OptionType.USER, "member","select member");
        OptionData cr = new OptionData(OptionType.STRING, "cr","enter your raid corruption resistance");
        OptionData option1 = new OptionData(OptionType.STRING, "message","enter data");
        event.getJDA().updateCommands().addCommands(Commands.slash("create_report", "create message ").addOptions(create))
        .addCommands(Commands.slash("addplayer", "add player").addOptions(name, user, cr))
        .addCommands(Commands.slash("report_list", "shows all reports created so far"))
        .addCommands(Commands.slash("notice", "shows details").addOptions(option1)).queue();
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
                            file.write(i.getName() + "," + i.getId()+ "\n");
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

    public void addPlayer(SlashCommandInteractionEvent event){
        Player player;
        OptionMapping opt1 = event.getOption("report");
        OptionMapping opt2 = event.getOption("member");
        OptionMapping opt3 = event.getOption("corruption_resistance");
        String reportName = opt1.getAsString();
        User user = opt2.getAsUser();
        String cr = opt3.getAsString();
        event.getGuild();
        String nickname = event.getGuild().retrieveMemberById(user.getId()).complete().getNickname();
        String avatar = event.getMember().getAvatarUrl();
        List<Role> roles = event.getGuild().retrieveMember(user).complete().getRoles();
        boolean duplicate = false;
        if (ES.isEmpty()) {
            player = new Player(user, nickname, roles, cr);
            ES.add(player);
        } else {
            for (Player i : ES) {
                if (i.getMember().getName().equals(user.getName())) {
                    i.setPlayer(user, nickname, roles, cr);
                    duplicate = true;
                    break;
                }
            }
            if (duplicate == false) {
                player = new Player(user, nickname, roles, cr);
                ES.add(player);
            }
        }
        for (Report report : reports) {
            if (report.getName().equalsIgnoreCase(reportName)) {
                String rId = report.getId();
                String s = "";
                for (Player p : ES) {
                        s += "[ ` " + p.getName() + "`" + "       " + "`" + p.getRaidLvl() + " `" + "       "
                                + "`" + p.getCr() + "`" + "<:emoji_cr:938080002648461323>" + "      " + "` " + p.rank + "` ]" + "\n";
                }
                if(s.length()> 2000){
                    event.reply(" Message exceeds 2000 character, cannot add player anymore to this message "  +  "\n" +
                            " wait for  <@&772173310247043132> to resole this issue" ).queue();
                } else {
                    event.reply(user.getAsTag() + " is added successfully " + avatar).setEphemeral(true).queue();
                    event.getChannel().editMessageById(rId, s).queue();
                }
                break;
            }
        }
    }

    public void removePlayer(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("report");
        OptionMapping opt2 = event.getOption("name");
        String reportName = opt1.getAsString();
        String name = opt2.getAsString();
        for (Report report : reports) {
            if (report.getName().equalsIgnoreCase(reportName)) {
                for (Player player : ES) {
                    System.out.println(report.getId());
                    if (player.getName().trim().equalsIgnoreCase(name)) {
                        System.out.println(player.getName() + " " +report.getId() + " removed");
                        ES.remove(player);
                        String s = "";
                        for(Player p : ES){
                            s += "[ ` " + p.getName() + "`" + "       " + "`" + p.getRaidLvl() + " `" +"       "
                                    + "`" + p.getCr() + "`" + "<:emoji_cr:938080002648461323>" + "      " + "` " + p.rank + "` ]" + "\n";
                        }
                        System.out.println(s);
                        event.reply(name + " removed successfully").setEphemeral(true).queue();
                        String rId = report.getId();
                        event.getChannel().editMessageById(rId, s).queue();
                        break;
                    }
                }
            }
        }
    }

    public void notice(SlashCommandInteractionEvent event){
        OptionMapping msgOpt = event.getOption("message");
        String msg = msgOpt.getAsString();
        event.getChannel().sendMessage(msg).queue();
    }

    public void reportList(SlashCommandInteractionEvent event){
        Path path = Paths.get(fileName);
        try {
            ObjectInputStream  file = new ObjectInputStream(Files.newInputStream(path));
            boolean eof = false;
            while(!eof) {
                try {
                    Report report = (Report) file.readObject();
                    System.out.println(report.getName());
                    event.getMessageChannel().sendMessage(report.getName() + "\n").queue();
                } catch (EOFException e) {
                    eof = true;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void clanStatus(SlashCommandInteractionEvent event){

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Dream Catcher Clan");
        builder.setThumbnail("https://i.imgur.com/G8c6cHD.png");
        int size = ES.size();
        int a = 50 - size;
        builder.setDescription(size + "/50\n" + " Available spot in clan is " + a);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
        event.reply(" jjjjj");
    }

    public void sword(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("katana");
        String sword = opt1.getAsString();
        for(Player p : ES){
            if(p.getMember().getName().equals(event.getUser().getName())){
                p.setSword(sword);
            }
        }
    }

    public void heroPower(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("hp");
        String hp = opt1.getAsString();
        for(Player p : ES){
            if(p.getMember().getName().equals(event.getUser().getName())){
                p.setHeroPower(hp);
            }
        }
    }

    public void armor(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("armor");
        String armor = opt1.getAsString();
        for(Player p : ES){
            if(p.getMember().getName().equals(event.getUser().getName())){
                p.setArmor(armor);
            }
        }
    }

    public void card(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("card");
        User player = opt1.getAsUser();
        for(Player p : ES){
            if(p.getMember().getName().equals(player.getName())){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Samurai Rank Card");
                builder.setThumbnail("https://i.imgur.com/G8c6cHD.png");
                builder.setImage(player.getAvatarUrl());
                builder.setDescription(
                        "Name " + p.getName() + "\n" +
                        "Hero Power " + p.getHeroPower() + "\n"+
                        "Raid Lvl " + p.getRaidLvl() + "\n" +
                        "Armor " + p.getArmor() + "\n" +
                        "Katana " + p.getSword() + "\n"
                );
                event.getChannel().sendMessageEmbeds(builder.build()).queue();
            }

            }
    }


}