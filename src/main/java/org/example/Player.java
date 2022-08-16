package org.example;

import net.dv8tion.jda.api.entities.*;
import java.util.List;

public class Player {
    private User member;
    private String name;
    private String raidLvl;
    private String cr;
    private String rank;
    private String HeroPower;
    private String Armor;
    private String memberId;
    private String gmt;

    public Player(User member, String nickname, List<Role> roles, String cr) {
        this.member = member;
        setMemberId(member.getId());
        String s;
        if (nickname != null) {
            s = nickname;
        } else {
            s = member.getName();
        }
        this.name = getNickname(s);
        this.raidLvl = getRaidLvlByRoles(roles);
        this.cr = cr;
        this.rank = getRankByRoles(roles);
    }

    public Player(String memberId, String name, String raidLvl, String cr, String rank) {
        this.memberId = memberId;
        this.name = name;
        this.raidLvl = raidLvl;
        this.cr = cr;
        this.rank = rank;
    }

    public void setPlayer(User member, String nickname, List<Role> roles, String cr) {
        this.member = member;
        String s;
        if (nickname != null) {
            s = nickname;
        } else {
            s = member.getName();
        }
        this.name = getNickname(s);
        this.raidLvl = getRaidLvlByRoles(roles);
        this.cr = cr;
        this.rank = getRankByRoles(roles);
    }


    private String getNickname(String s) {
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
            if (role.getName().contains("raid")) {
                return role.getName().replace("raid ", "");
            }
        }
        return " NA";
    }

    private String getRankByRoles(List<Role> roles) {
        for (Role role : roles) {
            String c = role.getName();
            if (c.equalsIgnoreCase("soldier") || c.equalsIgnoreCase("officer") || c.equalsIgnoreCase("shogun")) {
                if (c.equalsIgnoreCase("shogun")) {
                    return role.getName() + " ";
                } else {
                    return role.getName();
                }
            }
        }
        return " null  ";
    }

    public String getName() {
        return name;
    }

    public User getMember() {
        return member;
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
}
