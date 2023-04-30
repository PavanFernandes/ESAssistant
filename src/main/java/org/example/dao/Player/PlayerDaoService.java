package org.example.dao.Player;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.entities.User;
import org.example.entities.Clan;
import org.example.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.Main.*;

@Repository
public class PlayerDaoService implements PlayerDao{

    private EntityManager entityManager;

    @Autowired
    public PlayerDaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public boolean save(Player player, int clanId) {

        try {
            Clan clan = theClanDao.find(clanId);
            clan.addMember(player);
            this.entityManager.persist(player);
            return true;
        } catch (PersistenceException e) {
            e.printStackTrace();
            e.getMessage();
        } return false;
    }

    @Override
    public Player find(int id) {
        return this.entityManager.find(Player.class, id);
    }

    @Override
    @Transactional
    public Player find(String id) {
        TypedQuery<Player> query = this.entityManager.createQuery("FROM Player WHERE memberId=:id", Player.class);
        query.setParameter("id", id);
        try{
            return  query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


    @Override
    @Transactional
    public Player find(String memberId, int clanId) {

        Query query = this.entityManager.createNativeQuery("SELECT * FROM player WHERE member_id =?1 AND clan_id = ?2", Player.class);
        query.setParameter(1, memberId);
        query.setParameter(2, clanId);

        try{
            return  (Player) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Long getPlayersCount(int clanId){

        Long  count = (Long) this.entityManager.createNativeQuery("SELECT COUNT(*) FROM player where clan_id = " + clanId).getSingleResult();

        return count;

    }

    @Override
    @Transactional
    public boolean update(Player player) {
        try{
            this.entityManager.merge(player);
            return true;
        }catch (PersistenceException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public boolean remove(User user, Clan clan) {
        Player player = find(user.getId(), clan.getId());
        this.entityManager.remove(player);
        if(find(player.getId()) == null){
            return true;
        }else {
            return false;
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


}
