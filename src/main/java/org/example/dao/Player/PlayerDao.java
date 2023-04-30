package org.example.dao.Player;

import net.dv8tion.jda.api.entities.User;
import org.example.entities.Clan;
import org.example.entities.Player;

public interface PlayerDao {

    boolean save(Player player, int clanId);

    Player find(int id);

    Player find(String id);

    Player find(String memberId, int clan);

    boolean update(Player player);

    boolean remove(User user, Clan clan);

     Long getPlayersCount(int clanId);

     void savePlayerData();


}
