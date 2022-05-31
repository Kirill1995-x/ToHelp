package com.tohelp.tohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.resume.CheckResume;
import com.tohelp.tohelp.resume.Languages;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLanguages extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svLanguages)
    ScrollView svLanguages;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.llShowLanguages)
    LinearLayout llShowLanguages;
    @BindView(R.id.btnAddLanguage)
    Button btnAddLanguage;
    @BindView(R.id.btnSaveLanguages)
    Button btnSaveLanguages;
    @BindView(R.id.progressBarLanguages)
    ProgressBar progressBar;
    Languages Languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_languages);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //обработка нажатия на кнопку
        btnAddLanguage.setOnClickListener(this);
        btnSaveLanguages.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svLanguages.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getLanguages();
            else
            {
                svLanguages.setVisibility(View.VISIBLE);
                viewFailedInternetConnection.setVisibility(View.GONE);
                //проверка ProgressBar
                if(savedInstanceState.getBoolean("showProgressBar")) progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAddLanguage:
                addLanguage();
                break;
            case R.id.btnSaveLanguages:
                sendLanguages();
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    if(llShowLanguages.getChildCount()>0) {
                        svLanguages.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                    }
                    else getLanguages();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(this.getCurrentFocus()!=null)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLanguages()
    {
        svLanguages.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_languages_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                Languages = new Languages(jsonObject.getString("languages"));
                                splitLanguages(Languages.getLanguages());
                            }
                            else if(code.equals("failed"))
                            {
                                displayAlert(code, jsonObject.getString("title"), jsonObject.getString("message"));
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
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
                SharedPreferences sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
                try {
                    params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id", "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestque(stringRequest);
    }

    private void sendLanguages()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSaveLanguages.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(llShowLanguages).CheckFieldsLanguages())
            {
                displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_languages_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    progressBar.setVisibility(View.GONE);
                                    displayAlert(jsonObject.getString("code"),
                                                 jsonObject.getString("title"),
                                                 jsonObject.getString("message"));
                                } catch (JSONException e) {
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
                        SharedPreferences sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
                        try {
                            params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("languages", uniteLanguages());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(Languages!=null && !uniteLanguages().equals(Languages.getLanguages()))
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "resume_edit_data_dialog");
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("showProgressBar", progressBar.isShown());
        outState.putString("unite_languages", uniteLanguages());
        outState.putParcelable("languages", Languages);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Languages = savedInstanceState.getParcelable("languages");
        String unite_languages = savedInstanceState.getString("unite_languages");
        unite_languages=(unite_languages==null)?"":unite_languages;
        splitLanguages(unite_languages);
    }

    private void addLanguage() {
        if(llShowLanguages.getChildCount()<10) {
            View view = getLayoutInflater().inflate(R.layout.pattern_language, null, false);

            view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llShowLanguages.removeView(view);
                }
            });

            llShowLanguages.addView(view);
        }
        else
        {
            displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_count_of_blocks));
        }
    }

    public String uniteLanguages()
    {
        StringBuilder languages= new StringBuilder();
        //---
        for(int i=0; i<llShowLanguages.getChildCount(); i++)
        {
            View view = llShowLanguages.getChildAt(i);
            EditText etLanguage = view.findViewById(R.id.etLanguage);
            EditText etLevelOfLanguage = view.findViewById(R.id.etLevelOfLanguage);
            String language = (etLanguage.getText().toString().trim().isEmpty())?" ":etLanguage.getText().toString().trim();
            String level_of_language = (etLevelOfLanguage.getText().toString().trim().isEmpty())?" ":etLevelOfLanguage.getText().toString().trim();
            languages.append(language).append("tag").append(level_of_language).append("block");
        }
        return languages.toString();
    }

    public void splitLanguages(String languages)
    {
        if(!languages.equals("")) {
            String[] array_of_languages = languages.split("block");
            //---
            for (int i = 0; i < array_of_languages.length; i++) {
                View view = getLayoutInflater().inflate(R.layout.pattern_language, null, false);
                EditText etLanguage = view.findViewById(R.id.etLanguage);
                EditText etLevelOfLanguage = view.findViewById(R.id.etLevelOfLanguage);

                view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llShowLanguages.removeView(view);
                    }
                });

                String[] split = array_of_languages[i].split("tag");
                String language = (split[0].equals(" ")) ? "" : split[0];
                String level = (split[1].equals(" ")) ? "" : split[1];
                etLanguage.setText(language);
                etLevelOfLanguage.setText(level);
                llShowLanguages.addView(view);
            }
        }
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        if(code.equals("resume_get_success"))
        {
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            if(Languages==null) Languages = new Languages(uniteLanguages());
            else Languages.setLanguages(uniteLanguages());
            dialog.show(getSupportFragmentManager(), "resume_dialog");
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "resume_dialog");
        }
    }
}