package org.enthusia.app.enthusia;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.fragments.EnthusiaAboutFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaDepartmentHeadsFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaEventsFragment;
import org.enthusia.app.enthusia.fragments.TimerFragment.EnthusiaTimerFragment;
import org.enthusia.app.gcm.GCMIntentService;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaNavDrawerAdapter;
import org.enthusia.app.enthusia.fragments.EnthusiaCommitteeFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaIntraFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaRegisterFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaNewsFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaSponsorsFragment;
import org.enthusia.app.enthusia.fragments.TimerFragment.*;
import org.enthusia.app.enthusia.model.EnthusiaNavDrawerItem;
import org.enthusia.app.parse.LoginActivity;
import org.enthusia.app.parse.helper.NotificationDBManager;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

@SuppressWarnings({"ConstantConditions","RTLHardcoded","RTLSymmetry"})
public class EnthusiaStartActivity extends ActionBarActivity {

    private static final String TAG = "EnthusiaStartActivity";
    private int backPressedCount = 0;
    NotificationDBManager db;

    private final static int[] SOCIAL_MEDIA_DRAWABLES = {
            R.drawable.ic_fab_facebook,
            R.drawable.ic_fab_twitter,
            R.drawable.ic_fab_youtube,
            R.drawable.ic_fab_website
    };

    private DrawerLayout enthusiaSlider;
    public ActionBarDrawerToggle enthusiaToggle;
    public Fragment currentFragment;
    public ArrayList<String> title = new ArrayList<>();

