package chrisdalzell.rugbydraw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SelectTeamsActivity extends Activity {

    TeamsDB teamsDB = new TeamsDB(this);
    LinearLayout linearLayoutAllTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teams);

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbarSelectTeams);
        TextView textViewToolbarTitle = (TextView) toolbar.findViewById(R.id.textViewToolbarTitle);
        textViewToolbarTitle.setText("Select Teams");

        // Get users teams which are stored in sqlite3 database
        ArrayList<String> teamList =  teamsDB.getAllTeams();

        linearLayoutAllTeams = (LinearLayout) findViewById(R.id.linearLayoutAllTeams);

        for (int i = 0; i < MainActivity.divisions.size(); i += 1) {
            final LinearLayout linearLayoutTeams = new LinearLayout(this);
            linearLayoutTeams.setOrientation(LinearLayout.VERTICAL);
            linearLayoutTeams.setVisibility(View.GONE);

            // Create title for team
            View tableRowTeamTitle = getLayoutInflater().inflate(R.layout.result_division_title, linearLayoutAllTeams, false);
            TextView textViewDivTitle = (TextView) tableRowTeamTitle.findViewById(R.id.textViewDivTitle);
            textViewDivTitle.setText(MainActivity.divisions.get(i));

            tableRowTeamTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayoutTeams.getVisibility() == View.GONE) {
                        linearLayoutTeams.setVisibility(View.VISIBLE);
                    } else {
                        linearLayoutTeams.setVisibility(View.GONE);
                    }
                }
            });

            linearLayoutAllTeams.addView(tableRowTeamTitle);

            List<String> teams = new ArrayList<>();
            switch (i) {
                case 0:
                    teams = MainActivity.teamsDiv1;
                    break;
                case 1:
                    teams = MainActivity.teamsWomen;
                    break;
                case 2:
                    teams = MainActivity.teamsDiv2;
                    break;
                case 3:
                    teams = MainActivity.teamsDiv3;
                    break;
                case 4:
                    teams = MainActivity.teamsColts;
                    break;
                case 5:
                    teams = MainActivity.teamsU18;
                    break;
                case 6:
                    teams = MainActivity.teamsU16;
                    break;
                case 7:
                    teams = MainActivity.teamsU145;
                    break;
                case 8:
                    teams = MainActivity.teamsU13;
                    break;
                case 9:
                    teams = MainActivity.teamsU115;
                    break;
                case 10:
                    teams = MainActivity.teamsU10;
                    break;
                case 11:
                    teams = MainActivity.teamsU85;
                    break;
                case 12:
                    teams = MainActivity.teamsU7;
                    break;
            }

            for (int j = 0; j < teams.size(); j += 1) {
                final View tableRowTeam = getLayoutInflater().inflate(R.layout.team_tablerow, linearLayoutAllTeams, false);
                TextView textViewTeamName = (TextView) tableRowTeam.findViewById(R.id.textViewTeamName);
                textViewTeamName.setText(teams.get(j));
                TextView textViewDivisionID = (TextView) tableRowTeam.findViewById(R.id.textViewDivisionID);
                textViewDivisionID.setText(String.valueOf(i));
                TextView textViewTeamID = (TextView) tableRowTeam.findViewById(R.id.textViewTeamID);
                textViewTeamID.setText(String.valueOf(j));
                // Set selected image by iterating through previously selected teams
                for (String team : teamList) {
                    int teamID = Integer.parseInt(team.substring(0,2));
                    int divisionID = Integer.parseInt(team.substring(2,4));
                    if (divisionID == i && teamID == j) {
                        ImageView imageView = (ImageView) tableRowTeam.findViewById(R.id.imageViewSelected);
                        imageView.setImageResource(R.drawable.tick);
                        TextView textViewSelected = (TextView) tableRowTeam.findViewById(R.id.textViewSelected);
                        textViewSelected.setText("Selected");
                    }
                }
                tableRowTeam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textViewSelected = (TextView) tableRowTeam.findViewById(R.id.textViewSelected);
                        ImageView imageViewSelected = (ImageView) tableRowTeam.findViewById(R.id.imageViewSelected);
                        if (textViewSelected.getText().equals("Unselected")) {
                            imageViewSelected.setImageResource(R.drawable.tick);
                            textViewSelected.setText("Selected");
                        } else {
                            imageViewSelected.setImageResource(R.drawable.cross);
                            textViewSelected.setText("Unselected");
                        }
                    }
                });

                linearLayoutTeams.addView(tableRowTeam);
            }
            linearLayoutAllTeams.addView(linearLayoutTeams);
        }
    }

    public void save(View view) {
        teamsDB.deleteAllTeams();
        ArrayList<String> selectedTeamIDs = new ArrayList<>();
        // Go through the child of linearLayoutAllTeams and check if the child is a
        // title (has 3 children) or a linear layout with the teams in it.
        for (int i = 0; i < linearLayoutAllTeams.getChildCount(); i += 1) {
            LinearLayout child = (LinearLayout) linearLayoutAllTeams.getChildAt(i);
            if (child.getChildCount() != 3) {
                // If the child is a team linear layout, go through each of its children and
                // check it was selected
                for (int j = 0; j < child.getChildCount(); j += 1) {
                    LinearLayout team = ((LinearLayout)((LinearLayout) child.getChildAt(j)).getChildAt(1));
                    TextView selected = (TextView) team.getChildAt(3);
                    if (selected.getText().equals("Selected")) {
                        // If it is selected, then concatenate the teamID and divisionID and add to
                        // arraylist of selected teams
                        String teamID = ((TextView) team.getChildAt(2)).getText().toString();
                        String divisionID = ((TextView) team.getChildAt(1)).getText().toString();
                        selectedTeamIDs.add(pad(Integer.parseInt(teamID)) + pad(Integer.parseInt(divisionID)));
                    }
                }
            }
        }
        teamsDB.insertTeamArray(selectedTeamIDs);
        startActivity(new Intent(SelectTeamsActivity.this, MyTeamsActivity.class));
    }

    // Pads single digit ints with a leading zero to keep 2 character length
    private String pad(int c) {
        return c >= 10 ? String.valueOf(c) : "0" + String.valueOf(c);
    }
}
