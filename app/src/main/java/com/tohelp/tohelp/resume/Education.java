package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.prepare.FindItemInSpinner;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Education implements Parcelable
{
    private String education;

    public Education(String education)
    {
        this.education = education;
    }

    public String getEducation() {
        return education.equals("null")?"":education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    protected Education(Parcel in) {
        education = in.readString();
    }

    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(education);
    }
}
