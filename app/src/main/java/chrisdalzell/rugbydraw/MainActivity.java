package chrisdalzell.rugbydraw;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

    /**
     * Address for server where php scripts are
     */
    public static String SERVER_ADDRESS = "http://www.possumpam.com/rugby-scoring-app-scripts/";

    /**
     *  Data to be used for Division titles
     */
    public static List<String> divisions = Arrays.asList("Div 1", "Women", "Div 2", "Div 3", "Colts",
            "U18", "U16", "U14.5", "U13", "U11.5", "U10", "U8.5", "U7");

    /**
     * Contains all the game objects to be used in this activity
     */
    public static ArrayList<Game> games;

    /**
     * Contains all the team names used by the my teams activity
     */
    static List<String> teamsDiv1 = Arrays.asList("Hornby", "Waihora", "Lincoln", "Raikaia", "Methven", "Southbridge", "Burn/Duns/Irwell", "Glenmark", "Darfield",
            "Ashley", "Prebbleton", "Celtic", "Saracens", "Oxford", "Ohoka", "Kaiapoi", "West Melton", "Southern", "Hampstead", "Rolleston");
    static List<String> teamsWomen = Arrays.asList();
    static List<String> teamsDiv2 = Arrays.asList("Springston", "Diamond Harbour", "Darfield", "Banks Peninsula",
            "Southbridge", "Kirwee", "Lincoln", "Prebbleton", "Burn/Duns/Irwell");
    static List<String> teamsDiv3 = Arrays.asList("Hornby", "Waihora", "Kirwee", "Springston", "Burn/Duns/Irwell", "Lincoln", "Rolleston", "West Melton");
    static List<String> teamsColts = Arrays.asList("Banks Peninsula", "Waihora", "Prebbleton", "Celtic", "Lincoln Red", "Lincoln Black", "West Melton", "Darfield",
            "Springston", "Kirwee", "Burn/Duns/Irwell");
    static List<String> teamsU18 = Arrays.asList("Malvern Combined", "Waihora", "Rangiora High School", "Methven/Rakaia", "Hurunui",
            "Kaiapoi", "Ashley/Oxford", "West Melton/Rolleston", "Lincoln", "Celtic");
    static List<String> teamsU16 = Arrays.asList("Ashley/Amberley", "Oxford", "Waihora", "Rolleston", "Prebbleton", "West Melton/Southbridge", "Celtic",
            "Malvern", "Lincoln", "Kaiapoi", "Hampstead", "Hurunui", "Methven", "Saracens");
    static List<String> teamsU145 = Arrays.asList("Rolleston", "Prebbleton", "Malvern Combined", "West Melton", "Waihora", "Lincoln", "Duns/Southbr/Leest/Irwell");
    static List<String> teamsU13 = Arrays.asList("Rolleston Black", "Rolleston Gold", "West Melton", "Lincoln", "Waihora White", "Waihora Black", "Duns/Irwell/Leeston",
            "Prebbleton White", "Springston/Lincoln", "Prebbleton Blue", "Darfield", "Southbridge", "Malvern Combined");
    static List<String> teamsU115 = Arrays.asList("Rolleston Black", "Rolleston Gold", "Lincoln", "Southbridge", "Waihora",
            "Duns/Irwell", "West Melton Gold", "West Melton Blue", "Prebbleton Blue", "Prebbleton White", "Banks Peninsula",
            "Leeston", "Malvern Combined", "Prebbleton Green", "Prebbleton Red", "Springston");
    static List<String> teamsU10 = Arrays.asList("Rolleston Black", "Rolleston Gold", "Lincoln Red", "Lincoln Black", "Waihora White", "Waihora Black",
            "Duns/Irwell", "West Melton Gold", "West Melton Blue", "Prebbleton Blue", "Prebbleton White", "Banks Peninsula",
            "Leeston/Southbridge", "Prebbleton Green", "Prebbleton Red", "Springston", "Selwyn", "Darfield", "Rolleston Red", "Rolleston Blue");
    static List<String> teamsU85 = Arrays.asList("Rolleston Black", "Rolleston Gold", "Rolleston White", "Lincoln Red", "Lincoln Black", "Waihora White", "Waihora Black", "Waihora Red",
            "Duns/Irwell", "West Melton Gold", "West Melton Blue", "Prebbleton Blue", "Prebbleton White", "Banks Peninsula",
            "Leeston Red", "Leeston Black", "Prebbleton Green", "Prebbleton Red", "Springston Black", "Springston Green", "Selwyn", "Darfield", "Sheffield", "Rolleston Red",
            "Leeston White", "West Melton White", "Kirwee", "Southbridge");
    static List<String> teamsU7 = Arrays.asList("Rolleston Black", "Rolleston Gold", "Rolleston Red", "Rolleston Blue", "Rolleston White",
            "Lincoln Red (Section 1)", "Lincoln Red (Section 4)", "Lincoln Black", "Lincoln Green", "Lincoln White",
            "Waihora White", "Waihora Black", "Waihora Red", "Waihora Gold", "Waihora Green", "Duns/Irwell Blue", "Duns/Irwell Black",
            "West Melton Gold", "West Melton Blue", "West Melton White", "West Melton Red", "West Melton Black",
            "Prebbleton 1", "Prebbleton 2", "Prebbleton 3", "Prebbleton 4", "Prebbleton 5", "Prebbleton 6", "Prebbleton 7", "Prebbleton 8",
            "Banks Peninsula Maroon", "Banks Peninsula Gold", "Leeston Red", "Leeston Black", "Leeston White",
            "Springston Black", "Springston Green", "Springston White", "Selwyn Black", "Selwyn Green", "Darfield Red", "Darfield Blue", "Sheffield",
            "Kirwee Red", "Kirwee Yellow", "Kirwee White", "Kirwee Gold", "Southbridge White", "Southbridge Blue", "Southbridge Black",
            "Diamond Harbour White", "Diamond Harbour Blue");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sort all the collections alphabetically
        Collections.sort(teamsDiv1);
        Collections.sort(teamsWomen);
        Collections.sort(teamsDiv2);
        Collections.sort(teamsDiv3);
        Collections.sort(teamsColts);
        Collections.sort(teamsU18);
        Collections.sort(teamsU16);
        Collections.sort(teamsU145);
        Collections.sort(teamsU13);
        Collections.sort(teamsU115);
        Collections.sort(teamsU10);
        Collections.sort(teamsU85);
        Collections.sort(teamsU7);

        //TODO: HTML Email
        //TODO: Draw
        //TODO: Custom Colour Scheme
        //TODO: Enable Notifications
        //TODO: Live Scoring Notifications
        //TODO: Settings
        //new GameNotificationsAsyncTask().execute();

        try {
            final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if (!(activeNetwork != null && activeNetwork.isConnected())) {
                // if yes is clicked and user is offline, redirect them to network settings
                displayToast("Please connect to either wifi or a mobile network then reopen the draw");
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            } else {
                // Retrieve arraylist of every game stored in database from asynctask
                // and store it in static games variable
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            games = (ArrayList<Game>) new GetAllGames().execute(MainActivity.SERVER_ADDRESS + "get_all_games.php").get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GameNotificationsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // Prepare the pending intent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(),
                    0,
                    new Intent(MainActivity.this, GameNotifications.class),
                    PendingIntent.FLAG_CANCEL_CURRENT);

            // Set the game notifications to occur between 8am and 10am
            Random r = new Random();
            int minutes = r.nextInt(121) - 60;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9); // Set calender to 9am
            calendar.add(Calendar.DAY_OF_MONTH, 1); // tomorrow
            calendar.add(Calendar.MINUTE, minutes); // +- 60 minutes

            // Register the alert in the system.
            ((AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE))
                    .setInexactRepeating(
                            AlarmManager.RTC,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent);

            return null;
        }
    }

    // Displays a toast with passed in message
    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Retrieves all the games from the database
    private class GetAllGames extends AsyncTask {

        @Override
        protected ArrayList<Game> doInBackground(Object[] objects) {
            return getGames(objects[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, DrawFragmentActivity.class));
                }
            }, 3000);
        }
    }

    public static void updateGames() {
        try {
            //new UpdateGames().execute(MainActivity.SERVER_ADDRESS + "get_all_games.php");
            games = getGames(MainActivity.SERVER_ADDRESS + "get_all_games.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Game> getGames(Object urlString) {
        ArrayList<Game> games = new ArrayList<>();
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost((String) urlString);

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

        // Return ArrayList with every game stored in database
        return games;
    }
}
