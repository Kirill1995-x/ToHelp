package com.tohelp.tohelp.lists;

import android.os.Parcel;
import android.os.Parcelable;

import com.tohelp.tohelp.settings.Variable;

public class Contacts implements Parcelable
{
    private String id_of_specialist, type_of_specialist, surname, name, middlename, call_hours, mobile, name_of_photo;

    public Contacts(String id_of_specialist, String type_of_specialist, String surname, String name, String middlename, String call_hours, String mobile, String name_of_photo) {
        this.id_of_specialist=id_of_specialist;
        this.type_of_specialist=type_of_specialist;
        this.surname=surname;
        this.name = name;
        this.middlename=middlename;
        this.call_hours = call_hours;
        this.mobile = mobile;
        this.name_of_photo = name_of_photo;
    }

    public Contacts(Parcel in) {
        String[] data=new String[8];
        in.readStringArray(data);
        id_of_specialist=data[0];
        type_of_specialist=data[1];
        surname=data[2];
        name=data[3];
        middlename=data[4];
        call_hours=data[5];
        mobile=data[6];
        name_of_photo=data[7];
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{id_of_specialist, type_of_specialist, surname, name, middlename, call_hours, mobile, name_of_photo});
    }

    public String getIdOfSpecialist()
    {
        return ((!id_of_specialist.isEmpty())?id_of_specialist:"");
    }

    public String getTypeOfSpecialist()
    {
        return ((!type_of_specialist.isEmpty())?type_of_specialist:"Не определена");
    }

    public String getSurname()
    {
        return ((!surname.isEmpty())?surname:"");
    }

    public String getName() {
        return ((!name.isEmpty())?name:"");
    }

    public String getMiddlename()
    {
        return ((!middlename.isEmpty())?middlename:"");
    }

    public String getFullName()
    {
        return getSurname()+" "+getName()+" "+getMiddlename();
    }

    public String getCallHours() {
        return ((!call_hours.isEmpty())?call_hours:"");
    }

    public String getMobile() {
        return ((!mobile.isEmpty())?mobile:"");
    }

    public String getNameOfPhoto() {
        return name_of_photo;
    }

    public String getUrlOfPhoto()
    {
        return ((getNameOfPhoto().equals("without_photo"))?null:Variable.photo_of_specialist_url + getIdOfSpecialist() + "/"+getNameOfPhoto());
    }
}
