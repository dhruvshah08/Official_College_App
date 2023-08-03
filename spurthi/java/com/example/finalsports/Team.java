package com.example.finalsports;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Team implements Serializable {
    private String teamName;
    @Exclude
    private Set<Player> setOfPlayers=new HashSet<>();
    public Team(){}
    public Team(String teamName){
        this.teamName=teamName;
    }
    public String getTeamName(){
        return this.teamName;
    }
    public void addPlayer(Player player) {
        this.setOfPlayers.add(player);
    }
    public Player getPlayerByName(String playerName){
        for(Player player:this.setOfPlayers){
            if(playerName.equalsIgnoreCase(player.getName())){
                return player;
            }
        }
        return null;
    }
    private void removePlayer(Player player){
        this.setOfPlayers.remove(player);
    }
    public void removePlayerByName(String playerName){
        removePlayer(getPlayerByName(playerName));
    }
    public boolean checkAbsence(String name){
        for(Player player:this.setOfPlayers){
            if(name.equalsIgnoreCase(player.getName())){
                return false;
            }
        }
        return true;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void printAllPlayers(){
        System.out.println("Team :"+this.teamName);
        for(Player player:this.setOfPlayers){
            System.out.println("Player Name: "+player.getName());
        }
    }
    @Exclude
    public Set<Player> getSetOfPlayers(){
        return new HashSet<>(this.setOfPlayers);
    }
}
/*
* This class would consist of a list of players of a particular team of ant sport in a set!
* */