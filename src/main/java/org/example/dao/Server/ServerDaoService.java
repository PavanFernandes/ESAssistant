package org.example.dao.Server;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.entities.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServerDaoService implements ServerDao{

    private EntityManager entityManager;

    @Autowired
    public ServerDaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public int save(Server server) {
         this.entityManager.persist(server);
         return server.getId();
    }

    @Override
    public Server find(String id) {
        return this.entityManager.find(Server.class, id);
    }
}
