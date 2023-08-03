package com.example.finalsports;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class College implements Serializable {
    private static String collegeName;
    @Exclude
    private static Set<Tournament> setOfTournaments=new HashSet<>();

    public College(String collegeName){
        this.collegeName=collegeName;
    }
    public static String getCollegeName(){
        return collegeName;
    }

    private static void removeTournament(Tournament tournament){
        setOfTournaments.remove(tournament);
    }
    public static void removeTournamentByName(String tournamentName){
        removeTournament(getTournamentByName(tournamentName));
    }
    public static void addTournament(Tournament tournament){
        setOfTournaments.add(tournament);
    }

    public static Tournament getTournamentByName(String tournamentName){
        for(Tournament tournament:setOfTournaments){
            if(tournamentName.equalsIgnoreCase(tournament.getTournamentName())){
                return tournament;
            }
        }
        return null;
    }
    public static boolean checkAbsence(String name){
        for(Tournament tournament:setOfTournaments){
            if(name.equalsIgnoreCase(tournament.getTournamentName())){
                return false;
            }
        }
        return true;
    }
    public static void setCollegeName(String clgName){
        collegeName=clgName;
    }
    @Exclude
    public Set<Tournament> getSetOfTournaments(){
        return new HashSet<>(setOfTournaments);
    }
    public static void printListOfTournaments(){
        System.out.println("College: "+collegeName);
        for(Tournament tournament:setOfTournaments){
            System.out.println(tournament.getTournamentName());
        }
    }
}
