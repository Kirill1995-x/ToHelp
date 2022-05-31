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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.prepare.FindItemInSpinner;
import com.tohelp.tohelp.resume.GetArrayForResume;
import com.tohelp.tohelp.resume.CheckResume;
import com.tohelp.tohelp.resume.Education;
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

public class ActivityEducation extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svEducation)
    ScrollView svEducation;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.llShowEducation)
    LinearLayout llShowEducation;
    @BindView(R.id.btnSaveEducation)
    Button btnSaveEducation;
    @BindView(R.id.btnAddEducation)
    Button btnAddEducation;
    @BindView(R.id.progressBarEducation)
    ProgressBar progressBar;
    Education Education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_education);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //обработка нажатия на кнопку
        btnSaveEducation.setOnClickListener(this);
        btnAddEducation.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svEducation.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getEducation();
            else
            {
                svEducation.setVisibility(View.VISIBLE);
                viewFailedInternetConnection.setVisibility(View.GONE);
                //проверка ProgressBar
                if(savedInstanceState.getBoolean("showProgressBar")) progressBar.setVisibility(View.VISIBLE);
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSaveEducation:
                sendEducation();
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    if(llShowEducation.getChildCount()>0) {
                        svEducation.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                    }
                    else getEducation();
                }
                break;
            case R.id.btnAddEducation:
                addEducation();
            default:
                break;
        }
    }

    private void getEducation()
    {
        svEducation.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_education_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                Education = new Education(jsonObject.getString("education"));
                                splitEducation(Education.getEducation());
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

    private void sendEducation()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSaveEducation.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(llShowEducation).CheckFieldsEducation())
            {
                displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_education_url,
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
                        params.put("education", uniteEducation());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(Education!=null && !uniteEducation().equals(Education.getEducation()))
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
        outState.putString("unite_education", uniteEducation());
        outState.putParcelable("education", Education);;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Education = savedInstanceState.getParcelable("education");
        String unite_education = savedInstanceState.getString("unite_education");
        unite_education=(unite_education==null)?"":unite_education;
        splitEducation(unite_education);
    }

    private void addEducation()
    {
        if(llShowEducation.getChildCount()<10)
        {
            View view = getLayoutInflater().inflate(R.layout.pattern_education, null, false);

            Spinner spStartOfEducation = view.findViewById(R.id.spStartOfEducation);
            Spinner spEndOfEducation = view.findViewById(R.id.spEndOfEducation);
            Spinner spLevelOfEducation = view.findViewById(R.id.spLevelOfEducationResume);
            ArrayAdapter<String> adapter_for_start_of_education = new ArrayAdapter<String>(this, R.layout.spinner_layout, new GetArrayForResume().CreateArrayForEducationYears());
            ArrayAdapter<String> adapter_for_end_of_education = new ArrayAdapter<String>(this, R.layout.spinner_layout, new GetArrayForResume().CreateArrayForEducationYears());
            ArrayAdapter adapter_for_level_of_education=ArrayAdapter.createFromResource(this, R.array.array_level_education_for_resume, R.layout.spinner_layout);
            adapter_for_start_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_end_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_level_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            spStartOfEducation.setAdapter(adapter_for_start_of_education);
            spEndOfEducation.setAdapter(adapter_for_end_of_education);
            spLevelOfEducation.setAdapter(adapter_for_level_of_education);

            view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llShowEducation.removeView(view);
                }
            });

            llShowEducation.addView(view);
        }
        else
        {
            displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_count_of_blocks));
        }
    }

    public String uniteEducation()
    {
        StringBuilder education= new StringBuilder();
        //---
        for(int i=0; i<llShowEducation.getChildCount(); i++)
        {
            View view = llShowEducation.getChildAt(i);
            EditText etNameOfEducation = view.findViewById(R.id.etNameOfEducation);
            Spinner spStartOfEducation = view.findViewById(R.id.spStartOfEducation);
            Spinner spEndOfEducation = view.findViewById(R.id.spEndOfEducation);
            Spinner spLevelOfEducation = view.findViewById(R.id.spLevelOfEducationResume);
            String name_of_education = (etNameOfEducation.getText().toString().trim().isEmpty())?" ":etNameOfEducation.getText().toString().trim();
            education.append(name_of_education).append("tag")
                    .append(spStartOfEducation.getSelectedItem().toString()).append("tag")
                    .append(spEndOfEducation.getSelectedItem().toString()).append("tag")
                    .append(spLevelOfEducation.getSelectedItem().toString()).append("block");
        }
        return education.toString();
    }

    public void splitEducation(String education)
    {
        if(!education.equals("")) {
            String[] array_of_education = education.split("block");
            //---
            for (int i = 0; i < array_of_education.length; i++) {
                View view = getLayoutInflater().inflate(R.layout.pattern_education, null, false);
                EditText etNameOfEducation = view.findViewById(R.id.etNameOfEducation);
                Spinner spStartOfEducation = view.findViewById(R.id.spStartOfEducation);
                Spinner spEndOfEducation = view.findViewById(R.id.spEndOfEducation);
                Spinner spLevelOfEducation = view.findViewById(R.id.spLevelOfEducationResume);
                ArrayAdapter<String> adapter_for_start_of_education = new ArrayAdapter<String>(this, R.layout.spinner_layout, new GetArrayForResume().CreateArrayForEducationYears());
                ArrayAdapter<String> adapter_for_end_of_education = new ArrayAdapter<String>(this, R.layout.spinner_layout, new GetArrayForResume().CreateArrayForEducationYears());
                ArrayAdapter adapter_for_level_of_education = ArrayAdapter.createFromResource(this, R.array.array_level_education_for_resume, R.layout.spinner_layout);
                adapter_for_start_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_end_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_level_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                spStartOfEducation.setAdapter(adapter_for_start_of_education);
                spEndOfEducation.setAdapter(adapter_for_end_of_education);
                spLevelOfEducation.setAdapter(adapter_for_level_of_education);

                view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llShowEducation.removeView(view);
                    }
                });

                String[] split = array_of_education[i].split("tag");
                String name_of_education = (split[0].equals(" ")) ? "" : split[0];
                etNameOfEducation.setText(name_of_education);
                spStartOfEducation.setSelection(new FindItemInSpinner("year_education", split[1], this).getPosition());
                spEndOfEducation.setSelection(new FindItemInSpinner("year_education", split[2], this).getPosition());
                spLevelOfEducation.setSelection(new FindItemInSpinner("level_of_education_for_resume", split[3], this).getPosition());
                llShowEducation.addView(view);
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
            if(Education==null) Education = new Education(uniteEducation());
            else Education.setEducation(uniteEducation());
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