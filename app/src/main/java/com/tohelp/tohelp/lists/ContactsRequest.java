package com.tohelp.tohelp.lists;

import android.os.Parcel;
import android.os.Parcelable;

import com.tohelp.tohelp.R;
import com.tohelp.tohelp.settings.Variable;

public class ContactsRequest implements Parcelable
{
    String id_of_request;
    String type_of_request;
    String status;
    String id_of_specialist;
    String type_of_specialist;
    String surname;
    String name;
    String middlename;
    String workhours;
    String workphone;
    String text_of_request;
    String name_of_photo;


    public ContactsRequest(Parcel in)
    {
        String[] data=new String[12];
        in.readStringArray(data);
        id_of_request=data[0];
        type_of_request=data[1];
        status=data[2];
        id_of_specialist=data[3];
        type_of_specialist=data[4];
        surname=data[5];
        name=data[6];
        middlename=data[7];
        workhours=data[8];
        workphone=data[9];
        text_of_request=data[10];
        name_of_photo=data[11];
    }

    public ContactsRequest(String id_of_request, String type_of_request, String status, String id_of_specialist, String type_of_specialist,
                           String surname, String name, String middlename, String workhours, String workphone, String text_of_request, String name_of_photo)
    {
        this.id_of_request=id_of_request;
        this.type_of_request=type_of_request;
        this.status=status;
        this.id_of_specialist=id_of_specialist;
        this.type_of_specialist=type_of_specialist;
        this.surname=surname;
        this.name=name;
        this.middlename=middlename;
        this.workhours=workhours;
        this.workphone=workphone;
        this.text_of_request=text_of_request;
        this.name_of_photo=name_of_photo;
    }

    public String getFullname()
    {
        return (surname.isEmpty()&&name.isEmpty()&&middlename.isEmpty())?"Специалист не определен":(getSurname()+" "+getName()+" "+getMiddlename());
    }

    public String getSurname()
    {
        return ((!surname.isEmpty())?surname:"");
    }

    public String getName()
    {
        return ((!name.isEmpty())?name:"");
    }

    public String getMiddlename()
    {
        return ((!middlename.isEmpty())?middlename:"");
    }

    public String getIdOfRequest()
    {
        return ((!id_of_request.isEmpty())?id_of_request:"");
    }

    public String getStatus()
    {
        return ((!status.isEmpty())?status:"");
    }

    public String getIdOfSpecialist() {
        return ((!id_of_specialist.isEmpty())?id_of_specialist:"");
    }

    public String getTypeOfSpecialist()
    {
        return ((!type_of_specialist.isEmpty())?type_of_specialist:"Не определена");
    }

    public int getRequest()
    {
        switch (type_of_request)
        {
            case "1":
                return R.string.psychologist_request;
            case "2":
                return R.string.lawyer_request;
            case "3":
                return R.string.household_request;
            case "4":
                return R.string.health_request;
            case "5":
                return R.string.education_request;
            case "6":
                return R.string.finance_request;
            case "7":
                return R.string.other_request;
            default:
                return R.string.error_request;
        }
    }

    public String getWorkhours()
    {
        return ((!workhours.isEmpty())?workhours:"");
    }

    public String getWorkphone()
    {
        return ((!workphone.isEmpty())?workphone:"");
    }

    public String getTextOfRequest()
    {
        return ((!text_of_request.isEmpty())?text_of_request:"");
    }

    public String getNameOfPhoto()
    {
        return name_of_photo;
    }

    public String getUrlOfPhoto()
    {
        return (getNameOfPhoto().equals("without_photo"))?null:(Variable.photo_of_specialist_url + getIdOfSpecialist()+ "/" + getNameOfPhoto());
    }

    public static final Creator<ContactsRequest> CREATOR = new Creator<ContactsRequest>() {
        @Override
        public ContactsRequest createFromParcel(Parcel in) {
            return new ContactsRequest(in);
        }

        @Override
        public ContactsRequest[] newArray(int size) {
            return new ContactsRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeStringArray(new String[]{id_of_request, type_of_request, status, id_of_specialist, type_of_specialist, surname, name, middlename, workhours, workphone, text_of_request, name_of_photo});
    }

}
