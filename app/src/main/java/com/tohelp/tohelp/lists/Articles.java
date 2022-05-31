package com.tohelp.tohelp.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Articles implements Parcelable
{
    private int idOfArticle;
    private String nameOfArticle;
    private String linkToArticle;

    public Articles(int idOfArticle, String nameOfArticle, String linkToArticle) {
        this.idOfArticle = idOfArticle;
        this.nameOfArticle = nameOfArticle;
        this.linkToArticle = linkToArticle;
    }

    protected Articles(Parcel in) {
        idOfArticle = in.readInt();
        nameOfArticle = in.readString();
        linkToArticle = in.readString();
    }

    public static final Creator<Articles> CREATOR = new Creator<Articles>() {
        @Override
        public Articles createFromParcel(Parcel in) {
            return new Articles(in);
        }

        @Override
        public Articles[] newArray(int size) {
            return new Articles[size];
        }
    };

    public int getIdOfArticle() {
        return idOfArticle;
    }

    public String getNameOfArticle() {
        return nameOfArticle;
    }

    public String getLinkToArticle()
    {
        return linkToArticle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOfArticle);
        dest.writeString(nameOfArticle);
        dest.writeString(linkToArticle);
    }
}
