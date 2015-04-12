package chrisdalzell.rugbydraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DrawFragmentActivity extends FragmentActivity {

    /**
     * Sets the date for the start of the season. This is used for displaying
     * games by week.
     */
    public static final Calendar startFirstWeek = new GregorianCalendar(2015, 3, 23);

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static DrawPagerAdapter mDrawPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    public static SlidingTabLayout mSlidingTabLayout;

    /**
     * Create the activity. Sets up an {@link android.app.ActionBar} with tabs, and then configures the
     * {@link android.support.v4.view.ViewPager} contained inside R.layout.activity_draw_display.
     *
     * <p>A {@link DrawPagerAdapter} will be instantiated to hold the different pages of
     * fragments that are to be displayed. A
     * {@link android.support.v4.view.ViewPager.SimpleOnPageChangeListener} will also be configured
     * to receive callbacks when the user swipes between pages in the ViewPager.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_display);

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar);
        TextView textViewToolbarTitle = (TextView) toolbar.findViewById(R.id.textViewToolbarTitle);
        textViewToolbarTitle.setText("Draw/Results");

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mDrawPagerAdapter = new DrawPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDrawPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // If this activity is created because of a swipe to refresh by the user, set weeks to
        // the week the swipe to refresh was called, otherwise set weeks to the current week
        int weeks = getIntent().getIntExtra("weekNumber", -1);
        if (weeks == -1) {
            // Get todays date, add 1 month todays date (Gregorian calendars months are not zero indexed),
            // calculate difference in milliseconds and convert to weeks and set viewpager to that index.
            Calendar today = Calendar.getInstance();
            today.set(Calendar.MONTH, today.get(Calendar.MONTH) + 1);
            long c = today.getTimeInMillis() - startFirstWeek.getTimeInMillis();
            weeks = (int) c / (1000 * 60 * 60 * 24 * 7);
        }
        mViewPager.setCurrentItem(weeks, true);
    }

    /**
     * Starts the My Teams activity
     *
     * @param view
     */
    public void goToMyTeams(View view) {
        startActivity(new Intent(DrawFragmentActivity.this, MyTeamsActivity.class));
    }

    /**
     * Stop user going back to splash screen
     */
    @Override
    public void onBackPressed() {
    }
}