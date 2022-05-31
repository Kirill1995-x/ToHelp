package com.tohelp.tohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tohelp.tohelp.dialogs.DialogRegisterCheck;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.prepare.Profile;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterCheck extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.tilNumberCheckSurname)
    TextInputLayout tilNumberCheckSurname;
    @BindView(R.id.tilNumberCheckName)
    TextInputLayout tilNumberCheckName;
    @BindView(R.id.tilNumberCheckMiddlename)
    TextInputLayout tilNumberCheckMiddlename;
    @BindView(R.id.tilNumberCheck)
    TextInputLayout tilNumberCheck;
    @BindView (R.id.tietNumberCheckSurname)
    TextInputEditText tietNumberCheckSurname;
    @BindView(R.id.tietNumberCheckName)
    TextInputEditText tietNumberCheckName;
    @BindView(R.id.tietNumberCheckMiddlename)
    TextInputEditText tietNumberCheckMiddlename;
    @BindView(R.id.tietNumberCheck)
    TextInputEditText tietNumberCheck;
    @BindView(R.id.btnCheckNumber)
    Button btnCheckNumber;
    @BindView(R.id.progressBarRegisterCheck)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_check);

        //настройка ButterKnife
        ButterKnife.bind(this);

        btnCheckNumber.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCheckNumber:
                confirmInput();
                break;
            default:
                break;
        }
    }

    private void confirmInput()
    {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnCheckNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("code",
                         getResources().getString(R.string.error_connection),
                         getResources().getString(R.string.check_connection));
        }
        else
        {
            Profile profile = new Profile(tilNumberCheckSurname, tilNumberCheckName, tilNumberCheckMiddlename, tilNumberCheck);
            if(profile.CheckFieldsRegisterCheck())
            {
                displayAlert("code",
                             getResources().getString(R.string.something_went_wrong),
                             getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.compare_information_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try
                                {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    progressBar.setVisibility(View.GONE);
                                    if(jsonObject.getString("code").equals("success"))
                                    {
                                        finish();
                                        Intent intent = new Intent(RegisterCheck.this, Register.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        displayAlert("code",
                                                     jsonObject.getString("title"),
                                                     jsonObject.getString("message"));
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("surname", tilNumberCheckSurname.getEditText().getText().toString().trim());
                        params.put("name", tilNumberCheckName.getEditText().getText().toString().trim());
                        params.put("middlename", tilNumberCheckMiddlename.getEditText().getText().toString().trim());
                        params.put("check_number", tilNumberCheck.getEditText().getText().toString().trim());
                        return params;
                    }
                };
                MySingleton.getInstance(RegisterCheck.this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("surname", tietNumberCheckSurname.getText().toString().trim());
        outState.putString("name", tietNumberCheckName.getText().toString().trim());
        outState.putString("middlename", tietNumberCheckMiddlename.getText().toString().trim());
        outState.putString("check_number", tietNumberCheck.getText().toString().trim());
        outState.putBoolean("showProgressBar", progressBar.isShown());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tietNumberCheckSurname.setText(savedInstanceState.getString("surname"));
        tietNumberCheckName.setText(savedInstanceState.getString("name"));
        tietNumberCheckMiddlename.setText(savedInstanceState.getString("middlename"));
        tietNumberCheck.setText(savedInstanceState.getString("check_number"));
    }

    @Override
    public void onBackPressed() {

        if(!Objects.requireNonNull(tietNumberCheckSurname).getText().toString().trim().equals("") ||
           !Objects.requireNonNull(tietNumberCheckName).getText().toString().trim().equals("") ||
           !Objects.requireNonNull(tietNumberCheckMiddlename).getText().toString().trim().equals("") ||
           !Objects.requireNonNull(tietNumberCheck).getText().toString().trim().equals(""))
        {
            DialogRegisterCheck dialogRegisterCheck=new DialogRegisterCheck();
            dialogRegisterCheck.setCancelable(false);
            dialogRegisterCheck.show(getSupportFragmentManager(), "register_check_dialog");
        }
        else
           super.onBackPressed();
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        DialogSimple dialog=new DialogSimple();
        DialogSimple.title=title;
        DialogSimple.message=message;
        dialog.show(getSupportFragmentManager(), "register_check_dialog");
    }
}