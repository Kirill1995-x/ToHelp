package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.R;

public class Languages implements Parcelable
{
    private String languages;

    public Languages(String languages)
    {
        this.languages = languages;
    }

    public String getLanguages() {
        return languages.equals("null")?"":languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    protected Languages(Parcel in) {
        languages = in.readString();
    }

    public static final Creator<Languages> CREATOR = new Creator<Languages>() {
        @Override
        public Languages createFromParcel(Parcel in) {
            return new Languages(in);
        }

        @Override
        public Languages[] newArray(int size) {
            return new Languages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(languages);
    }
}
