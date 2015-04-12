package chrisdalzell.rugbydraw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MyTeamsActivity extends Activity {

    TeamsDB teamDB = new TeamsDB(this);
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Workaround for swipe to refresh bug
        if (getIntent().getBooleanExtra("refresh", false)) {
            Intent intent = new Intent(MyTeamsActivity.this, DrawFragmentActivity.class);
            intent.putExtra("weekNumber", getIntent().getIntExtra("weekNumber", -1));
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbarMyTeam);
        TextView textViewToolbarTitle = (TextView) toolbar.findViewById(R.id.textViewToolbarTitle);
        textViewToolbarTitle.setText("My Teams");

        FloatingActionButton fabSelectTeam = (FloatingActionButton) findViewById(R.id.fabSelectTeam);
        fabSelectTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyTeamsActivity.this, SelectTeamsActivity.class));
            }
        });

        // Get users teams which are stored in sqlite3 database
        ArrayList<String> teamList =  teamDB.getAllTeams();

        LinearLayout linearLayoutTeams = (LinearLayout) findViewById(R.id.linearLayoutTeams);
        if (teamList.size() > 0) {
            // Iterate through all teams
            for (String s : teamList) {
                // Extract team and division IDs from database string
                String team = s.substring(0, 2);
                String division = s.substring(2, 4);

                // Create linearlayout to put games in
                final LinearLayout linearLayoutGames = new LinearLayout(this);
                linearLayoutGames.setOrientation(LinearLayout.VERTICAL);
                linearLayoutGames.setVisibility(View.GONE);

                // Create title for team
                View tableRowTeamTitle = getLayoutInflater().inflate(R.layout.result_division_title, linearLayoutTeams, false);
                TextView textViewDivTitle = (TextView) tableRowTeamTitle.findViewById(R.id.textViewDivTitle);
                textViewDivTitle.setText(getTeamTitle(team, division));

                tableRowTeamTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (linearLayoutGames.getVisibility() == View.GONE) {
                            linearLayoutGames.setVisibility(View.VISIBLE);
                        } else {
                            linearLayoutGames.setVisibility(View.GONE);
                        }
                    }
                });

                linearLayoutTeams.addView(tableRowTeamTitle);

                // Iterate through all games
                for (Game g : MainActivity.games) {
                    String gameIDString = String.valueOf(g.getGameID());
                    // Check if game is in division and is team being displayed
                    if (gameIDString.endsWith(division) &&
                            (gameIDString.substring(8, 10).equals(team) || gameIDString.substring(10, 12).equals(team))) {
                        View tableRow = getLayoutInflater().inflate(R.layout.result_tablerow, linearLayoutTeams, false);
                        // Set gameID. This is used when the row is clicked to retrieve
                        // all the game info from the database.
                        final TextView textViewGameID = (TextView) tableRow.findViewById(R.id.textViewGameID);
                        textViewGameID.setText(String.valueOf(g.getGameID()));
                        // Set ref name
                        if (!g.getRef().equals("")) {
                            TextView textViewRef = (TextView) tableRow.findViewById(R.id.textViewRef);
                            textViewRef.setText("Ref: " + g.getRef());
                        }
                        if (!g.getAssRef1().equals("")) {
                            // Set assistant ref 1 name
                            TextView textViewAssRef1 = (TextView) tableRow.findViewById(R.id.textViewAssRef1);
                            textViewAssRef1.setText("Assistant Refs: " + g.getAssRef1());
                        }
                        if (!g.getAssRef2().equals("")) {
                            // Set assistant ref 2 name
                            TextView textViewAssRef2 = (TextView) tableRow.findViewById(R.id.textViewAssRef2);
                            textViewAssRef2.setText(" and " + g.getAssRef2());
                        }
                        // Set home team name
                        TextView textViewHomeName = (TextView) tableRow.findViewById(R.id.textViewHomeName);
                        textViewHomeName.setText(g.getHomeTeamName());
                        // Set home team score
                        TextView textViewHomeScore = (TextView) tableRow.findViewById(R.id.textViewHomeScore);
                        textViewHomeScore.setText(String.valueOf(g.getHomeTeamScore()));
                        // Set away team name
                        TextView textViewAwayName = (TextView) tableRow.findViewById(R.id.textViewAwayName);
                        textViewAwayName.setText(g.getAwayTeamName());
                        // Set away team score
                        TextView textViewAwayScore = (TextView) tableRow.findViewById(R.id.textViewAwayScore);
                        textViewAwayScore.setText(String.valueOf(g.getAwayTeamScore()));
                        // Set location of game
                        TextView textViewLocation = (TextView) tableRow.findViewById(R.id.textViewLocation);
                        textViewLocation.setText(String.valueOf(g.getLocation()));
                        // If game hasn't started, set to time game starts. Set to minutes played during game.
                        // If minutesPlayed equals 40, set to "Halftime" and set to "Final" if it equals 80.
                        TextView textViewTime = (TextView) tableRow.findViewById(R.id.textViewTime);
                        if (g.getMinutesPlayed() == 0) {
                            textViewTime.setText(g.getStartTime());
                        } else if (g.getMinutesPlayed() == 40) {
                            textViewTime.setText("Halftime");
                        } else if (g.getMinutesPlayed() == 80) {
                            textViewTime.setText("Final");
                        } else {
                            textViewTime.setText(String.valueOf(g.getMinutesPlayed()) + "mins");
                        }
                        // Add clicklistener to each row which opens activity
                        // with more information about game.
                        tableRow.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyTeamsActivity.this, GameInfoActivity.class);
                                intent.putExtra("gameID", textViewGameID.getText().toString());
                                startActivity(intent);
                            }
                        });
                        // Add tablerow to tablelayout
                        linearLayoutGames.addView(tableRow);
                    }
                }
                linearLayoutTeams.addView(linearLayoutGames);
            }
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean firstTime = settings.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences prefsFile = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsFile.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();
                startActivity(new Intent(MyTeamsActivity.this, SelectTeamsActivity.class));
            }
        }
    }

    /**
     * Gets title for team
     *
     * @param team TeamID in string form
     * @param division DivisionID in string form
     * @return the title in the format "teamname (divisionname)"
     */
    private String getTeamTitle(String team, String division) {
        String title = "";
        int divisionIndex = Integer.parseInt(division);
        switch (divisionIndex) {
            case 0:
                title += MainActivity.teamsDiv1.get(Integer.parseInt(team));
                break;
            case 1:
                title += MainActivity.teamsWomen.get(Integer.parseInt(team));
                break;
            case 2:
                title += MainActivity.teamsDiv2.get(Integer.parseInt(team));
                break;
            case 3:
                title += MainActivity.teamsDiv3.get(Integer.parseInt(team));
                break;
            case 4:
                title += MainActivity.teamsColts.get(Integer.parseInt(team));
                break;
            case 5:
                title += MainActivity.teamsU18.get(Integer.parseInt(team));
                break;
            case 6:
                title += MainActivity.teamsU16.get(Integer.parseInt(team));
                break;
            case 7:
                title += MainActivity.teamsU145.get(Integer.parseInt(team));
                break;
            case 8:
                title += MainActivity.teamsU13.get(Integer.parseInt(team));
                break;
            case 9:
                title += MainActivity.teamsU115.get(Integer.parseInt(team));
                break;
            case 10:
                title += MainActivity.teamsU10.get(Integer.parseInt(team));
                break;
            case 11:
                title += MainActivity.teamsU85.get(Integer.parseInt(team));
                break;
            case 12:
                title += MainActivity.teamsU7.get(Integer.parseInt(team));
                break;
        }

        title += " (" + MainActivity.divisions.get(divisionIndex) + ")";
        return title;
    }

    /**
     * Starts the DrawFragmentActivity after the Draw/Results button is clicked
     *
     * @param view
     */
    public void goToDraw(View view) {
        startActivity(new Intent(MyTeamsActivity.this, DrawFragmentActivity.class));
    }
}
