package org.example.dao.clan;

import jakarta.transaction.Transactional;
import org.example.entities.Clan;

import java.util.List;

public interface ClanDao {

    int save(Clan clan);

    Clan find(int id);
    Clan find(String serverId);

    Clan findByName(String clanName);

    Clan findUsingQuery(String query);

    @Transactional
    List<String> runQuery(String query);

    List<Clan> getClanList(String serverId);

    Long getClansCount();

    Long checkClanName(String clanName);


}
