package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.R;

public class Projects implements Parcelable {
    private String projects;

    public Projects(String projects)
    {
        this.projects = projects;
    }

    public String getProjects() {
        return projects.equals("null")?"":projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    protected Projects(Parcel in) {
        projects = in.readString();
    }

    public static final Creator<Projects> CREATOR = new Creator<Projects>() {
        @Override
        public Projects createFromParcel(Parcel in) {
            return new Projects(in);
        }

        @Override
        public Projects[] newArray(int size) {
            return new Projects[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(projects);
    }
}
