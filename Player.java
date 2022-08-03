package org.example;

import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class Player {
    User member;
    String name;
    String raidLvl;
    String cr;
    String rank;

    String sword;

    String HeroPower;

    String Armor;

    String otherfc;

    public Player(User member, String nickname, List<Role> roles, String cr) {
        this.member = member;
        String s;
        if( nickname != null){
             s = nickname;
        } else{
            s = member.getName();
        }
        String[] a = s.split(" ");
        StringBuilder sb = new StringBuilder(a[0]);
        if(sb.length() < 12){
             for(int i = sb.length(); i<12; i++){
                 sb.append(" ");
             }
        }
        else if (s.length() > 12){
             sb.delete(11, sb.length());
        }
        this.name = sb.toString();
        this.raidLvl = " NA";
        for (Role role : roles) {
            if (role.getName().contains("raid")) {
                this.raidLvl = role.getName().replace("raid" , "");
                break;
            }
        }

        this.cr = cr;
        this.rank = " null  ";
        for (Role role : roles) {
             String c= role.getName();
            if (c.equalsIgnoreCase("soldier") || c.equalsIgnoreCase("officer") || c.equalsIgnoreCase("shogun")) {
                if(c.equalsIgnoreCase("shogun")){
                   this.rank = role.getName() + " ";
                } else {
                    this.rank = role.getName();
                }
            }
        }
    }

    public void setPlayer(User member, String nickname, List<Role> roles, String cr) {
        this.member = member;
        String s;
        if( nickname != null){
            s = nickname;
        } else{
            s = member.getName();
        }
        String[] a = s.split(" ");
        StringBuilder sb = new StringBuilder(a[0]);
        if(sb.length() < 12){
            for(int i = sb.length(); i<12; i++){
                sb.append(" ");
            }
        }
        else if (s.length() > 12){
            sb.delete(11, sb.length());
        }
        this.name = sb.toString();
        this.raidLvl = " NA";
        for (Role role : roles) {
            if (role.getName().contains("raid")) {
                this.raidLvl = role.getName().replace("raid" , "");
                break;
            }
        }

        this.cr = cr;
        this.rank = " null  ";
        for (Role role : roles) {
            String c= role.getName();
            if (c.equalsIgnoreCase("soldier") || c.equalsIgnoreCase("officer") || c.equalsIgnoreCase("shogun")) {
                if(c.equalsIgnoreCase("shogun")){
                    this.rank = role.getName() + " ";
                } else {
                    this.rank = role.getName();
                }
            }
        }
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

    public String getSword() {
        return sword;
    }

    public void setSword(String sword) {
        this.sword = sword;
    }

    public String getHeroPower() {
        return HeroPower;
    }

    public void setHeroPower(String heroPower) {
        HeroPower = heroPower;
    }

    public String getArmor() {
        return Armor;
    }

    public void setArmor(String armor) {
        Armor = armor;
    }

    public String getOtherfc() {
        return otherfc;
    }

    public void setOtherfc(String otherfc) {
        this.otherfc = otherfc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRaidLvl() {
        return raidLvl;
    }


}
