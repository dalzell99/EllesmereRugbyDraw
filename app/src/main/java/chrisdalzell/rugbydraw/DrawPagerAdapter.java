package chrisdalzell.rugbydraw;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.Calendar;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages. This provides the data for the {@link android.support.v4.view.ViewPager}.
 */
public class DrawPagerAdapter extends FragmentPagerAdapter {

    public static int weekNumber = 0;

    public DrawPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Get fragment corresponding to a specific position. This will be used to populate the
     * contents of the {@link android.support.v4.view.ViewPager}.
     *
     * @param position Position to fetch fragment for.
     * @return Fragment for specified position.
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DrawDisplayFragment
        weekNumber = position;
        return new DrawDisplayFragment();
    }

    /**
     * Causes all fragments to be recreated with notifyDataSetChanged() called which
     * in turn updates the game info.
     *
     */
    @Override
    public int getItemPosition(Object o) {
        return POSITION_NONE;
    }

    /**
     * Get number of pages the {@link android.support.v4.view.ViewPager} should render.
     *
     * @return Number of fragments to be rendered as pages.
     */
    @Override
    public int getCount() {
        // Show 7 total pages.
        return 7;
    }

    /**
     * Get title for each of the pages. This will be displayed on each of the tabs.
     *
     * @param position Page to fetch title for.
     * @return Title for specified page.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Calendar calendar = DrawFragmentActivity.startFirstWeek;

        // Get date for start of week (Monday) as int in YYYYMMDD format
        calendar.add(Calendar.DAY_OF_MONTH, 7 * position);
        String startDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec").get(calendar.get(Calendar.MONTH) - 1);

        // Get date for end of week (Sunday) as int in YYYYMMDD format
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        String endDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                        "Aug", "Sep", "Oct", "Nov", "Dec").get(calendar.get(Calendar.MONTH) - 1);

        calendar.add(Calendar.DAY_OF_MONTH, -(6 + 7 * position));

        return "Week " + String.valueOf(position + 1) + " (" + startDate + " - " + endDate + ")";
    }
}