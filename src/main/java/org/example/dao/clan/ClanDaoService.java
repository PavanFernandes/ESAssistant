package org.example.dao.clan;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.example.entities.Clan;
import org.example.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClanDaoService implements ClanDao{

    private EntityManager entityManager;

    @Autowired
    public ClanDaoService(EntityManager entityManger) {
        this.entityManager = entityManger;
    }

    @Override
    @Transactional
    public int save(Clan clan) {
        this.entityManager.persist(clan);
        return clan.getId();
    }

    @Override
    @Transactional
    public Clan find(int id) {
        return this.entityManager.find(Clan.class, id);
    }

    @Override
    public Clan find(String serverId) {
        TypedQuery<Clan> query = this.entityManager.createQuery("FROM Clan WHERE serverId =:serverId", Clan.class);
        query.setParameter("serverId", serverId);

        return query.getResultList().get(0);
    }

    @Override
    @Transactional
    public Clan findByName(String clanName){

        TypedQuery<Clan> query = this.entityManager.createQuery("FROM Clan WHERE clanName=:clanName", Clan.class);
        query.setParameter("clanName", clanName);

        System.out.println(query);

            return query.getSingleResult();

    }

    @Override
    @Transactional
    public Clan findUsingQuery(String query){

        TypedQuery<Clan> typedquery = this.entityManager.createQuery(query, Clan.class);

        return typedquery.getSingleResult();
    }

    @Override
    @Transactional
    public List runQuery(String query){

        List list = this.entityManager.createNativeQuery(query).getResultList();

        return list;
    }



    @Override
    @Transactional
    public List<Clan> getClanList(String serverId){

        TypedQuery<Clan> query = this.entityManager.createQuery("FROM Clan WHERE serverId=:serverId", Clan.class);
        query.setParameter("serverId", serverId);

        return query.getResultList();

    }

    @Override
    public Long getClansCount(){
        Long count = (Long) this.entityManager.createNativeQuery("SELECT COUNT(*) FROM clan").getSingleResult();
        return count;
    }

    @Override
    public Long checkClanName(String clanName){

        Query query = this.entityManager.createNativeQuery("SELECT COUNT(*) FROM clan where clan_name = ?1");
        query.setParameter(1, clanName);
        return (Long) query.getSingleResult();

    }
}