    private ArrayList<FloatingActionButton> socialMediaIcons;
    private boolean socialMediaShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_start);

        db = new NotificationDBManager(this, null, null, 1);
        Log.i(TAG, "DB created");

        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enthusiaSlider = (DrawerLayout) findViewById(R.id.enthusia_start_drawer);
        ArrayList<EnthusiaNavDrawerItem> mItems = new ArrayList<>();

        for (int i=0; i < getResources().getStringArray(R.array.enthusia_nav_drawer_items).length; i++) {
            mItems.add(new EnthusiaNavDrawerItem(getResources().getStringArray(R.array.enthusia_nav_drawer_items)[i], getResources().obtainTypedArray(R.array.enthusia_nav_drawer_icons).getResourceId(i, -1)));
        }

        ((ListView) findViewById(R.id.enthusia_start_slider)).setAdapter(new EnthusiaNavDrawerAdapter(this, mItems));
        ((ListView) findViewById(R.id.enthusia_start_slider)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                displayView(position);
            }
        });

        ((TextView) findViewById(R.id.enthusia_start_user)).setText(getString(R.string.welcome) + ", " + Utils.getPrefs(getApplicationContext(), Utils.PREF_USER_NAME, String.class));

        ((Toolbar) findViewById(R.id.action_bar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentFragment != null && currentFragment instanceof EnthusiaEventsFragment && ((EnthusiaEventsFragment) currentFragment).mUnfoldableView.isUnfolded()) {
                    onBackPressed();
                } else if (currentFragment != null && currentFragment instanceof EnthusiaDepartmentHeadsFragment) {
                    onBackPressed();
                } else if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
                    enthusiaSlider.openDrawer(Gravity.LEFT);
                } else {
                    enthusiaSlider.closeDrawers();
                }
            }
        });

        enthusiaToggle = new ActionBarDrawerToggle(this, enthusiaSlider,
                   R.string.enthusia_fest_name, R.string.enthusia_events) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(title.get(title.size() - 1));
                for (int i=0; i<title.size(); i++)
                    Log.i("Beri: ", title.get(i));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle((getString(R.string.enthusia_fest_name)));
            }
        };

        enthusiaSlider.setDrawerListener(enthusiaToggle);

        setupSocialMedia();

        if (getIntent().getExtras() != null && getIntent().hasExtra("points")) {
            if (getIntent().getExtras().getBoolean("points"))
                displayView(2);
            else
                displayView(0);
        } else if (savedInstanceState == null) {
            displayView(0);
        }

        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.enthusia_start_slider));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return enthusiaToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enthusiaToggle.syncState();



        if (!((Boolean) Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_REGISTRATION_DONE, Boolean.class))) {
            startActivityForResult(new Intent(EnthusiaStartActivity.this, LoginActivity.class), 47);
        }
        /*
        else if (getAppVersion() > (Integer) Utils.getPrefs(this, Utils.PREF_APP_VERSION, Integer.class)) {
            startActivityForResult(new Intent(EnthusiaStartActivity.this, LoginActivity.class), 47);

        }
        */
        else {

            if (savedInstanceState == null)
                Utils.showInfo(EnthusiaStartActivity.this, "Welcome, " + Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_USER_NAME, String.class));
            if (!((Boolean) Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_FIRST_RUN, Boolean.class))) {
                help();
            }
        }



    }

    private int getAppVersion() {
        try {
            return getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();

        super.onNewIntent(intent);

        if (intent.getBooleanExtra("from_push", false)) {
            String message = intent.getStringExtra("message");

            if (currentFragment instanceof EnthusiaNewsFragment){
            }
        }
        GCMIntentService gcmIntentService = new GCMIntentService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 47) {
            if (resultCode == RESULT_OK) {
                ( (TextView) findViewById(R.id.enthusia_start_user)).setText("Welcome, " + Utils.getPrefs(this, Utils.PREF_USER_NAME, String.class));
                if (!((Boolean) Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_FIRST_RUN, Boolean.class)))
                    help();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        enthusiaToggle.onConfigurationChanged(newConfig);
        Crouton.clearCroutonsForActivity(this);
        Crouton.cancelAllCroutons();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
            enthusiaSlider.closeDrawers();
            return;
        }

        if (currentFragment != null && currentFragment instanceof EnthusiaEventsFragment) {
            EnthusiaEventsFragment fragment = (EnthusiaEventsFragment) currentFragment;
            if (fragment.mUnfoldableView != null && (fragment.mUnfoldableView.isUnfolded() || fragment.mUnfoldableView.isUnfolding()))
                fragment.reset();
            else if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                if(backPressedCount == 1)
                {
                    backPressedCount=0;
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Press Back again to quit.", Toast.LENGTH_SHORT).show();
                    backPressedCount++;


                    new CountDownTimer(3000, 1000) {
                        public void onFinish() {
                            backPressedCount = 0;
                            // When timer is finished
                            // Execute your code here
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();


                }

            }
            else
                popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            if(backPressedCount == 1)
            {
                backPressedCount=0;
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Press Back again to quit.", Toast.LENGTH_SHORT).show();
                backPressedCount++;

                new CountDownTimer(3000, 1000) {
                    public void onFinish() {
                        backPressedCount = 0;
                        // When timer is finished
                        // Execute your code here
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();

            }
        }
        else
            popBackStack();
    }

    @Override
    protected void onDestroy() {
        enthusiaSlider.closeDrawers();
        super.onDestroy();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (enthusiaSlider.getDrawerLockMode(GravityCompat.START) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED || enthusiaSlider.getDrawerLockMode(Gravity.LEFT) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
                enthusiaSlider.openDrawer(Gravity.LEFT);
            } else {
                enthusiaSlider.closeDrawers();
            }
        }
        return true;
    }

    private void popBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else {
            try {
                title.remove(title.size() - 1);
                getSupportActionBar().setTitle(title.get(title.size() - 1));
            } catch (IndexOutOfBoundsException ignore) {}
            super.onBackPressed();
        }
        setSelected(getPosition(title.get(title.size() - 1)));
    }

    @SuppressWarnings("ResourceType")
    @SuppressLint("RtlHardcoded")
    private void setupSocialMedia() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enthusia_start_fab);
        fab.setColorNormal(Color.WHITE);
        fab.setType(FloatingActionButton.TYPE_MINI);
        fab.setColorPressed(darkenColor(Color.WHITE));
        fab.setImageDrawable(getResources().getDrawable(R.drawable.enthu_fab_ring));
        fab.setOnClickListener(fabClick);

        socialMediaIcons = new ArrayList<>();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( (int) (72 *  getResources().getDisplayMetrics().scaledDensity),
                (int) (72 * getResources().getDisplayMetrics().scaledDensity));
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.bottomMargin = (int) (16 * getResources().getDisplayMetrics().scaledDensity);
        params.rightMargin = (int) (16 * getResources().getDisplayMetrics().scaledDensity);

        for (int i=0; i < SOCIAL_MEDIA_DRAWABLES.length; i++) {
            FloatingActionButton button = new FloatingActionButton(EnthusiaStartActivity.this);
            button.setImageDrawable(getResources().getDrawable(SOCIAL_MEDIA_DRAWABLES[i]));
            button.setType(FloatingActionButton.TYPE_MINI);
            button.setVisibility(View.GONE);
            button.setColorNormal(Color.WHITE);
            button.setColorPressed(darkenColor(Color.WHITE));
            button.setId(i);
            button.setLayoutParams(params);
            button.setOnClickListener(fabItemClick);

            ((FrameLayout) findViewById(R.id.enthusia_start_fab_container)).addView(button);
            socialMediaIcons.add(button);
        }

    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    private View.OnClickListener fabClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            reveal();
        }
    };

    private void reveal() {
        AnimatorSet reveal = new AnimatorSet();
        reveal.setDuration(500);
        reveal.setInterpolator(new OvershootInterpolator(1.0f));
        reveal.addListener(getListener());
        for (int i=0; i < socialMediaIcons.size(); i++)
            reveal.play(getAnimation(socialMediaIcons.get(i), i+1));
        reveal.play(getFABAnimation());
        reveal.start();
    }

    private Animator getFABAnimation() {
        Animator animator = ObjectAnimator.ofFloat(findViewById(R.id.enthusia_start_fab), View.ROTATION, 0, 360);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.enthusia_start_fab).setOnClickListener(null);
                if (socialMediaShown)
                    ( (FloatingActionButton) findViewById(R.id.enthusia_start_fab)).setImageDrawable(getResources().getDrawable(R.drawable.enthu_fab_ring));
                else
                    ( (FloatingActionButton) findViewById(R.id.enthusia_start_fab)).setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                findViewById(R.id.enthusia_start_fab).setOnClickListener(fabClick);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500);
        return animator;
    }

    private Animator getAnimation(FloatingActionButton button, int i) {
        AnimatorSet set = new AnimatorSet();
        if (socialMediaShown) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                set.play(ObjectAnimator.ofFloat(button, View.Y, findViewById(R.id.enthusia_start_fab).getY()));
            else
                set.play(ObjectAnimator.ofFloat(button, View.X, findViewById(R.id.enthusia_start_fab).getX()));
            set.play(ObjectAnimator.ofFloat(button, View.ROTATION, 0, 360.0f));
            set.play(ObjectAnimator.ofFloat(button, View.ALPHA, 1.0f, 0.0f));

        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                set.play(ObjectAnimator.ofFloat(button, View.TRANSLATION_Y, -button.getLayoutParams().height * i));
            else
                set.play(ObjectAnimator.ofFloat(button, View.TRANSLATION_X, -button.getLayoutParams().height * i));
            set.play(ObjectAnimator.ofFloat(button, View.ROTATION, 0, 360.0f));
            set.play(ObjectAnimator.ofFloat(button, View.ALPHA, 0.0f, 1.0f));
        }
        return set;

    }

    private Animator.AnimatorListener getListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (FloatingActionButton button : socialMediaIcons) {
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(null);
                }

                findViewById(R.id.enthusia_start_fab).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (FloatingActionButton button : socialMediaIcons)
                    button.setOnClickListener(fabItemClick);

                if (socialMediaShown) {
                    for (FloatingActionButton button : socialMediaIcons)
                        button.setVisibility(View.GONE);
                }

                socialMediaShown = !socialMediaShown;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private View.OnClickListener fabItemClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case 0:
                    try {
                        getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/158407949426"));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/enthusia.vjti"));
                    }
                    break;

                case 1:
                    try {
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=192904564"));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ENTHUSIAVJTI"));
                    }
                    break;

                case 2:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/channel/UCKhaVXEDjLjt5YAqhL19yLA?feature=watch"));
                    break;

                case 3:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.enthusia.org/"));
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        }
    };

    private void displayView (int position) {
        if (title.size() > 0 && getPosition(title.get(title.size() - 1)) == position) {
            Log.i("Test: ", "returning");
            Log.i("getPosition", String.valueOf(getPosition(title.get(title.size() - 1))));
            Log.i("position: ", String.valueOf(position));
            Log.i("Test: ", "returned");
            return;
        }
        Log.i("Test: ", "not returned");
        Crouton.cancelAllCroutons();
        currentFragment = null;
        switch (position) {
            case 0:
                getSupportActionBar().setTitle((getString(R.string.enthusia_fest_name)));
                currentFragment = new EnthusiaNewsFragment();
                break;
            case 1:
                getSupportActionBar().setTitle((R.string.enthusia_events));
                currentFragment = new EnthusiaEventsFragment();
                break;
            case 2:
                getSupportActionBar().setTitle((getString(R.string.enthusia_intra)));
                currentFragment = new EnthusiaIntraFragment();
                break;
            case 3:
                getSupportActionBar().setTitle((getString(R.string.enthusia_register)));
                currentFragment = new EnthusiaRegisterFragment();
                break;

                /*
                Intent i = new Intent(this, EventRegistrationActivity.class);
                i.putExtra("Event", 0);
                startActivity(i);
                */
            case 4:
                getSupportActionBar().setTitle((getString(R.string.enthusia_sponsors)));
                currentFragment = new EnthusiaSponsorsFragment();
                break;
            case 5:
                getSupportActionBar().setTitle((getString(R.string.enthusia_timer)));
                currentFragment = new EnthusiaTimerFragment();
                break;
            case 6:
                getSupportActionBar().setTitle((getString(R.string.enthusia_committee)));
                currentFragment = new EnthusiaCommitteeFragment();
                break;
            case 7:
                getSupportActionBar().setTitle((getString(R.string.enthusia_about)));
                currentFragment = new EnthusiaAboutFragment();
                break;
        }
        title.add(getSupportActionBar().getTitle().toString());
        setSelected(position);
        enthusiaSlider.closeDrawers();

        if (currentFragment != null) {
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit,
                                                         R.anim.fragment_enter, R.anim.fragment_exit)
                                    .replace(R.id.enthusia_start_fragment_container, currentFragment)
                                    .addToBackStack(title.get(title.size() - 1))
                                    .commit();
                        } catch (Exception ignore) {}
                    }
                }, 250);
            } catch (Exception ignore) {}
        }
    }

    public void lockDrawer(boolean lock) {
        if (lock)
            enthusiaSlider.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else
            enthusiaSlider.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    private void setSelected(int id) {
        for (int i=0; i < ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getCount(); i++) {
            ( (EnthusiaNavDrawerItem) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getItem(i)).setSelected(id == i);
        }
        ( (EnthusiaNavDrawerAdapter) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter()).notifyDataSetChanged();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragment", getFragment());
        outState.putSerializable("title", title);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        title = (ArrayList<String>) savedInstanceState.getSerializable("title");
        setSelected(getPosition(title.get(title.size() - 1)));
        getSupportActionBar().setTitle(title.get(title.size() - 1));
    }

    private int getFragment() {
        if (currentFragment != null) {
            if (currentFragment instanceof EnthusiaNewsFragment)
                return 0;
            else if (currentFragment instanceof EnthusiaEventsFragment)
                return 1;
            else if (currentFragment instanceof EnthusiaIntraFragment)
                return 2;
            else if (currentFragment instanceof EnthusiaSponsorsFragment)
                return 4;
            else if (currentFragment instanceof EnthusiaCommitteeFragment)
                return 6;
            else if (currentFragment instanceof EnthusiaAboutFragment)
                return 7;
            else if(currentFragment instanceof  EnthusiaTimerFragment)
                return 5;
            else if(currentFragment instanceof EnthusiaRegisterFragment)
                return 3;
        }
        return 0;
    }

    private void help() {
        findViewById(R.id.enthusia_start_fab).performClick();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.enthusia_start_fab).performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enthusiaSlider.openDrawer(Gravity.LEFT);
                        Utils.putPrefs(getApplicationContext(), Utils.PREF_FIRST_RUN, true);
                    }
                }, 250);
            }
        }, 2000);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public int getPosition(String title) {
        if (title.equals("Enthusia"))
            return 0;
        if (title.equals("Intra") || title.equals("Department Heads"))
            return 2;
        if (title.equals("Register"))
            return 3;
        if (title.equals("Sponsors"))
            return 4;
        if (title.equals("Committee"))
            return 6;
        if (title.equals("About Us"))
            return 7;
        if(title.equals("Count Down"))
            return 5;
        return 1;
    }



    public void ShowAlert(String title, String message) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });
        alertDialog.setIcon(R.drawable.abc_dialog_material_background_dark);
        alertDialog.show();
    }
}
