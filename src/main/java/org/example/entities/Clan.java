package org.example.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clan")
public class Clan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "server_id")
    private String serverId;

    @Column(name = "clan_name")
    private String clanName;

    @OneToMany(mappedBy = "clan", fetch=FetchType.EAGER , cascade= {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST,CascadeType.REFRESH})
    private List<Player> members;

//    @Column(name = "primary")
//    private boolean primary = true;


    public Clan(){

    }
    public Clan(String clanName, String serverId){
        this.clanName = clanName;
        this.serverId = serverId;
        this.members = new ArrayList<>();
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

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public void addMember(Player newMember){
        members.add(newMember);
        newMember.setClan(this);
    }

    public List<Player> getMembers() {
        return members;
    }
}
