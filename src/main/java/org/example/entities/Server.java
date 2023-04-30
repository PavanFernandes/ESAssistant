package org.example.entities;

public class Server {

    private int id;

    private String serverId;

    private Clan clan;

    public Server(String serverId, Clan clan){
        this.serverId = serverId;
        this.clan = clan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }
}
