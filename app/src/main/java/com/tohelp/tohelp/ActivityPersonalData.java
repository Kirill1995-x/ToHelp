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
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.prepare.FindItemInSpinner;
import com.tohelp.tohelp.resume.CheckResume;
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

public class ActivityPersonalData extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svPersonalData)
    ScrollView svPersonalData;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.tilCareerObjective)
    TextInputLayout tilCareerObjective;
    @BindView(R.id.tietCareerObjective)
    TextInputEditText tietCareerObjective;
    @BindView(R.id.tilSalary)
    TextInputLayout tilSalary;
    @BindView(R.id.tietSalary)
    TextInputEditText tietSalary;
    @BindView(R.id.tvEmployment)
    TextView tvEmployment;
    @BindView(R.id.spEmployment)
    Spinner spEmployment;
    @BindView(R.id.tvSchedule)
    TextView tvSchedule;
    @BindView(R.id.spSchedule)
    Spinner spSchedule;
    @BindView(R.id.tvMaritalStatus)
    TextView tvMaritalStatus;
    @BindView(R.id.spMaritalStatus)
    Spinner spMaritalStatus;
    @BindView(R.id.cbBusinessTrips)
    CheckBox cbBusinessTrips;
    @BindView(R.id.cbMoving)
    CheckBox cbMoving;
    @BindView(R.id.cbHavingChildren)
    CheckBox cbHavingChildren;
    @BindView(R.id.btnSavePersonalData)
    Button btnSavePersonalData;
    @BindView(R.id.progressBarPersonalData)
    ProgressBar progressBar;
    String career_objective="", salary="", employment="-", schedule="-", marital_status = "-";
    boolean business_trips, moving, having_children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_personal_data);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //изменение стиля Spinner
        ArrayAdapter array_adapter_employment = ArrayAdapter.createFromResource(ActivityPersonalData.this, R.array.array_employment, R.layout.spinner_layout);
        ArrayAdapter array_adapter_schedule = ArrayAdapter.createFromResource(ActivityPersonalData.this, R.array.array_schedule, R.layout.spinner_layout);
        ArrayAdapter array_adapter_marital_status = ArrayAdapter.createFromResource(ActivityPersonalData.this, R.array.array_marital_status, R.layout.spinner_layout);
        array_adapter_employment.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        array_adapter_schedule.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        array_adapter_marital_status.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spEmployment.setAdapter(array_adapter_employment);
        spSchedule.setAdapter(array_adapter_schedule);
        spMaritalStatus.setAdapter(array_adapter_marital_status);

        //обработка нажатия на кнопку
        btnSavePersonalData.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svPersonalData.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getPersonalData();
            else
            {
                svPersonalData.setVisibility(View.VISIBLE);
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
            case R.id.btnSavePersonalData:
                sendPersonalData();
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    getPersonalData();
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

    @Override
    public void onBackPressed() {
        if(!Objects.requireNonNull(tietCareerObjective.getText()).toString().trim().equals(career_objective) ||
           !Objects.requireNonNull(tietSalary.getText()).toString().trim().equals(salary) ||
           !spEmployment.getSelectedItem().toString().equals(employment) ||
           !spSchedule.getSelectedItem().toString().equals(schedule) ||
           !spMaritalStatus.getSelectedItem().toString().equals(marital_status) ||
           cbBusinessTrips.isChecked() != business_trips ||
           cbMoving.isChecked() != moving ||
           cbHavingChildren.isChecked() != having_children)
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "resume_edit_data_dialog");
        }
        else {
            super.onBackPressed();
        }
    }

    private void getPersonalData()
    {
        svPersonalData.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_personal_data_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                career_objective = jsonObject.getString("career_objective");
                                salary = jsonObject.getString("salary");
                                employment = jsonObject.getString("employment");
                                schedule = jsonObject.getString("schedule");
                                marital_status = jsonObject.getString("marital_status");
                                business_trips = jsonObject.getString("business_trips").equals("1");
                                moving = jsonObject.getString("moving").equals("1");
                                having_children = jsonObject.getString("having_children").equals("1");
                                tietCareerObjective.setText(career_objective);
                                tietSalary.setText(salary);
                                spEmployment.setSelection(new FindItemInSpinner("employment", employment, ActivityPersonalData.this).getPosition());
                                spSchedule.setSelection(new FindItemInSpinner("schedule", schedule, ActivityPersonalData.this).getPosition());
                                spMaritalStatus.setSelection(new FindItemInSpinner("marital_status", marital_status, ActivityPersonalData.this).getPosition());
                                cbBusinessTrips.setChecked(business_trips);
                                cbMoving.setChecked(moving);
                                cbHavingChildren.setChecked(having_children);
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

    private void sendPersonalData()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSavePersonalData.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(this, tilCareerObjective, tilSalary, tvEmployment, spEmployment,
                    tvSchedule, spSchedule, tvMaritalStatus, spMaritalStatus).CheckFieldsPersonalData())
            {
                displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_personal_data_url,
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
                        String business_trips = (cbBusinessTrips.isChecked())?"1":"0";
                        String moving = (cbMoving.isChecked())?"1":"0";
                        String having_children = (cbHavingChildren.isChecked())?"1":"0";
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("career_objective", Objects.requireNonNull(tilCareerObjective.getEditText()).getText().toString().trim());
                        params.put("salary", Objects.requireNonNull(tilSalary.getEditText()).getText().toString().trim());
                        params.put("employment", spEmployment.getSelectedItem().toString());
                        params.put("schedule", spSchedule.getSelectedItem().toString());
                        params.put("marital_status", spMaritalStatus.getSelectedItem().toString());
                        params.put("business_trips", business_trips);
                        params.put("moving", moving);
                        params.put("having_children", having_children);
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("career_objective", Objects.requireNonNull(tietCareerObjective.getText()).toString().trim());
        outState.putString("salary", Objects.requireNonNull(tietSalary.getText()).toString().trim());
        outState.putInt("employment", spEmployment.getSelectedItemPosition());
        outState.putInt("schedule", spSchedule.getSelectedItemPosition());
        outState.putInt("marital_status", spMaritalStatus.getSelectedItemPosition());
        outState.putBoolean("business_trips", cbBusinessTrips.isChecked());
        outState.putBoolean("moving", cbMoving.isChecked());
        outState.putBoolean("having_children", cbHavingChildren.isChecked());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        outState.putString("career_objective_string", career_objective);
        outState.putString("salary_string", salary);
        outState.putString("employment_string", employment);
        outState.putString("schedule_string", schedule);
        outState.putString("marital_status_string", marital_status);
        outState.putBoolean("business_trips_boolean", business_trips);
        outState.putBoolean("moving_boolean", moving);
        outState.putBoolean("having_children_boolean", having_children);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tietCareerObjective.setText(savedInstanceState.getString("career_objective",""));
        tietSalary.setText(savedInstanceState.getString("salary",""));
        spEmployment.setSelection(savedInstanceState.getInt("employment",0));
        spSchedule.setSelection(savedInstanceState.getInt("schedule",0));
        spMaritalStatus.setSelection(savedInstanceState.getInt("marital_status",0));
        cbBusinessTrips.setChecked(savedInstanceState.getBoolean("business_trips", false));
        cbMoving.setChecked(savedInstanceState.getBoolean("moving",false));
        cbHavingChildren.setChecked(savedInstanceState.getBoolean("having_children",false));
        career_objective = savedInstanceState.getString("career_objective_string");
        salary = savedInstanceState.getString("salary_string");
        employment = savedInstanceState.getString("employment_int");
        schedule = savedInstanceState.getString("schedule_int");
        marital_status = savedInstanceState.getString("marital_status_string");
        business_trips = savedInstanceState.getBoolean("business_trips_boolean");
        moving = savedInstanceState.getBoolean("moving_boolean");
        having_children = savedInstanceState.getBoolean("having_children_boolean");
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
            career_objective = Objects.requireNonNull(tietCareerObjective.getText()).toString().trim();
            salary = Objects.requireNonNull(tietSalary.getText()).toString().trim();
            employment = spEmployment.getSelectedItem().toString();
            schedule = spSchedule.getSelectedItem().toString();
            marital_status = spMaritalStatus.getSelectedItem().toString();
            business_trips = cbBusinessTrips.isChecked();
            moving = cbMoving.isChecked();
            having_children = cbHavingChildren.isChecked();
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