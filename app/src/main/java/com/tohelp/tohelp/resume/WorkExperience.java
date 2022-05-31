package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.prepare.FindItemInSpinner;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorkExperience implements Parcelable {
    private String work_experience;

    public WorkExperience(String work_experience)
    {
        this.work_experience = work_experience;
    }

    public String getWorkExperience() {
        return work_experience.equals("null")?"":work_experience;
    }

    public void setWorkExperience(String work_experience) {
        this.work_experience = work_experience;
    }

    protected WorkExperience(Parcel in) {
        work_experience = in.readString();
    }

    public static final Creator<WorkExperience> CREATOR = new Creator<WorkExperience>() {
        @Override
        public WorkExperience createFromParcel(Parcel in) {
            return new WorkExperience(in);
        }

        @Override
        public WorkExperience[] newArray(int size) {
            return new WorkExperience[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(work_experience);
    }
}
