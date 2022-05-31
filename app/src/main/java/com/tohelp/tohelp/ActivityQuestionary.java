package com.tohelp.tohelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.tohelp.tohelp.prepare.Questionary;
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

public class ActivityQuestionary extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svActivityQuestionary)
    ScrollView svActivityQuestionary;
    @BindView(R.id.progressBarQuestionary)
    ProgressBar progressBar;
    @BindView(R.id.tilMyTargets)
    TextInputLayout tilMyTargets;
    @BindView(R.id.tilEducationProblem)
    TextInputLayout tilEducationProblem;
    @BindView(R.id.tilFlatProblem)
    TextInputLayout tilFlatProblem;
    @BindView(R.id.tilMoneyProblem)
    TextInputLayout tilMoneyProblem;
    @BindView(R.id.tilLawProblem)
    TextInputLayout tilLawProblem;
    @BindView(R.id.tilOtherProblem)
    TextInputLayout tilOtherProblem;
    @BindView(R.id.tilProfessional)
    TextInputLayout tilProfessional;
    @BindView(R.id.tilEducationInstitution)
    TextInputLayout tilEducationInstitution;
    @BindView(R.id.tilInterests)
    TextInputLayout tilInterests;
    @BindView(R.id.tietMyTargets)
    TextInputEditText tietMyTargets;
    @BindView(R.id.tietEducationProblem)
    TextInputEditText tietEducationProblem;
    @BindView(R.id.tietFlatProblem)
    TextInputEditText tietFlatProblem;
    @BindView(R.id.tietMoneyProblem)
    TextInputEditText tietMoneyProblem;
    @BindView(R.id.tietLawProblem)
    TextInputEditText tietLawProblem;
    @BindView(R.id.tietOtherProblem)
    TextInputEditText tietOtherProblem;
    @BindView(R.id.tietProfessional)
    TextInputEditText tietProfessional;
    @BindView(R.id.tietEducationInstitution)
    TextInputEditText tietEducationInstitution;
    @BindView(R.id.tietInterests)
    TextInputEditText tietInterests;
    @BindView(R.id.tvLevelOfEducation)
    TextView tvLevelOfEducation;
    @BindView(R.id.spLevelOfEducation)
    Spinner spLevelOfEducation;
    @BindView(R.id.btnSendData)
    Button btnSendData;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionary);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //настройка SharedPreferences
        sharedPreferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);

        //изменение стиля Spinner
        ArrayAdapter adapter_for_level_of_education = ArrayAdapter.createFromResource(this,
                                                                    R.array.array_level_education,
                                                                    R.layout.spinner_layout);
        adapter_for_level_of_education.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spLevelOfEducation.setAdapter(adapter_for_level_of_education);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //обработка нажатия на элементы
        btnSendData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSendData:
                confirmInput();
                break;
            default:
                break;
        }
    }

    public void confirmInput()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSendData.getWindowToken(),
                                                                            InputMethodManager.HIDE_NOT_ALWAYS);
        Questionary questionary = new Questionary(this, tilMyTargets, tilEducationProblem, tilFlatProblem,
                                                  tilMoneyProblem, tilLawProblem, tilOtherProblem, tilEducationInstitution,
                                                  spLevelOfEducation, tvLevelOfEducation, tilProfessional, tilInterests);
        //---
        if (new CheckInternetConnection(this).isNetworkConnected()) {
            if (questionary.checkFieldsQuestionary())
            {
                displayAlert("input_failed",
                        getResources().getString(R.string.something_went_wrong),
                        getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_questionary_url,
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
                        try {
                            params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("main_target", Objects.requireNonNull(tilMyTargets.getEditText()).getText().toString().trim());
                        params.put("problem_education", Objects.requireNonNull(tilEducationProblem.getEditText()).getText().toString().trim());
                        params.put("problem_flat", Objects.requireNonNull(tilFlatProblem.getEditText()).getText().toString().trim());
                        params.put("problem_money", Objects.requireNonNull(tilMoneyProblem.getEditText()).getText().toString().trim());
                        params.put("problem_law", Objects.requireNonNull(tilLawProblem.getEditText()).getText().toString().trim());
                        params.put("problem_other", Objects.requireNonNull(tilOtherProblem.getEditText()).getText().toString().trim());
                        params.put("level_of_education", spLevelOfEducation.getSelectedItem().toString().trim());
                        params.put("name_education_institution", Objects.requireNonNull(tilEducationInstitution.getEditText()).getText().toString().trim());
                        params.put("my_professional", Objects.requireNonNull(tilProfessional.getEditText()).getText().toString().trim());
                        params.put("my_interests", Objects.requireNonNull(tilInterests.getEditText()).getText().toString().trim());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
        else
        {
            displayAlert("internet_connection_failed",
                    getResources().getString(R.string.error_connection),
                    getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putString("main_target", Objects.requireNonNull(tietMyTargets.getText()).toString().trim());
        outState.putString("problem_education", Objects.requireNonNull(tietEducationProblem.getText()).toString().trim());
        outState.putString("problem_flat", Objects.requireNonNull(tietFlatProblem.getText()).toString().trim());
        outState.putString("problem_money", Objects.requireNonNull(tietMoneyProblem.getText()).toString().trim());
        outState.putString("problem_law", Objects.requireNonNull(tietLawProblem.getText()).toString().trim());
        outState.putString("problem_other", Objects.requireNonNull(tietOtherProblem.getText()).toString().trim());
        outState.putString("name_education_institution", Objects.requireNonNull(tietEducationInstitution.getText()).toString().trim());
        outState.putString("my_professional", Objects.requireNonNull(tietProfessional.getText()).toString().trim());
        outState.putString("my_interests", Objects.requireNonNull(tietInterests.getText()).toString().trim());
        outState.putInt("level_of_education", spLevelOfEducation.getSelectedItemPosition());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tietMyTargets.setText(savedInstanceState.getString("main_target"," "));
        tietEducationProblem.setText(savedInstanceState.getString("problem_education"," "));
        tietFlatProblem.setText(savedInstanceState.getString("problem_flat"," "));
        tietMoneyProblem.setText(savedInstanceState.getString("problem_money"," "));
        tietLawProblem.setText(savedInstanceState.getString("problem_law"," "));
        tietOtherProblem.setText(savedInstanceState.getString("problem_other"," "));
        tietEducationInstitution.setText(savedInstanceState.getString("name_education_institution"," "));
        tietProfessional.setText(savedInstanceState.getString("my_professional", " "));
        tietInterests.setText(savedInstanceState.getString("my_interests", " "));
        spLevelOfEducation.setSelection(savedInstanceState.getInt("level_of_education",0));
    }

    @Override
    public void onBackPressed() {
        if(!Objects.requireNonNull(tietMyTargets.getText()).toString().trim().isEmpty() ||
           !Objects.requireNonNull(tietEducationProblem.getText()).toString().trim().isEmpty() ||
           !Objects.requireNonNull(tietFlatProblem.getText()).toString().trim().isEmpty() ||
           !Objects.requireNonNull(tietMoneyProblem.getText()).toString().trim().isEmpty() ||
           !Objects.requireNonNull(tietLawProblem.getText()).toString().trim().isEmpty() ||
           !Objects.requireNonNull(tietOtherProblem.getText()).toString().trim().isEmpty() ||
           !spLevelOfEducation.getSelectedItem().toString().equals("-") ||
           !Objects.requireNonNull(tilEducationInstitution.getEditText()).getText().toString().trim().isEmpty() ||
           !Objects.requireNonNull(tilProfessional.getEditText()).getText().toString().trim().isEmpty() ||
           !Objects.requireNonNull(tilInterests.getEditText()).getText().toString().trim().isEmpty())
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "questionary_edit_data_dialog");
        }
        else {
            super.onBackPressed();
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
    public void displayAlert(String code, String title, String message)
    {
        if(code.equals("questionary_get_success"))
        {
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "questionary_dialog_finish");
        }
        else {
            DialogSimple dialog = new DialogSimple();
            DialogSimple.title = title;
            DialogSimple.message = message;
            dialog.show(getSupportFragmentManager(), "questionary_dialog");
        }
    }
}

