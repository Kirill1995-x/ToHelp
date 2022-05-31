package com.tohelp.tohelp.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Tests implements Parcelable
{
    private int idOfTest;
    private String nameOfTest;
    private String descriptionOfTest;
    private String linkToTest;

    public Tests(int idOfTest, String nameOfTest, String descriptionOfTest, String linkToTest) {
        this.idOfTest=idOfTest;
        this.nameOfTest = nameOfTest;
        this.descriptionOfTest = descriptionOfTest;
        this.linkToTest = linkToTest;
    }

    protected Tests(Parcel in) {
        idOfTest=in.readInt();
        nameOfTest = in.readString();
        descriptionOfTest = in.readString();
        linkToTest = in.readString();
    }

    public static final Creator<Tests> CREATOR = new Creator<Tests>() {
        @Override
        public Tests createFromParcel(Parcel in) {
            return new Tests(in);
        }

        @Override
        public Tests[] newArray(int size) {
            return new Tests[size];
        }
    };

    public int getIdOfTest()
    {
        return idOfTest;
    }

    public String getNameOfTest() {
        return nameOfTest;
    }

    public String getDescriptionOfTest() {
        return descriptionOfTest;
    }

    public String getLinkToTest() {
        return linkToTest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOfTest);
        dest.writeString(nameOfTest);
        dest.writeString(descriptionOfTest);
        dest.writeString(linkToTest);
    }
}
