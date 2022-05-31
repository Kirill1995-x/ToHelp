package com.tohelp.tohelp.prepare;

import android.content.Context;
import android.content.SharedPreferences;

import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

public class PhotoDelete
{
    private String id;
    private String name_of_photo;
    private String access_token;
    private Context context;
    private SharedPreferences sharedPreferences;

    public PhotoDelete(Context context)
    {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
        try {
            id = Encryption.decrypt(sharedPreferences.getString("shared_id", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        name_of_photo = sharedPreferences.getString("shared_name_of_photo", "");
        access_token = sharedPreferences.getString("shared_access_token","");
    }

    public String getId() {
        return id;
    }

    public String getNameOfPhoto() {
        return name_of_photo;
    }

    public String getAccessToken()
    {
        return access_token;
    }

    public void setWithoutPhoto()
    {
        sharedPreferences.edit().putString("shared_name_of_photo", "without_photo").apply();
    }
}
