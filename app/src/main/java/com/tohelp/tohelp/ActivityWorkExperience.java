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
import android.widget.CompoundButton;
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
import com.tohelp.tohelp.resume.WorkExperience;
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

public class ActivityWorkExperience extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svWorkExperience)
    ScrollView svWorkExperience;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.llShowWorkExperience)
    LinearLayout llShowWorkExperience;
    @BindView(R.id.btnSaveWorkExperience)
    Button btnSaveWorkExperience;
    @BindView(R.id.btnAddWorkExperience)
    Button btnAddWorkExperience;
    @BindView(R.id.progressBarWorkExperience)
    ProgressBar progressBar;
    WorkExperience workExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_work_experience);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //обработка нажатий на кнопку
        btnSaveWorkExperience.setOnClickListener(this);
        btnAddWorkExperience.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svWorkExperience.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getWorkExperience();
            else
            {
                svWorkExperience.setVisibility(View.VISIBLE);
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
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    if(llShowWorkExperience.getChildCount()>0) {
                        svWorkExperience.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                    }
                    else
                    {
                        getWorkExperience();
                    }
                }
                break;
            case R.id.btnAddWorkExperience:
                addWorkExperience();
                break;
            case R.id.btnSaveWorkExperience:
                sendWorkExperience();
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

    private void getWorkExperience()
    {
        svWorkExperience.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_work_experience_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                workExperience = new WorkExperience(jsonObject.getString("work"));
                                splitWorkExperience(workExperience.getWorkExperience());
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

    private void sendWorkExperience()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSaveWorkExperience.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(llShowWorkExperience).CheckFieldsWorkExperience())
            {
                displayAlert("input_failed",getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_work_experience_url,
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
                        params.put("work", uniteWorkExperience());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(workExperience!=null && !uniteWorkExperience().equals(workExperience.getWorkExperience()))
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
        outState.putString("unite_work_experience", uniteWorkExperience());
        outState.putParcelable("work_experience", workExperience);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        workExperience = savedInstanceState.getParcelable("work_experience");
        String unite_work_experience = savedInstanceState.getString("unite_work_experience");
        unite_work_experience=(unite_work_experience==null)?"":unite_work_experience;
        splitWorkExperience(unite_work_experience);
    }

    private void addWorkExperience()
    {
        if(llShowWorkExperience.getChildCount()<10)
        {
            View view = getLayoutInflater().inflate(R.layout.pattern_work_experience, null, false);
            LinearLayout llEndOfWork = view.findViewById(R.id.llEndOfWork);
            Spinner spOldEmployment = view.findViewById(R.id.spOldEmployment);
            Spinner spStartOfWorkMonth = view.findViewById(R.id.spStartOfWorkPeriodMonth);
            Spinner spStartOfWorkYear = view.findViewById(R.id.spStartOfWorkPeriodYear);
            Spinner spEndOfWorkMonth = view.findViewById(R.id.spEndOfWorkPeriodMonth);
            Spinner spEndOfWorkYear = view.findViewById(R.id.spEndOfWorkPeriodYear);
            CheckBox cbWorkNow = view.findViewById(R.id.cbWorkNow);
            ArrayAdapter adapter_for_old_employment = ArrayAdapter.createFromResource(this, R.array.array_employment, R.layout.spinner_layout);
            ArrayAdapter<String> adapter_for_start_work_month = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceMonth());
            ArrayAdapter<String> adapter_for_start_work_year = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceYears());
            ArrayAdapter<String> adapter_for_end_work_month = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceMonth());
            ArrayAdapter<String> adapter_for_end_work_year = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceYears());
            adapter_for_old_employment.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_start_work_month.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_start_work_year.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_end_work_month.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter_for_end_work_year.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            spOldEmployment.setAdapter(adapter_for_old_employment);
            spStartOfWorkMonth.setAdapter(adapter_for_start_work_month);
            spStartOfWorkYear.setAdapter(adapter_for_start_work_year);
            spEndOfWorkMonth.setAdapter(adapter_for_end_work_month);
            spEndOfWorkYear.setAdapter(adapter_for_end_work_year);
            cbWorkNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) llEndOfWork.setVisibility(View.GONE);
                    else llEndOfWork.setVisibility(View.VISIBLE);
                }
            });
            view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llShowWorkExperience.removeView(view);
                }
            });

            llShowWorkExperience.addView(view);
        }
        else
        {
            displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_count_of_blocks));
        }
    }


    public String uniteWorkExperience()
    {
        StringBuilder work_experience= new StringBuilder();
        //---
        for(int i=0; i<llShowWorkExperience.getChildCount(); i++)
        {
            View view = llShowWorkExperience.getChildAt(i);
            EditText etOldOrganization = view.findViewById(R.id.etOldOrganization);
            EditText etOldPosition = view.findViewById(R.id.etOldPosition);
            EditText etOldResponsibility = view.findViewById(R.id.etOldResponsibility);
            Spinner spOldEmployment = view.findViewById(R.id.spOldEmployment);
            Spinner spStartOfWorkPeriodMonth = view.findViewById(R.id.spStartOfWorkPeriodMonth);
            Spinner spStartOfWorkPeriodYear = view.findViewById(R.id.spStartOfWorkPeriodYear);
            Spinner spEndOfWorkPeriodMonth = view.findViewById(R.id.spEndOfWorkPeriodMonth);
            Spinner spEndOfWorkPeriodYear = view.findViewById(R.id.spEndOfWorkPeriodYear);
            CheckBox cbWorkNow = view.findViewById(R.id.cbWorkNow);
            String old_organization = (etOldOrganization.getText().toString().trim().isEmpty())?" ":etOldOrganization.getText().toString().trim();
            String old_position = (etOldPosition.getText().toString().trim().isEmpty())?" ":etOldPosition.getText().toString().trim();
            String old_responsibility = (etOldResponsibility.getText().toString().trim().isEmpty())?" ":etOldResponsibility.getText().toString().trim();
            if(cbWorkNow.isChecked())
            {
                work_experience.append(old_organization).append("tag")
                        .append(old_position).append("tag")
                        .append(old_responsibility).append("tag")
                        .append(spOldEmployment.getSelectedItem().toString()).append("tag")
                        .append(spStartOfWorkPeriodMonth.getSelectedItem().toString()).append("tag")
                        .append(spStartOfWorkPeriodYear.getSelectedItem().toString()).append("tag")
                        .append("по настоящий момент").append("block");
            }
            else {
                work_experience.append(old_organization).append("tag")
                        .append(old_position).append("tag")
                        .append(old_responsibility).append("tag")
                        .append(spOldEmployment.getSelectedItem().toString()).append("tag")
                        .append(spStartOfWorkPeriodMonth.getSelectedItem().toString()).append("tag")
                        .append(spStartOfWorkPeriodYear.getSelectedItem().toString()).append("tag")
                        .append(spEndOfWorkPeriodMonth.getSelectedItem().toString()).append("tag")
                        .append(spEndOfWorkPeriodYear.getSelectedItem().toString()).append("block");
            }
        }
        return work_experience.toString();
    }

    public void splitWorkExperience(String work_experience)
    {
        if(!work_experience.equals("")) {
            String[] array_of_work_experience = work_experience.split("block");
            //---
            for (int i = 0; i < array_of_work_experience.length; i++) {
                View view = getLayoutInflater().inflate(R.layout.pattern_work_experience, null, false);
                LinearLayout llEndOfWork = view.findViewById(R.id.llEndOfWork);
                EditText etOldOrganization = view.findViewById(R.id.etOldOrganization);
                EditText etOldPosition = view.findViewById(R.id.etOldPosition);
                EditText etOldResponsibility = view.findViewById(R.id.etOldResponsibility);
                Spinner spOldEmployment = view.findViewById(R.id.spOldEmployment);
                Spinner spStartOfWorkMonth = view.findViewById(R.id.spStartOfWorkPeriodMonth);
                Spinner spStartOfWorkYear = view.findViewById(R.id.spStartOfWorkPeriodYear);
                Spinner spEndOfWorkMonth = view.findViewById(R.id.spEndOfWorkPeriodMonth);
                Spinner spEndOfWorkYear = view.findViewById(R.id.spEndOfWorkPeriodYear);
                CheckBox cbWorkNow = view.findViewById(R.id.cbWorkNow);
                ArrayAdapter adapter_for_old_employment = ArrayAdapter.createFromResource(this, R.array.array_employment, R.layout.spinner_layout);
                ArrayAdapter<String> adapter_for_start_work_month = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceMonth());
                ArrayAdapter<String> adapter_for_start_work_year = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceYears());
                ArrayAdapter<String> adapter_for_end_work_month = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceMonth());
                ArrayAdapter<String> adapter_for_end_work_year = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayForResume().CreateArrayForWorkExperienceYears());
                adapter_for_old_employment.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_start_work_month.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_start_work_year.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_end_work_month.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter_for_end_work_year.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                spOldEmployment.setAdapter(adapter_for_old_employment);
                spStartOfWorkMonth.setAdapter(adapter_for_start_work_month);
                spStartOfWorkYear.setAdapter(adapter_for_start_work_year);
                spEndOfWorkMonth.setAdapter(adapter_for_end_work_month);
                spEndOfWorkYear.setAdapter(adapter_for_end_work_year);
                cbWorkNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) llEndOfWork.setVisibility(View.GONE);
                        else llEndOfWork.setVisibility(View.VISIBLE);
                    }
                });
                view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llShowWorkExperience.removeView(view);
                    }
                });

                String[] split = array_of_work_experience[i].split("tag");
                String old_organization = (split[0].equals(" ")) ? "" : split[0];
                String old_position = (split[1].equals(" ")) ? "" : split[1];
                String old_responsibility = (split[2].equals(" ")) ? "" : split[2];
                etOldOrganization.setText(old_organization);
                etOldPosition.setText(old_position);
                etOldResponsibility.setText(old_responsibility);
                spOldEmployment.setSelection(new FindItemInSpinner("employment", split[3], this).getPosition());
                spStartOfWorkMonth.setSelection(new FindItemInSpinner("month_work", split[4], this).getPosition());
                spStartOfWorkYear.setSelection(new FindItemInSpinner("year_work", split[5], this).getPosition());
                if(split[6].equals("по настоящий момент"))
                {
                    cbWorkNow.setChecked(true);
                    llEndOfWork.setVisibility(View.GONE);
                    spEndOfWorkMonth.setSelection(new FindItemInSpinner("month_work", "Месяц", this).getPosition());
                    spEndOfWorkYear.setSelection(new FindItemInSpinner("year_work", "Год", this).getPosition());
                }
                else
                {
                    cbWorkNow.setChecked(false);
                    llEndOfWork.setVisibility(View.VISIBLE);
                    spEndOfWorkMonth.setSelection(new FindItemInSpinner("month_work", split[6], this).getPosition());
                    spEndOfWorkYear.setSelection(new FindItemInSpinner("year_work", split[7], this).getPosition());
                }
                llShowWorkExperience.addView(view);
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
            if(workExperience==null) workExperience = new WorkExperience(uniteWorkExperience());
            else workExperience.setWorkExperience(uniteWorkExperience());
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