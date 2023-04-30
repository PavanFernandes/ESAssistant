package org.example.EventListeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.entities.Clan;

import javax.management.Query;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static org.example.Main.theClanDao;
import static org.example.Main.thePlayerDao;


public class ClanListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String key = event.getName();

        switch (key) {

            case "create_clan" -> createClan(event);

            case "show_clans" -> showClans(event);

            case "clan_status" -> clanStatus(event);

            case "clan_profile" -> clanStatus(event);

            case "raid_members" -> getMembers(event);

        }
    }

    public void createClan(SlashCommandInteractionEvent event){

        String clanName = event.getOption("clan_name").getAsString();

        if(event.getUser().equals(Objects.requireNonNull(event.getGuild()).getOwner()) || event.getUser().getId().equals("715186187874074625")) {

            if(theClanDao.getClansCount() == 0 || theClanDao.checkClanName(clanName) == 0){
                String serverId = event.getGuild().getId();
                Clan clan = new Clan(clanName, serverId);
                theClanDao.save(clan);
                event.reply("``` Clan " + clanName + " is Created Successfully. ```").setEphemeral(true).queue();
            }  else {
                event.reply("``` Clan " + clanName + " already exists ```").setEphemeral(true).queue();
            }
        }
    }

    public void showClans(SlashCommandInteractionEvent event){

        List<Clan> clans = theClanDao.getClanList(event.getGuild().getId());
        String s = "";
        int i = 1;
        for(Clan clan : clans){
            s += i + ". " + clan.getClanName()  + "\n";
        }

        if(clans != null){

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Clan List");builder.setDescription(s);
            builder.setThumbnail(event.getGuild().getIconUrl());
            event.reply("").setEmbeds(builder.build()).setEphemeral(true).queue();;

        }else{
            event.reply("``` The server doesn't have any clan yet. ```").setEphemeral(true).queue();
        }
    }

    public void clanStatus(SlashCommandInteractionEvent event) {

        Clan clan = null;
        if(event.getOption("clan") == null){
            clan = theClanDao.find(event.getGuild().getId());
        }else {
            clan = theClanDao.findByName(event.getOption("clan").getAsString());
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle( clan.getClanName()  + " Clan");
        builder.setThumbnail("https://i.imgur.com/G8c6cHD.png");
        Long size = thePlayerDao.getPlayersCount(clan.getId());
        Long a = 50L - size;
        builder.setDescription(" " + size + "/50\n" + " Available spot in clan is " + a );
        event.reply("").setEmbeds(builder.build()).setEphemeral(true).queue();;
    }

    public void getMembers(SlashCommandInteractionEvent event){

        String raid = Objects.requireNonNull(event.getOption("raid")).getAsString();
        String query = "select distinct member_Name from clan, player where raid_lvl =" + raid + " and server_id = " + event.getGuild().getId();
        List<String> list = theClanDao.runQuery(query);
        int count = list.size();
        String s = "";
        for(String name : list){
            s+= name + "\n";
        }


        event.reply("``` here's the raid player's list ```").queue();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("``` Raid " + raid + "    -  ( " + count + " ) ```");
        builder.setThumbnail(event.getGuild().getIconUrl());
        builder.setDescription(s);
        builder.setColor(Color.CYAN);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();

    }

}
