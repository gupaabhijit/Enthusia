package org.enthusia.app.enthusia.fragments.TimerFragment;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.fragments.EnthusiaDepartmentHeadsFragment;
import org.enthusia.app.enthusia.fragments.dialog.EnthusiaPointsTableDialog;

/**
 * Created by Abhijit on 23-11-2015.
 */
public class EnthusiaTimerFragment extends Fragment
{
    private static final String TAG = "CountdownTimer";

    private TextView mCountdownNote;
    private ProgressWheel mDaysWheel;
    private TextView mDaysLabel;
    private ProgressWheel mHoursWheel;
    private TextView mHoursLabel;
    private ProgressWheel mMinutesWheel;
    private TextView mMinutesLabel;
    private ProgressWheel mSecondsWheel;
    private TextView mSecondsLabel;


    private TextView mCountdownNoteMara;
    private ProgressWheel mDaysWheelMara;
    private ProgressWheel mHoursWheelMara;
    private ProgressWheel mMinutesWheelMara;
    private ProgressWheel mSecondsWheelMara;


    // Timer setup for cyclothon
    Time conferenceTime = new Time(Time.getCurrentTimezone());
    int hour = 11;
    int minute = 28;
    int second = 0;
    int monthDay = 20;
    // month is zero based...7 == August
    int month = 11;
    int year;


    //Timer setup for marathon
    Time conferenceTimeMara = new Time(Time.getCurrentTimezone());
    int hourMara = 11;
    int minuteMara = 28;
    int secondMara = 0;
    int monthDayMara = 13;
    // month is zero based...7 == August
    int monthMara = 11;
    int yearMara;


    // Values displayed by the timer
    private int mDisplayDays;
    private int mDisplayHours;
    private int mDisplayMinutes;
    private int mDisplaySeconds;

