package com.example.finalsports;

 import com.google.firebase.database.Exclude;

 import java.io.Serializable;
 import java.util.HashSet;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Set;

 public class Tournament implements Serializable {
     private String tournamentName,startDate,endDate;
     @Exclude
     private Set<Sports> setOfSports=new HashSet<>();
     public Tournament(String tournamentName,String startDate,String endDate){
         this.tournamentName=tournamentName;
         this.startDate=startDate;
         this.endDate=endDate;
     }
     public Tournament(){}
     public String getTournamentName(){
         return this.tournamentName;
     }
     public String getStartDate(){
         return this.startDate;
     }
     public String getEndDate(){
         return this.endDate;
     }
     public Sports getSportByName(String sportName){
         for(Sports sport:this.setOfSports){
             if(sportName.equalsIgnoreCase(sport.getSportName())){
                 return sport;
             }
         }
         return null;
     }
     private void removeSport(Sports sport){
         this.setOfSports.remove(sport);
     }
     public void removeSportByName(String sportName){
         removeSport(getSportByName(sportName));
     }

     public void addSport(Sports sport){
         this.setOfSports.add(sport);
     }
     public boolean checkAbsence(String name){
         for(Sports sport:this.setOfSports){
             if(name.equalsIgnoreCase(sport.getSportName())){
                 return false;
             }
         }
         return true;
     }
     public void setEndDate(String endDate){
         this.endDate=endDate;
     }
     public void setTournamentName(String tournamentName){
         this.tournamentName=tournamentName;
     }
     public void setStartDate(String startDate){
         this.startDate=startDate;
     }
     public void printListOfSports() {
         System.out.println("Tournament : " + this.tournamentName);
         for (Sports sport : this.setOfSports) {
             System.out.println(sport.getSportName());
         }
     }
     @Exclude
     public Set<Sports> getSetOfSports(){
         return new HashSet<>(this.setOfSports);
     }
}
