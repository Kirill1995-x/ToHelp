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

public class ActivityMainSkills extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svMainSkills)
    ScrollView svMainSkills;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.tilBasicSkills)
    TextInputLayout tilBasicSkills;
    @BindView(R.id.tietBasicSkills)
    TextInputEditText tietBasicSkills;
    @BindView(R.id.tilProgram)
    TextInputLayout tilProgram;
    @BindView(R.id.tietProgram)
    TextInputEditText tietProgram;
    @BindView(R.id.spComputerSkills)
    Spinner spComputerSkills;
    @BindView(R.id.tvComputerSkills)
    TextView tvComputerSkills;
    @BindView(R.id.cbMilitaryService)
    CheckBox cbMilitaryService;
    @BindView(R.id.cbCategoryA)
    CheckBox cbCategoryA;
    @BindView(R.id.cbCategoryB)
    CheckBox cbCategoryB;
    @BindView(R.id.cbCategoryBE)
    CheckBox cbCategoryBE;
    @BindView(R.id.cbCategoryC)
    CheckBox cbCategoryC;
    @BindView(R.id.cbCategoryCE)
    CheckBox cbCategoryCE;
    @BindView(R.id.cbCategoryD)
    CheckBox cbCategoryD;
    @BindView(R.id.cbCategoryDE)
    CheckBox cbCategoryDE;
    @BindView(R.id.cbCategoryM)
    CheckBox cbCategoryM;
    @BindView(R.id.cbCategoryTbAndTM)
    CheckBox cbCategoryTbAndTm;
    @BindView(R.id.btnSaveMainSkills)
    Button btnSaveMainSkills;
    @BindView(R.id.progressBarMainSkills)
    ProgressBar progressBar;
    String basic_skills="", program="", computer_skills="-";
    boolean military_service;
    boolean categoryA, categoryB, categoryBE, categoryC, categoryCE, categoryD, categoryDE, categoryM, categoryTbAndTm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_main_skills);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //изменение стиля Spinner
        ArrayAdapter array_adapter_computer_skills = ArrayAdapter.createFromResource(ActivityMainSkills.this, R.array.array_skill_of_using_computer, R.layout.spinner_layout);
        array_adapter_computer_skills.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spComputerSkills.setAdapter(array_adapter_computer_skills);

        //обработка нажатия на кнопку
        btnSaveMainSkills.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svMainSkills.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getMainSkills();
            else
            {
                svMainSkills.setVisibility(View.VISIBLE);
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
            case R.id.btnSaveMainSkills:
                sendMainSkills();
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    getMainSkills();
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
        if(!Objects.requireNonNull(tietBasicSkills.getText()).toString().trim().equals(basic_skills) ||
           !Objects.requireNonNull(tietProgram.getText()).toString().trim().equals(program) ||
           !spComputerSkills.getSelectedItem().toString().equals(computer_skills) ||
           cbMilitaryService.isChecked() != military_service ||
           cbCategoryA.isChecked() != categoryA ||
           cbCategoryB.isChecked() != categoryB ||
           cbCategoryBE.isChecked() != categoryBE ||
           cbCategoryC.isChecked() !=categoryC ||
           cbCategoryCE.isChecked() !=categoryCE ||
           cbCategoryD.isChecked() !=categoryD ||
           cbCategoryDE.isChecked() != categoryDE ||
           cbCategoryM.isChecked() != categoryM ||
           cbCategoryTbAndTm.isChecked() != categoryTbAndTm)
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "resume_edit_data_dialog");
        }
        else {
            super.onBackPressed();
        }
    }

    private void getMainSkills()
    {
        svMainSkills.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_main_skills_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                basic_skills = jsonObject.getString("basic_skills");
                                computer_skills = jsonObject.getString("computer_skills");
                                program = jsonObject.getString("program");
                                military_service = jsonObject.getString("military_service").equals("1");
                                int drivers_licences = Integer.parseInt(jsonObject.getString("drivers_licences"));
                                categoryA=((drivers_licences&0x1)==0x1);
                                categoryB=((drivers_licences&0x2)==0x2);
                                categoryBE=((drivers_licences&0x4)==0x4);
                                categoryC=((drivers_licences&0x8)==0x8);
                                categoryCE=((drivers_licences&0x10)==0x10);
                                categoryD=((drivers_licences&0x20)==0x20);
                                categoryDE=((drivers_licences&0x40)==0x40);
                                categoryM=((drivers_licences&0x80)==0x80);
                                categoryTbAndTm=((drivers_licences&0x100)==0x100);
                                //---
                                tietBasicSkills.setText(basic_skills);
                                spComputerSkills.setSelection(new FindItemInSpinner("computer_skills", computer_skills, ActivityMainSkills.this).getPosition());
                                tietProgram.setText(program);
                                cbMilitaryService.setChecked(military_service);
                                cbCategoryA.setChecked(categoryA);
                                cbCategoryB.setChecked(categoryB);
                                cbCategoryBE.setChecked(categoryBE);
                                cbCategoryC.setChecked(categoryC);
                                cbCategoryCE.setChecked(categoryCE);
                                cbCategoryD.setChecked(categoryD);
                                cbCategoryDE.setChecked(categoryDE);
                                cbCategoryM.setChecked(categoryM);
                                cbCategoryTbAndTm.setChecked(categoryTbAndTm);
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

    private void sendMainSkills()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSaveMainSkills.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(this, tilBasicSkills, tilProgram, tvComputerSkills, spComputerSkills).CheckFieldsMainSkills())
            {
                displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_main_skills_url,
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
                        String military_service = (cbMilitaryService.isChecked())?"1":"0";
                        int drivers_licences = 0;
                        drivers_licences|=(cbCategoryA.isChecked())?0x1:0x0;
                        drivers_licences|=(cbCategoryB.isChecked())?0x2:0x0;
                        drivers_licences|=(cbCategoryBE.isChecked())?0x4:0x0;
                        drivers_licences|=(cbCategoryC.isChecked())?0x8:0x0;
                        drivers_licences|=(cbCategoryCE.isChecked())?0x10:0x0;
                        drivers_licences|=(cbCategoryD.isChecked())?0x20:0x0;
                        drivers_licences|=(cbCategoryDE.isChecked())?0x40:0x0;
                        drivers_licences|=(cbCategoryM.isChecked())?0x80:0x0;
                        drivers_licences|=(cbCategoryTbAndTm.isChecked())?0x100:0x0;
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("basic_skills", Objects.requireNonNull(tietBasicSkills.getText()).toString().trim());
                        params.put("computer_skills", spComputerSkills.getSelectedItem().toString());
                        params.put("program", Objects.requireNonNull(tietProgram.getText()).toString().trim());
                        params.put("military_service", military_service);
                        params.put("drivers_licences", String.valueOf(drivers_licences));
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
        outState.putBoolean("showProgressBar", progressBar.isShown());
        outState.putString("basic_skills", Objects.requireNonNull(tietBasicSkills.getText()).toString().trim());
        outState.putString("program", Objects.requireNonNull(tietProgram.getText()).toString().trim());
        outState.putInt("computer_skills", spComputerSkills.getSelectedItemPosition());
        outState.putBoolean("military_service", cbMilitaryService.isChecked());
        outState.putBoolean("category_A", cbCategoryA.isChecked());
        outState.putBoolean("category_B", cbCategoryB.isChecked());
        outState.putBoolean("category_BE", cbCategoryBE.isChecked());
        outState.putBoolean("category_C", cbCategoryC.isChecked());
        outState.putBoolean("category_CE", cbCategoryCE.isChecked());
        outState.putBoolean("category_D", cbCategoryD.isChecked());
        outState.putBoolean("category_DE", cbCategoryDE.isChecked());
        outState.putBoolean("category_M", cbCategoryM.isChecked());
        outState.putBoolean("category_Tb_and_Tm", cbCategoryTbAndTm.isChecked());
        outState.putString("basic_skills_string", basic_skills);
        outState.putString("program_string", program);
        outState.putString("computer_skills_string", computer_skills);
        outState.putBoolean("military_service_boolean", military_service);
        outState.putBoolean("category_A_boolean", categoryA);
        outState.putBoolean("category_B_boolean", categoryB);
        outState.putBoolean("category_BE_boolean", categoryBE);
        outState.putBoolean("category_C_boolean", categoryC);
        outState.putBoolean("category_CE_boolean", categoryCE);
        outState.putBoolean("category_D_boolean", categoryD);
        outState.putBoolean("category_DE_boolean", categoryDE);
        outState.putBoolean("category_M_boolean", categoryM);
        outState.putBoolean("category_Tb_and_Tm_boolean", categoryTbAndTm);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tietBasicSkills.setText(savedInstanceState.getString("basic_skills",""));
        tietProgram.setText(savedInstanceState.getString("program",""));
        spComputerSkills.setSelection(savedInstanceState.getInt("computer_skills"));
        cbMilitaryService.setChecked(savedInstanceState.getBoolean("military_service"));
        cbCategoryA.setChecked(savedInstanceState.getBoolean("category_A"));
        cbCategoryB.setChecked(savedInstanceState.getBoolean("category_B"));
        cbCategoryBE.setChecked(savedInstanceState.getBoolean("category_BE"));
        cbCategoryC.setChecked(savedInstanceState.getBoolean("category_C"));
        cbCategoryCE.setChecked(savedInstanceState.getBoolean("category_CE"));
        cbCategoryD.setChecked(savedInstanceState.getBoolean("category_D"));
        cbCategoryDE.setChecked(savedInstanceState.getBoolean("category_DE"));
        cbCategoryM.setChecked(savedInstanceState.getBoolean("category_M"));
        cbCategoryTbAndTm.setChecked(savedInstanceState.getBoolean("category_Tb_and_Tm"));
        categoryA = savedInstanceState.getBoolean("category_A_boolean");
        categoryB = savedInstanceState.getBoolean("category_B_boolean");
        categoryBE = savedInstanceState.getBoolean("category_BE_boolean");
        categoryC = savedInstanceState.getBoolean("category_C_boolean");
        categoryCE = savedInstanceState.getBoolean("category_CE_boolean");
        categoryD = savedInstanceState.getBoolean("category_D_boolean");
        categoryDE = savedInstanceState.getBoolean("category_DE_boolean");
        categoryM = savedInstanceState.getBoolean("category_M_boolean");
        categoryTbAndTm = savedInstanceState.getBoolean("category_Tb_and_Tm_boolean");
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
            basic_skills = Objects.requireNonNull(tietBasicSkills.getText()).toString().trim();
            program = Objects.requireNonNull(tietProgram.getText()).toString().trim();
            computer_skills = spComputerSkills.getSelectedItem().toString();
            military_service = cbMilitaryService.isChecked();
            categoryA = cbCategoryA.isChecked();
            categoryB = cbCategoryB.isChecked();
            categoryBE = cbCategoryBE.isChecked();
            categoryC = cbCategoryC.isChecked();
            categoryCE = cbCategoryCE.isChecked();
            categoryD = cbCategoryD.isChecked();
            categoryDE = cbCategoryDE.isChecked();
            categoryM = cbCategoryM.isChecked();
            categoryTbAndTm = cbCategoryTbAndTm.isChecked();
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