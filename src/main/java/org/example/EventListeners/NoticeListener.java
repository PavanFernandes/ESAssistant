package org.example.EventListeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.example.entities.Player;
import org.example.entities.Report;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static org.example.Main.*;

public class NoticeListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String key = event.getName();

        switch (key) {

            case "create report" -> createReport(event);

            case "report list" -> reportList(event);

        }
    }


    public static void createReport(SlashCommandInteractionEvent event) {
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

    public void Load(SlashCommandInteractionEvent event , String reportName){

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
                    event.getChannel().editMessageById(rId, s).queue();
                }
                break;
            }
        }
    }


}
