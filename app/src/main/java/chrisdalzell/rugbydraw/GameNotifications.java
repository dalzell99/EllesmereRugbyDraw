package chrisdalzell.rugbydraw;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class GameNotifications extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent paramIntent) {
        int numTeams = 0;

        // Get users teams which are stored in sqlite3 database
        TeamsDB teamDB = new TeamsDB(context);
        ArrayList<String> teamList =  teamDB.getAllTeams();

        // Get an arraylist with all the games in it
        ArrayList<Game> games = new ArrayList<>();
        try {
            games = (ArrayList<Game>) new GetAllGames().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Game game : games) {
            // Get todays date in format yyyyMMdd
            String todaysDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).substring(0,8);
            // Extract date from gameID
            String gameDate = String.valueOf(game.getGameID()).substring(0,8);
            // Check if game is played today
            if (gameDate.equals(todaysDate)) {
                // check each team the user follows against gameID
                String homeTeamID = String.valueOf(game.getGameID()).substring(8,10);
                String awayTeamID = String.valueOf(game.getGameID()).substring(10,12);
                String divisionID = String.valueOf(game.getGameID()).substring(12,14);
                // Go through each of the teams the user follows and increase numTeams by 1 if
                // the teamID and divisionID from teamList are the same as the home or away team ID and
                // division ID respectively
                for (String s : teamList) {
                    if ((s.substring(0,2).equals(homeTeamID) || s.substring(0,2).equals(awayTeamID)) &&
                    s.substring(2,4).equals(divisionID)) {
                        numTeams += 1;
                    }
                }
            }
        }

        if (numTeams > 0) {
            // Create notification with the number of teams the user has selected to follow in the
            // MyTeamsActivity that are playing today.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ball)
                            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                            .setColor(context.getResources().getColor(R.color.light_blue))
                            .setContentTitle(numTeams == 1 ? "Game Today" : "Games Today")
                            .setContentText(numTeams + (numTeams == 1 ? " team " : " teams ") +
                                    "that you follow " + (numTeams == 1 ? " is " : " are ") + " playing today");
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MyTeamsActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    // Retrieves all the games from the database
    private static class GetAllGames extends AsyncTask {

        @Override
        protected ArrayList<Game> doInBackground(Object[] objects) {
            ArrayList<Game> games = new ArrayList<>();
            String result = "";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.possumpam.com/rugby-scoring-app-scripts/get_all_games.php");

                // Return all games between start and end dates
                HttpResponse response = httpclient.execute(httppost);

                // Retrieve json data to be processed
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result=sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error retrieving data " + e.toString());
            }

            try{
                // Check if any games were retrieved. This prevents most JSONExceptions.
                if (!result.equals("")) {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArrayList<ScoringPlay> scoringPlays = new ArrayList<>();
                        JSONObject json = jsonArray.getJSONObject(i);
                        // Retrieve String containing JSONArray of JSONArrays each containing
                        // minutesPlayed, play and description
                        String jsonString = json.getString("scoringPlays");
                        // Get JSONArray from String
                        JSONArray jsonArray1 = new JSONArray(jsonString);
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            // Get JSONArray from JSONArray
                            JSONArray object = jsonArray1.getJSONArray(j);
                            // Get minutesPlayed, scoring play and description from inner JSONArray
                            // and create a ScoringPlay object with them then add it to ArrayList of scoringPlays
                            // for this game
                            scoringPlays.add(new ScoringPlay(object.getInt(0), object.getString(1), object.getString(2)));
                        }
                        // Create Game from retrieved info and add it to games ArrayList
                        games.add(new Game(json.getLong("GameID"), json.getString("homeTeamName"), json.getInt("homeTeamScore"),
                                json.getString("awayTeamName"), json.getInt("awayTeamScore"), json.getString("ref"),
                                json.getString("assRef1"), json.getString("assRef2"), json.getString("location"),
                                json.getInt("minutesPlayed"), json.getString("time"), scoringPlays));
                    }
                }
            }catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }

            return games;
        }
    }
}