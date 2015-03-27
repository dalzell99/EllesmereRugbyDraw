package chrisdalzell.rugbydraw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Game {
    long gameID;
    String homeTeamName;
    int homeTeamScore;
    String awayTeamName;
    int awayTeamScore;
    String location;
    int minutesPlayed;
    String time;
    String ref;
    String assRef1;
    String assRef2;
    ArrayList<ScoringPlay> scoringPlays;

    public Game(long gameID, String homeTeamName, int homeTeamScore, String awayTeamName,
                int awayTeamScore, String ref, String assRef1, String assRef2, String location,
                int minutesPlayed, String time, ArrayList<ScoringPlay> scoringPlays) {
        this.gameID = gameID;
        this.homeTeamName = homeTeamName;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamName = awayTeamName;
        this.awayTeamScore = awayTeamScore;
        this.location = location;
        this.minutesPlayed = minutesPlayed;
        this.time = time;
        this.scoringPlays = scoringPlays;
        this.ref = ref;
        this.assRef1 = assRef1;
        this.assRef2 = assRef2;
    }

    public ArrayList<ScoringPlay> getScoringPlays() {
        return scoringPlays;
    }

    public long getGameID() {
        return gameID;
    }

    public String getLocation() {
        return location;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public String getTime() {
        return time;
    }

    public String getStartTime() {
        String startTime = "";
        String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        // Check if game is being played today.
        if (String.valueOf(gameID).substring(0, 8).equals(today.substring(0, 8))) {
            startTime += "Today ";
        } else {
            try {
                Date date = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(gameID).substring(0, 8));
                startTime += new SimpleDateFormat("EE").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startTime += " ";
            startTime += String.valueOf(gameID).substring(6, 8);
            startTime += " ";
            startTime += Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                    "Aug", "Sep", "Oct", "Nov", "Dec").get(Integer.parseInt(String.valueOf(gameID).substring(4, 6)) - 1);
            startTime += " ";
        }
        startTime += time;
        return startTime;
    }

    public String getDate() {
        String dateString = "";
        String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        // Check if game is being played today.
        if (String.valueOf(gameID).substring(0, 8).equals(today.substring(0, 8))) {
            dateString += "Today ";
        } else {
            try {
                Date date = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(gameID).substring(0, 8));
                dateString += new SimpleDateFormat("EE").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateString += " ";
            dateString += String.valueOf(gameID).substring(6, 8);
            dateString += " ";
            dateString += Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                    "Aug", "Sep", "Oct", "Nov", "Dec").get(Integer.parseInt(String.valueOf(gameID).substring(4, 6)) - 1);
            dateString += " ";
        }

        return dateString;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getRef() { return ref; }

    public String getAssRef1() { return assRef1; }

    public String getAssRef2() { return assRef2; }
}
