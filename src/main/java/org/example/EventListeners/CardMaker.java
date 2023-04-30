package org.example.EventListeners;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import static org.example.Main.thePlayerDao;

public class CardMaker extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String key = event.getName();

        switch (key) {

            case "card" -> card(event);
        }
    }


    public void card(SlashCommandInteractionEvent event) {

        OptionMapping opt1 = event.getOption("card");
        User user = opt1.getAsUser();
        Player player  = thePlayerDao.find(user.getId());

        if(player!=null){
            BufferedImage image = drawImage(user, player);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try {
                ImageIO.write(image, "png", output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                InputStream bais = new ByteArrayInputStream(output.toByteArray());
                FileUpload fileUpload = FileUpload.fromData(bais,   "card.png");
                event.reply("").addFiles(fileUpload).queue();
                fileUpload.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            event.reply("To use this feature, you need to register as a player with the clan first.").queue();
        }
    }


    public BufferedImage drawImage(User user, Player player) {

        String[] bgs = {"holo1", "holo2", "holo2", "holo2"};

        Random rand = new Random();
        int index = rand.nextInt(bgs.length);
        String holo = bgs[index];

        File bg = new File(holo + ".jpg");

        File avatar = getUserAvatar(user);

        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        try {
            g.setColor(Color.WHITE);
            g.drawImage(ImageIO.read(bg), 0, 0, image.getWidth(), image.getHeight(), null);
            crop(avatar, g);
            g.setClip(0, 0, image.getWidth(), image.getHeight());
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = 220 , y = 110 , i =30;


            g.drawString("Name  :  " + player.getName(), x, y);
            g.drawString("Clan    :  ES", x, y+=30);
            g.drawString("HP      :  " + player.getHeroPower(), x, y+=30);
            g.drawString("Raid    :  " + player.getRaidLvl(), x, y+=30);
            g.drawString("Rank   :  " + player.getRank(), x, y+=30);

            g.setColor(Color.GRAY);
            g.drawLine(80, 250, 420, 250);
            g.setColor(Color.WHITE);

            g.drawString("Str    :  " + player.getStrength(), x-140, y+=70);
            g.drawString("Armor  :  " + player.getArmor(), x+30, y);
            g.drawString("level  :  75", x-140, y+=40);
            g.drawString("corruption  :  " + player.getCr(), x+30, y);
            g.drawString("Katana  :  " + player.getKatana1() + ", " + player.getKatana2(), x-140, y+=40);


        } catch (IOException e){
            throw new RuntimeException();
        }
        return image;
    }




    public  File getUserAvatar(User user){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(user.getAvatarUrl());
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File avatar = new File("avatar" + user.getName() + ".png");
        InputStream inputStream = null;
        try {
            inputStream = httpResponse.getEntity().getContent();
            Files.copy(inputStream, avatar.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return avatar;
    }

    public void crop(File avatar, Graphics2D g) throws IOException {
        int x = 75;
        int y = 90;
        int s = 120;
        int sy = 120;
        Polygon pic = new Polygon();
        pic.addPoint(x, y+sy);
        pic.addPoint((int) (x+(s*0.85)), y+sy );
        pic.addPoint(x+s, (int) (y + (sy*0.85)));
        pic.addPoint(x+s,y);
        pic.addPoint((int) (x+(s*0.15)), y );
        pic.addPoint(x, (int) (y + (sy*0.15)));

        Path2D path = new Path2D.Float();
        path.append(pic, true);

        g.setClip(path);

        g.drawImage(ImageIO.read(avatar), 75, 90, s, sy, null);
        avatar.delete();
    }
}

