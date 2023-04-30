package org.example.entities;

import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.apache.logging.log4j.util.StringBuilders.equalsIgnoreCase;

@Entity
@Table(name="player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="member_id")
    private String memberId;

    @Column(name="member_Name")
    private String name;

    @Column(name="member_rank")
    private String rank = "rank";

    @Column(name="raid_lvl")
    private String raidLvl = "raid";

    @Column(name="cr")
    private String cr = "cr";

    @Column(name="hero_power")
    private String HeroPower = "hp";

    @Column(name="strength")
    private String strength;

    @Column(name="armor")
    private String Armor = "armor";

    @Column(name="katana1")
    private String katana1 = "N/A";

    @Column(name="katana2")
    private String katana2 = "N/A";

    @Column(name="build_ss_url")
    private String BuildSsUrl = "url";

    @Column(name="gmt")
    private String gmt = "gmt";

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="clan_id")
    private Clan clan;

    public Player(){

    }

    public Player(String id, String name){
        this.memberId=id;
        this.name=name;
    }

    public Player(User member, String nickname, List<Role> roles, String cr) {
        System.out.println("in");
        setMemberId(member.getId());
        if (nickname != null) {
            this.name = nickname;
        } else {
            this.name = member.getName();
        }
        this.raidLvl = getRaidLvlByRoles(roles);
        if(this.raidLvl ==null){
            this.raidLvl = "90";
        }
        this.cr = cr;
        this.rank = getRankByRoles(roles);
        if(this.rank == null){
            this.rank = "soldier";
        }
        this.Armor = "";
        this.BuildSsUrl= "";
        this.HeroPower= "";
        this.gmt = "";

    }

    public Player(String memberId, String name, String raidLvl, String cr, String rank) {
        this.memberId = memberId;
        this.name = name;
        this.raidLvl = raidLvl;
        this.cr = cr;
        this.rank = rank;
    }

    public void setPlayer(User member, String nickname, List<Role> roles, String cr) {
        if (nickname != null) {
            this.name = nickname;
        } else {
            this.name = member.getName();
        }
        this.raidLvl = getRaidLvlByRoles(roles);
        this.cr = cr;
        this.rank = getRankByRoles(roles);
    }


    private String getNicknameto11(String s) {
        String[] a = s.split(" ");
        StringBuilder sb = new StringBuilder(a[0]);
        if (sb.length() < 12) {
            for (int i = sb.length(); i < 12; i++) {
                sb.append(" ");
            }
        } else if (s.length() > 12) {
            sb.delete(11, sb.length());
        }
        return sb.toString();
    }

    private String getRaidLvlByRoles(List<Role> roles) {

        for (Role role : roles) {
            String digitsOnly = StringUtils.getDigits(role.getName());
            if (digitsOnly.length() > 0 && Character.isDigit(digitsOnly.charAt(0))) {
                String raidLvl = role.getName().replaceAll("[^0-9]", "");
                return raidLvl;
            }
        }
        return "";
    }

    private String getRankByRoles(List<Role> roles) {

        for (Role role : roles) {
            String c = role.getName().toLowerCase();
            if (c.contains("soldier") || c.contains("officer") || c.contains("shogun")) {
                return role.getName();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCr() {
        return cr;
    }


    public void setCr(String cr) {
        this.cr = cr;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public void setRaidLvl(String raidLvl) {
        this.raidLvl = raidLvl;
    }

    public String getHeroPower() {
        return HeroPower;
    }

    public boolean setHeroPower(String heroPower) {
        this.HeroPower = heroPower;
        return true;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getArmor() {
        return Armor;
    }

    public boolean setArmor(String armor) {
        this.Armor = armor;
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRaidLvl() {
        return raidLvl;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String id) {
        this.memberId = id;
    }

    public String getGmt() {
        return gmt;
    }

    public boolean setGmt(String gmt) {
        this.gmt = gmt;
        return true;
    }

    public String getBuildSsUrl() {
        return BuildSsUrl;
    }

    public void setBuildSsUrl(String buildSsUrl) {
        BuildSsUrl = buildSsUrl;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }


    public String getKatana1() {
        return katana1;
    }

    public void setKatana1(String katana1) {
        this.katana1 = katana1;
    }

    public String getKatana2() {
        return katana2;
    }

    public void setKatana2(String katana2) {
        this.katana2 = katana2;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", raidLvl='" + raidLvl + '\'' +
                ", cr='" + cr + '\'' +
                ", HeroPower='" + HeroPower + '\'' +
                ", Armor='" + Armor + '\'' +
                ", BuildSsUrl='" + BuildSsUrl + '\'' +
                ", gmt='" + gmt + '\'' +
                '}';
    }
}
