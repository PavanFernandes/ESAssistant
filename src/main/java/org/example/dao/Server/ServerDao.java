package org.example.dao.Server;

import org.example.entities.Server;

public interface ServerDao {

    int save(Server server);

    Server find(String id);
}
