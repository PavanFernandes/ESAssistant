package org.example.EventListeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.StringUtils;
import org.example.entities.Clan;
import org.example.entities.Player;

import java.util.List;
import java.util.Objects;

import static org.example.Main.*;

public class PlayerListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String key = event.getName();

        switch (key) {

            case "add_player" -> addPlayer(event);

            case "add_nd_player" -> addNonDiscordPlayer(event);

            case "remove_player" -> removePlayer(event);

            case "update_name" -> updateName(event);

            case "update_rank" -> updateRank(event);

            case "update_raid" -> updateRaid(event);

            case "update_corruption_resistance" -> updateCr(event);

            case "hero_power" -> setHeroPower(event);

            case "strength" -> setStrength(event);

            case "armor" -> setArmor(event);

            case "gmt" -> setGmt(event);

            case "katana" -> setKatana(event);
        }
    }

    public void addPlayer(SlashCommandInteractionEvent event) {

        User user = Objects.requireNonNull(event.getOption("member")).getAsUser();

        boolean access = checkPermission(event);
        if (access) {
            boolean success = false;

            String cr = Objects.requireNonNull(event.getOption("corruption_resistance")).getAsString();
            String clanName = null;
            if (event.getOption("clan_name") != null) {
                clanName = event.getOption("clan_name").getAsString();
            }
            String nickname = Objects.requireNonNull(event.getGuild()).retrieveMemberById(user.getId()).complete().getNickname();
            List<Role> roles = event.getGuild().retrieveMember(user).complete().getRoles();

            Clan clan = null;

            if (clanName == null) {
                clan = theClanDao.find(event.getGuild().getId());
            } else {
                clan = theClanDao.findByName(clanName);
            }

            if (clan != null) {

                Player player = null;

                if (thePlayerDao.getPlayersCount(clan.getId()) != 0) {
                    player = thePlayerDao.find(user.getId(), clan.getId());
                }

                if (player == null) {
                    player = new Player(user, nickname, roles, cr);
                    success = thePlayerDao.save(player, clan.getId());
                } else {
                    player.setPlayer(user, nickname, roles, cr);
                    success = thePlayerDao.update(player);
                }
                if (success) {
                    event.reply("``` " + player.getName() + " is now part of clan : " + clan.getClanName()
                            + "``` ").queue();
                }
            } else {
                event.reply("``` no clan found to add member ```").queue();
            }
        }
    }


    public void addNonDiscordPlayer(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("name");
        OptionMapping opt2 = event.getOption("raid");
        OptionMapping opt3 = event.getOption("corruption_resistance");
        OptionMapping opt4 = event.getOption("rank");
        Player player = new Player("ND", opt1.getAsString(), opt2.getAsString(), opt3.getAsString(), opt4.getAsString());
        Clan clan = null;

        String clanName = null;
        OptionMapping opt5 = event.getOption("clan_name");
        if (opt5 != null) {
            clanName = opt5.getAsString();
        }

        if (clanName == null) {
            clan = theClanDao.find(Objects.requireNonNull(event.getGuild()).getId());
        } else {
            clan = theClanDao.findByName(clanName);
        }

        if (clan != null) {
            boolean success = thePlayerDao.save(player, clan.getId());

            if (success) {
                event.reply("``` " + player.getName() + " is now part of clan : " + clan.getClanName()
                        + "``` ").queue();
            }
        } else {
            event.reply("``` no clan found to add member ```").queue();
        }
    }

    public void removePlayer(SlashCommandInteractionEvent event) {

        boolean access = checkPermission(event);

        if (access) {
            Clan clan = null;
            User user = Objects.requireNonNull(event.getOption("member")).getAsUser();
            OptionMapping opt2 = event.getOption("clan");
            if(opt2 != null){
                clan = theClanDao.findByName(opt2.getAsString());
            }else {
                clan = theClanDao.find(Objects.requireNonNull(event.getGuild()).getId());
            }
            boolean removed = thePlayerDao.remove(user, clan);
            if (removed) {
                event.reply("```" + event.getUser().getName() + " has been removed successfully ```").queue();
            } else {
                event.reply("``` unsuccessful in removing " + event.getUser().getName()+ ". ```").queue();
            }
        }
    }

    private void updateName(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("name");
        if (opt1 != null) {
            String name = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setName(name);
            thePlayerDao.update(player);
            event.reply(event.getUser().getName() + " your name = " + player.getName()
                    + " is updated successfully ").setEphemeral(true).queue();
        } else {
            event.reply("name value is missing , failed to update.").setEphemeral(true).queue();
        }

    }

    private void updateRank(SlashCommandInteractionEvent event) {

        List<Role> roles = event.getGuild().retrieveMember(event.getUser()).complete().getRoles();
            String rank = getRankByRoles(roles);
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setRank(rank);
            thePlayerDao.update(player);
            event.reply(event.getUser().getName() + " your rank = " + player.getRank()
                    + " is updated successfully ").setEphemeral(true).queue();
        }

    private void updateRaid(SlashCommandInteractionEvent event) {

        List<Role> roles = event.getGuild().retrieveMember(event.getUser()).complete().getRoles();
        String raid = getRaidLvlByRoles(roles);
        Player player = thePlayerDao.find(event.getUser().getId());
        player.setRaidLvl(raid);
        thePlayerDao.update(player);
        event.reply(event.getUser().getName() + " your raid level = " + player.getRaidLvl()
                + " is updated successfully ").setEphemeral(true).queue();
    }

    public void updateCr(SlashCommandInteractionEvent event){
        OptionMapping opt1 = event.getOption("corruption_resistance");
        if (opt1 != null) {
            String cr = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setCr(cr);
            thePlayerDao.update(player);
            event.reply(event.getUser().getName() + " your corruption resistance  = " + player.getCr()
                    + " is set successfully ").setEphemeral(true).queue();
        } else {
            event.reply("corruption resistance value is missing , failed to update.").setEphemeral(true).queue();
        }
    }

    public void setHeroPower(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("hp");
        if (opt1 != null) {
            String hp = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setHeroPower(hp);
            thePlayerDao.update(player);
            event.reply("```" + event.getUser().getName() + " your hp = " + player.getHeroPower()
                    + " is set successfully ```").setEphemeral(true).queue();
        } else {
            event.reply("``` hero power value is missing , failed to update. ```").setEphemeral(true).queue();
        }
    }

    public void setStrength(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("str");
        if (opt1 != null) {
            String str = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setStrength(str);
            thePlayerDao.update(player);
            event.reply(event.getUser().getName() + " your strength = " + player.getStrength()
                    + " is set successfully ").setEphemeral(true).queue();
        } else {
            event.reply("strength value is missing , failed to update.").setEphemeral(true).queue();
        }
    }

    public void setArmor(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("armor");
        if (opt1 != null) {
            String armor = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setArmor(armor);
            thePlayerDao.update(player);
            event.reply("```" + event.getUser().getName() + " your armor = " + player.getArmor() +
                            " is set successfully ```").setEphemeral(true).queue();
        } else {
            event.reply("``` armor value is missing , failed to update. ```").setEphemeral(true).queue();
        }
    }

    public void setKatana(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("katana1");
        OptionMapping opt2 = event.getOption("katana2");
        if (opt1 != null) {
            String katana1 = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setKatana1(katana1);
            if(opt2 != null){
                String katana2 = opt2.getAsString();
                player.setKatana2(katana2);
           }
            thePlayerDao.update(player);
            event.reply("```" + event.getUser().getName() + " your katana1 = " + player.getKatana1() + " and katana2 = " +
                    player.getKatana2() + " is set successfully ```").setEphemeral(true).queue();
        }
         else {
            event.reply("``` katana value is missing , failed to update. ```").setEphemeral(true).queue();
        }
    }

    public void setGmt(SlashCommandInteractionEvent event) {
        OptionMapping opt1 = event.getOption("timezone");
        if (opt1 != null) {
            String gmt = opt1.getAsString();
            Player player = thePlayerDao.find(event.getUser().getId());
            player.setGmt(gmt);
            thePlayerDao.update(player);
            event.reply(event.getUser().getName()+ " your timezone  = " + player.getGmt() + " is set successfully ").setEphemeral(true).queue();
        } else {
            event.reply("gmt value is missing , failed to update.").setEphemeral(true).queue();
        }
    }



    private boolean checkPermission(SlashCommandInteractionEvent event) {
        List<Role> roles = event.getGuild().retrieveMember(event.getUser()).complete().getRoles();
        for (Role role : roles) {
            String c = role.getName().toLowerCase();
            if (c.contains("shogun")) {
                return true;
            }
        }
        return false;
    }

    private String getRankByRoles(List<Role> roles) {

        for (Role role : roles) {
            String c = role.getName().toLowerCase();
            if (c.contains("soldier") || c.contains("officer") || c.contains("shogun")) {
                return role.getName();
            }
        }
        return "";
    }

    private String getRaidLvlByRoles(List<Role> roles) {

        for (Role role : roles) {
            String digitsOnly = StringUtils.getDigits(role.getName());
            if (digitsOnly.length() > 0 && Character.isDigit(digitsOnly.charAt(0))) {
                String raidLvl = role.getName().replaceAll("[^0-9]", "");
                return raidLvl;
            }
        }
        return "";
    }


}
