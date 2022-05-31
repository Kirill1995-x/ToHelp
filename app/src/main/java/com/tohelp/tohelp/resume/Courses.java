package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.R;

public class Courses implements Parcelable
{
    private String courses;

    public Courses(String courses)
    {
        this.courses = courses;
    }

    protected Courses(Parcel in) {
        courses = in.readString();
    }

    public static final Creator<Courses> CREATOR = new Creator<Courses>() {
        @Override
        public Courses createFromParcel(Parcel in) {
            return new Courses(in);
        }

        @Override
        public Courses[] newArray(int size) {
            return new Courses[size];
        }
    };

    public String getCourses() {
        return (courses.equals("null"))?"":courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courses);
    }
}
