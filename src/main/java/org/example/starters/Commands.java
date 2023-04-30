package org.example.starters;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name", "give a name for report", true);
        OptionData user = new OptionData(OptionType.USER, "member", "select member", true);
        OptionData cr = new OptionData(OptionType.STRING, "corruption_resistance", "enter your raid corruption resistance", true);
        OptionData option1 = new OptionData(OptionType.STRING, "message", "enter data", true);
        OptionData playerName = new OptionData(OptionType.STRING, "name", "enter player name", true);
        OptionData raid = new OptionData(OptionType.STRING, "raid", "enter raid level", true);
        OptionData rank = new OptionData(OptionType.STRING, "rank", "enter rank ex-officer", true);
        OptionData hp = new OptionData(OptionType.STRING, "hp", "enter player hp", true);
        OptionData armor = new OptionData(OptionType.STRING, "armor", "enter your armor/def points", true);
        OptionData str = new OptionData(OptionType.STRING, "armor", "enter your armor/def points", true);
        OptionData katana1 = new OptionData(OptionType.STRING, "katana1", "enter your main katana", true);
        OptionData katana2 = new OptionData(OptionType.STRING, "katana2", "enter your alt katana", true);
        OptionData card = new OptionData(OptionType.USER, "card", "view member's profile", true);
        OptionData gmt = new OptionData(OptionType.STRING, "timezone", "enter ur time zone", true);
        OptionData clanName = new OptionData(OptionType.STRING, "clan_name","enter ur clan name", true);
        OptionData clan = new OptionData(OptionType.STRING, "clan_name","enter clan to be added", false);

        event.getGuild().updateCommands().addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("create_report", "create message ").addOptions(create))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("add_player", "add player").addOptions(user, cr, clan))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("create_clan", "create clan").addOptions(clanName))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("show_clans", "list of clans in the server"))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("raid_members", "raid members").addOptions(raid))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("remove_player", "Remove Player from the clan").addOptions(user , clan))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("report_list", "shows all reports created so far"))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("clan_status", "clan profile").addOptions(clan))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("card", "player card").addOptions(card))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("update_name", "update your name").addOptions(playerName))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("update_rank", "update rank"))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("update_raid", "update raid"))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("update_corruption_resistance", "update your corruption resistance").addOptions(cr))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("hero_power", "set your hero power").addOptions(hp))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("strength", "set your strength points").addOptions(str))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("armor", "set your armor points").addOptions(armor))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("katana", "set your katanas").addOptions(katana1, katana2))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("add_non_discord_player", " add non discord player").addOptions(playerName, raid, cr, rank))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("gmt", "set your gmt").addOptions(gmt))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("notice", "shows details").addOptions(option1)).queue();

    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {

        OptionData create = new OptionData(OptionType.STRING, "name", "give a name for report");
        OptionData user = new OptionData(OptionType.USER, "member", "select member");
        OptionData cr = new OptionData(OptionType.STRING, "cr", "enter your raid corruption resistance");
        OptionData option1 = new OptionData(OptionType.STRING, "message", "enter data");
        event.getJDA().updateCommands().addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("create_report", "create message ").addOptions(create))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("addplayer", "add player").addOptions(user, cr))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("report_list", "shows all reports created so far"))
                .addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("notice", "enter message to be sent back ").addOptions(option1)).queue();
    }

}
