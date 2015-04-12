package chrisdalzell.rugbydraw;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SQLiteOpenHelper helps you open or create a database

public class TeamsDB extends SQLiteOpenHelper {

    // Context : provides access to application-specific resources and classes

    public TeamsDB(Context applicationcontext) {

        // Call use the database or to create it

        super(applicationcontext, "myteams.db", null, 1);

    }

    // onCreate is called the first time the database is created

    public void onCreate(SQLiteDatabase database) {

        // Create a table in SQLite

        String query = "CREATE TABLE teams (teamID TEXT PRIMARY KEY)";

        // Executes the query
        database.execSQL(query);

    }

    // onUpgrade is used to drop tables, add tables, or do anything
    // else it needs to upgrade
    // This is dropping the table to delete the data and then calling
    // onCreate to make an empty table

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query = "DROP TABLE IF EXISTS teams";

        // Executes the query provided as long as the query isn't a select
        // or if the query doesn't return any data

        database.execSQL(query);
        onCreate(database);
    }

    public void insertTeam(String teamID) {

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        // Stores key value pairs being the column name and the data
        // ContentValues data type is needed because the database
        // requires its data type to be passed

        ContentValues values = new ContentValues();

        values.put("teamID", teamID);

        // Inserts the data in the form of ContentValues into the
        // table name provided

        try {
            database.insert("teams", null, values);
        } catch (Exception e) {

        }

        // Release the reference to the SQLiteDatabase object

        database.close();
    }

    public void insertTeamArray(ArrayList<String> teams) {
        for (String team : teams) {
            insertTeam(team);
        }
    }

    // Used to delete a contact with the matching contactId

    public void deleteTeam(String id) {

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM teams where teamID='" + id + "'";

        // Executes the query provided as long as the query isn't a select
        // or if the query doesn't return any data

        database.execSQL(deleteQuery);
    }

    public void deleteAllTeams() {
        ArrayList<String> allTeams = getAllTeams();

        for (String team : allTeams) {
            deleteTeam(team);
        }
    }

    public ArrayList<String> getAllTeams() {

        // ArrayList that contains every row in the database
        // and each row key / value stored in a HashMap

        ArrayList<String> teamArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM teams";

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        // Cursor provides read and write access for the
        // data returned from a database query

        // rawQuery executes the query and returns the result as a Cursor

        Cursor cursor = database.rawQuery(selectQuery, null);

        // Move to the first row

        if (cursor.moveToFirst()) {
            do {
                teamArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext()); // Move Cursor to the next row
        }

        cursor.close();

        // return contact list
        return teamArrayList;
    }
}