package com.example.finalsports;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Sports implements Serializable {
    private String sportName,startDate,endDate,sportType;
    private int maxNoOfPlayers;
    @Exclude
    private Set<Team> setOfTeams=new HashSet<>();
    public Sports(){}
    public Sports(String sportName,String sportType,int maxNoOfPlayers,String startDate,String endDate){
        this.sportName=sportName;
        this.sportType=sportType;
        this.maxNoOfPlayers=maxNoOfPlayers;
        this.startDate=startDate;
        this.endDate=endDate;
    }
    public void addTeam(Team team){
        this.setOfTeams.add(team);
    }
    public Team getTeamByName(String teamName){
        for(Team team:this.setOfTeams){
            if(teamName.equalsIgnoreCase(team.getTeamName())){
                return team;
            }
        }
        return null;
    }
    private void removeTeam(Team team){
        this.setOfTeams.remove(team);
    }
    public void removeTeamByName(String teamName){
        removeTeam(getTeamByName(teamName));
    }

    public String getSportType(){
        return this.sportType;
    }
    public String getSportName(){
        return this.sportName;
    }
    public String getStartDate(){
        return this.startDate;
    }
    public String getEndDate(){
        return this.endDate;
    }
    public int getMaxNoOfPlayers(){
        return this.maxNoOfPlayers;
    }
    public boolean checkAbsence(String name){
        for(Team team:this.setOfTeams){
            if(name.equalsIgnoreCase(team.getTeamName())){
                return false;
            }
        }
        return true;
    }
    public void printListOfTeams(){
        System.out.println("Sport : "+this.sportName);
        for(Team team:this.setOfTeams){
            System.out.println(team.getTeamName());
        }
    }
    @Exclude
    public Set<Team> getSetOfTeams(){
        return new HashSet<>(this.setOfTeams);
    }

    public void setEndDate(String endDate){
        this.endDate=endDate;
    }
    public void setStartDate(String startDate){
        this.startDate=startDate;
    }
    public void setSportName(String sportName){
        this.sportName=sportName;
    }
    public void setMaxNoOfPlayers(int maxNoOfPlayers) {
        this.maxNoOfPlayers = maxNoOfPlayers;
    }
    public void setSportType(String sportType){
        this.sportType=sportType;
    }
}