    // Values displayed by the timer
    private int mDisplayDaysMara;
    private int mDisplayHoursMara;
    private int mDisplayMinutesMara;
    private int mDisplaySecondsMara;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_cyclothon_timer, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        configureViews();
        configureConferenceDate();
        configureConferenceDateMarathon();
    }

    private void configureViews() {

        this.conferenceTime.setToNow();
        this.year = conferenceTime.year;

        this.conferenceTimeMara.setToNow();
        this.yearMara=conferenceTimeMara.year;

        //cycthon
        //this.mCountdownNote = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_note);
        this.mDaysWheel = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_days);
        this.mHoursWheel = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_hours);
        this.mMinutesWheel = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_minutes);
        this.mSecondsWheel = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_seconds);

        //marathon
        //this.mCountdownNoteMara = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_note_marathon);
        this.mDaysWheelMara = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_days_mara);
        this.mHoursWheelMara = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_hours_mara);
        this.mMinutesWheelMara = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_minutes_mara);
        this.mSecondsWheelMara = (ProgressWheel) getActivity().findViewById(R.id.activity_countdown_timer_seconds_mara);


        //this.mDaysLabel = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_days_text);
        this.mHoursLabel = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_hours_text);
        this.mMinutesLabel = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_minutes_text);
        this.mSecondsLabel = (TextView) getActivity().findViewById(R.id.activity_countdown_timer_seconds_text);

    }
    private void configureConferenceDateMarathon() {
        conferenceTimeMara.set(secondMara, minuteMara, hourMara, monthDayMara, monthMara, yearMara);
        conferenceTimeMara.normalize(true);
        long confMillis = conferenceTimeMara.toMillis(true);

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;

        new CountDownTimer(milliDiff, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // decompose difference into days, hours, minutes and seconds
                EnthusiaTimerFragment.this.mDisplayDaysMara = (int) ((millisUntilFinished / 1000) / 86400);
                EnthusiaTimerFragment.this.mDisplayHoursMara = (int) (((millisUntilFinished / 1000) - (EnthusiaTimerFragment.this.mDisplayDaysMara * 86400)) / 3600);
                EnthusiaTimerFragment.this.mDisplayMinutesMara = (int) (((millisUntilFinished / 1000) - ((EnthusiaTimerFragment.this.mDisplayDaysMara * 86400) + (EnthusiaTimerFragment.this.mDisplayHoursMara * 3600))) / 60);
                EnthusiaTimerFragment.this.mDisplaySecondsMara = (int) ((millisUntilFinished / 1000) % 60);

                EnthusiaTimerFragment.this.mDaysWheelMara.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayDaysMara));
                EnthusiaTimerFragment.this.mDaysWheelMara.setProgress(EnthusiaTimerFragment.this.mDisplayDaysMara);

                EnthusiaTimerFragment.this.mHoursWheelMara.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayHoursMara));
                EnthusiaTimerFragment.this.mHoursWheelMara.setProgress(EnthusiaTimerFragment.this.mDisplayHoursMara * 15);

                EnthusiaTimerFragment.this.mMinutesWheelMara.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayMinutesMara));
                EnthusiaTimerFragment.this.mMinutesWheelMara.setProgress(EnthusiaTimerFragment.this.mDisplayMinutesMara * 6);

                Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
                an.setFillAfter(true);

                EnthusiaTimerFragment.this.mSecondsWheelMara.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplaySecondsMara));
                EnthusiaTimerFragment.this.mSecondsWheelMara.setProgress(EnthusiaTimerFragment.this.mDisplaySecondsMara * 6);
            }

            @Override
            public void onFinish() {
                //TODO: this is where you would launch a subsequent activity if you'd like.  I'm currently just setting the seconds to zero
                Logger.d(TAG, "Timer Finished...");
                EnthusiaTimerFragment.this.mSecondsWheelMara.setText("0");
                EnthusiaTimerFragment.this.mSecondsWheelMara.setProgress(0);

                EnthusiaTimerFragment.this.mMinutesWheelMara.setText("0");
                EnthusiaTimerFragment.this.mMinutesWheelMara.setProgress(0);

                EnthusiaTimerFragment.this.mHoursWheelMara.setText("0");
                EnthusiaTimerFragment.this.mHoursWheelMara.setProgress(0);


                EnthusiaTimerFragment.this.mDaysWheelMara.setText("0");
                EnthusiaTimerFragment.this.mDaysWheelMara.setProgress(0);
            }
        }.start();
    }

   
    private void configureConferenceDate() {
        conferenceTime.set(second, minute, hour, monthDay, month, year);
        conferenceTime.normalize(true);
        long confMillis = conferenceTime.toMillis(true);

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;

        new CountDownTimer(milliDiff, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // decompose difference into days, hours, minutes and seconds
                EnthusiaTimerFragment.this.mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
                EnthusiaTimerFragment.this.mDisplayHours = (int) (((millisUntilFinished / 1000) - (EnthusiaTimerFragment.this.mDisplayDays * 86400)) / 3600);
                EnthusiaTimerFragment.this.mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((EnthusiaTimerFragment.this.mDisplayDays * 86400) + (EnthusiaTimerFragment.this.mDisplayHours * 3600))) / 60);
                EnthusiaTimerFragment.this.mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);

                EnthusiaTimerFragment.this.mDaysWheel.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayDays));
                EnthusiaTimerFragment.this.mDaysWheel.setProgress(EnthusiaTimerFragment.this.mDisplayDays);

                EnthusiaTimerFragment.this.mHoursWheel.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayHours));
                EnthusiaTimerFragment.this.mHoursWheel.setProgress(EnthusiaTimerFragment.this.mDisplayHours * 15);

                EnthusiaTimerFragment.this.mMinutesWheel.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplayMinutes));
                EnthusiaTimerFragment.this.mMinutesWheel.setProgress(EnthusiaTimerFragment.this.mDisplayMinutes * 6);

                Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
                an.setFillAfter(true);

                EnthusiaTimerFragment.this.mSecondsWheel.setText(String.valueOf(EnthusiaTimerFragment.this.mDisplaySeconds));
                EnthusiaTimerFragment.this.mSecondsWheel.setProgress(EnthusiaTimerFragment.this.mDisplaySeconds * 6);
            }

            @Override
            public void onFinish() {
                //TODO: this is where you would launch a subsequent activity if you'd like.  I'm currently just setting the seconds to zero
                Logger.d(TAG, "Timer Finished...");
                EnthusiaTimerFragment.this.mSecondsWheel.setText("0");
                EnthusiaTimerFragment.this.mSecondsWheel.setProgress(0);
            }
        }.start();
    }
}
