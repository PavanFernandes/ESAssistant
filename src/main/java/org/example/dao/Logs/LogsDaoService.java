package org.example.dao.Logs;

import jakarta.persistence.EntityManager;
import org.example.entities.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogsDaoService implements LogsDao{

    private EntityManager entityManager;

    @Autowired
    public LogsDaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int save(Logs logs) {
        return 0;
    }
}
