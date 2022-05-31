package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santalu.maskedittext.MaskEditText;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.prepare.GetArrayOfBirth;
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

public class Register extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.tilRegisterSurname)
    TextInputLayout tilSurname;
    @BindView(R.id.tilRegisterName)
    TextInputLayout tilName;
    @BindView(R.id.tilRegisterMiddlename)
    TextInputLayout tilMiddlename;
    @BindView(R.id.tilRegisterChidHome)
    TextInputLayout tilChildHome;
    @BindView(R.id.tilRegisterEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilRegisterPhone)
    TextInputLayout tilPhone;
    @BindView(R.id.tilRegisterRegistrationAddress)
    TextInputLayout tilRegistrationAddress;
    @BindView(R.id.tilRegisterFactualAddress)
    TextInputLayout tilFactualAddress;
    @BindView(R.id.tilRegisterPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tilRegisterConfirmPassword)
    TextInputLayout tilConfirmPassword;
    @BindView(R.id.tilRegisterCity)
    TextInputLayout tilCity;
    @BindView(R.id.tilRegisterSubject)
    TextInputLayout tilSubject;
    @BindView(R.id.tietRegisterSurname)
    TextInputEditText tietSurname;
    @BindView(R.id.tietRegisterName)
    TextInputEditText tietName;
    @BindView(R.id.tietRegisterMiddlename)
    TextInputEditText tietMiddlename;
    @BindView(R.id.tietRegisterEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.metRegisterPhone)
    MaskEditText metPhone;
    @BindView(R.id.tietRegisterChildHome)
    TextInputEditText tietChildHome;
    @BindView(R.id.tietRegisterRegistrationAddress)
    TextInputEditText tietRegistrationAddress;
    @BindView(R.id.tietRegisterFactualAddress)
    TextInputEditText tietFactualAddress;
    @BindView(R.id.tietRegisterPassword)
    TextInputEditText tietPassword;
    @BindView(R.id.tietRegisterConfirmPassword)
    TextInputEditText tietConfirmPassword;
    @BindView(R.id.cbAgreement)
    CheckBox cbAgreement;
    @BindView(R.id.spRegisterTypeOfFlat)
    Spinner spTypeOfFlat;
    @BindView(R.id.spRegisterDate)
    Spinner spDate;
    @BindView(R.id.spRegisterMonth)
    Spinner spMonth;
    @BindView(R.id.spRegisterYear)
    Spinner spYear;
    @BindView(R.id.spRegisterSex)
    Spinner spSex;
    @BindView(R.id.tvRegisterDateOfBirth)
    TextView tvDateOfBirth;
    @BindView(R.id.tvRegisterSex)
    TextView tvSex;
    @BindView(R.id.tvLinkToDocument)
    TextView tvLinkToDocument;
    @BindView(R.id.tvCopyrightInformation)
    TextView tvCopyrightInformation;
    @BindView(R.id.actvRegisterSubject)
    AutoCompleteTextView actvSubject;
    @BindView(R.id.actvRegisterCity)
    AutoCompleteTextView actvCity;
    @BindView(R.id.progressBarRegister)
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;
    String [] array_subject_of_Russian_Federation;
    String [] array_city_of_Russian_Federation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //обработка нажатия на элементы
        btnRegister.setOnClickListener(this);
        tvLinkToDocument.setOnClickListener(this);
        tvCopyrightInformation.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //Ввод адаптера для работы с массивами: региона и города
        array_subject_of_Russian_Federation=getResources().getStringArray(R.array.array_subjects);
        array_city_of_Russian_Federation=getResources().getStringArray(R.array.array_cities);
        ArrayAdapter<String> adapter_for_subject=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_subject_of_Russian_Federation);
        ArrayAdapter<String> adapter_for_city=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_city_of_Russian_Federation);
        actvSubject.setAdapter(adapter_for_subject);
        actvCity.setAdapter(adapter_for_city);
        //Изменения стиля Spinner
        ArrayAdapter<String> adapter_for_date_of_birth = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayDate());
        ArrayAdapter<String> adapter_for_month_of_birth = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayMonth());
        ArrayAdapter<String> adapter_for_year_of_birth = (getSharedPreferences(Variable.APP_NOTIFICATIONS, MODE_PRIVATE).getBoolean("check_age",true))?
                new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayYearsStrong()):
                new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayYears());
        ArrayAdapter adapter_for_sex=ArrayAdapter.createFromResource(this, R.array.array_sex, R.layout.spinner_layout);
        ArrayAdapter adapter_type_of_flat=ArrayAdapter.createFromResource(this, R.array.array_type_of_flat, R.layout.spinner_layout);
        adapter_for_sex.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_date_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_month_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_year_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_type_of_flat.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spSex.setAdapter(adapter_for_sex);
        spDate.setAdapter(adapter_for_date_of_birth);
        spMonth.setAdapter(adapter_for_month_of_birth);
        spYear.setAdapter(adapter_for_year_of_birth);
        spTypeOfFlat.setAdapter(adapter_type_of_flat);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btnRegister:
                confirmInput();
                break;
            case R.id.tvLinkToDocument:
                intent=new Intent(Register.this, Browser.class);
                intent.putExtra("url_phone", Variable.document_phone_url);
                intent.putExtra("url_tablet", Variable.document_tablet_url);
                startActivity(intent);
                break;
            case R.id.tvCopyrightInformation:
                intent=new Intent(Register.this, CopyrightInformation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!Objects.requireNonNull(tietSurname.getText()).toString().trim().equals("") | !Objects.requireNonNull(tietName.getText()).toString().trim().equals("") |
        !Objects.requireNonNull(tietMiddlename.getText()).toString().trim().equals("") | !Objects.requireNonNull(tietChildHome.getText()).toString().trim().equals("") |
        !Objects.requireNonNull(tietEmail.getText()).toString().trim().equals("") | !Objects.requireNonNull(metPhone.getText()).toString().trim().equals("") |
        !Objects.requireNonNull(tietRegistrationAddress.getText()).toString().trim().equals("") | !Objects.requireNonNull(tietFactualAddress.getText()).toString().trim().equals("") |
        !Objects.requireNonNull(tietPassword.getText()).toString().trim().equals("") | !Objects.requireNonNull(tietConfirmPassword.getText()).toString().trim().equals("") |
        !spTypeOfFlat.getSelectedItem().toString().equals("-") | !spDate.getSelectedItem().toString().equals("День") |
        !spMonth.getSelectedItem().toString().equals("Месяц") | !spYear.getSelectedItem().toString().equals("Год") |
        !spSex.getSelectedItem().toString().equals("-") | !actvSubject.getText().toString().trim().equals("") |
        !actvCity.getText().toString().trim().equals("") | cbAgreement.isChecked())
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "register_edit_data_dialog");
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void confirmInput() {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnRegister.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Profile profile = new Profile(this, tilSurname, tilName, tilMiddlename, tilEmail, tilPhone, tilPassword, tilConfirmPassword,
                                      tilSubject, tilCity, cbAgreement, spSex, tvSex, spDate, spMonth, spYear, tvDateOfBirth);
        if (checkInternetConnection.isNetworkConnected())
        {
            if (profile.CheckFieldsRegister())
            {
                displayAlert("input_failed",
                             getResources().getString(R.string.something_went_wrong),
                             getResources().getString(R.string.check_data));
            }
            else
            {
                if (!Objects.requireNonNull(tilPassword.getEditText()).getText().toString().trim().equals(Objects.requireNonNull(tilConfirmPassword.getEditText()).getText().toString().trim())) {
                    displayAlert("input_error",
                                 getResources().getString(R.string.something_went_wrong),
                                 getResources().getString(R.string.check_passwords));
                } else {
                    //запуск ProgressBar
                    progressBar.setVisibility(View.VISIBLE);
                    //---
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.register_url,
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
                            params.put("surname", Objects.requireNonNull(tietSurname.getText()).toString().trim());
                            params.put("name", Objects.requireNonNull(tietName.getText()).toString().trim());
                            params.put("middlename", Objects.requireNonNull(tietMiddlename.getText()).toString().trim());
                            params.put("child_home", Objects.requireNonNull(tietChildHome.getText()).toString().trim());
                            params.put("email", Objects.requireNonNull(tietEmail.getText()).toString().trim());
                            params.put("password",  Objects.requireNonNull(tietPassword.getText()).toString().trim());
                            params.put("phone_number", Objects.requireNonNull(metPhone.getText()).toString().trim());
                            params.put("city", actvCity.getText().toString().trim());
                            params.put("subject_of_country", actvSubject.getText().toString().trim());
                            params.put("registration_address", Objects.requireNonNull(tietRegistrationAddress.getText()).toString().trim());
                            params.put("factual_address", Objects.requireNonNull(tietFactualAddress.getText()).toString().trim());
                            params.put("type_of_flat", spTypeOfFlat.getSelectedItem().toString().trim());
                            params.put("sex", spSex.getSelectedItem().toString().trim());
                            params.put("date_of_born", spDate.getSelectedItem().toString().trim());
                            params.put("month_of_born", spMonth.getSelectedItem().toString().trim());
                            params.put("year_of_born", spYear.getSelectedItem().toString().trim());
                            params.put("agreement", "success");
                            return params;
                        }
                    };
                    MySingleton.getInstance(Register.this).addToRequestque(stringRequest);
                }
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
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("surname", Objects.requireNonNull(tietSurname.getText()).toString().trim());
        outState.putString("name", Objects.requireNonNull(tietName.getText()).toString().trim());
        outState.putString("middlename", Objects.requireNonNull(tietMiddlename.getText()).toString().trim());
        outState.putString("child_home", Objects.requireNonNull(tietChildHome.getText()).toString().trim());
        outState.putString("email", Objects.requireNonNull(tietEmail.getText()).toString().trim());
        outState.putString("password", Objects.requireNonNull(tietPassword.getText()).toString().trim());
        outState.putString("confirm_password", Objects.requireNonNull(tietConfirmPassword.getText()).toString().trim());
        outState.putString("phone", Objects.requireNonNull(metPhone.getText()).toString().trim());
        outState.putString("registration_address", Objects.requireNonNull(tietRegistrationAddress.getText()).toString().trim());
        outState.putString("factual_address", Objects.requireNonNull(tietFactualAddress.getText()).toString().trim());
        outState.putString("subject_of_Russian_Federation", actvSubject.getText().toString().trim());
        outState.putString("city_of_Russian_Federation", actvCity.getText().toString().trim());
        outState.putInt("type_of_flat", spTypeOfFlat.getSelectedItemPosition());
        outState.putInt("day", spDate.getSelectedItemPosition());
        outState.putInt("month", spMonth.getSelectedItemPosition());
        outState.putInt("year", spYear.getSelectedItemPosition());
        outState.putInt("sex", spSex.getSelectedItemPosition());
        outState.putBoolean("agreement", cbAgreement.isChecked());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        tietSurname.setText(savedInstanceState.getString("surname"));
        tietName.setText(savedInstanceState.getString("name"));
        tietMiddlename.setText(savedInstanceState.getString("middlename"));
        tietChildHome.setText(savedInstanceState.getString("child_home"));
        tietEmail.setText(savedInstanceState.getString("email"));
        tietPassword.setText(savedInstanceState.getString("password"));
        tietConfirmPassword.setText(savedInstanceState.getString("confirm_password"));
        metPhone.setText(savedInstanceState.getString("phone"));
        tietRegistrationAddress.setText(savedInstanceState.getString("registration_address"));
        tietFactualAddress.setText(savedInstanceState.getString("factual_address"));
        actvSubject.setText(savedInstanceState.getString("subject_of_Russian_Federation"));
        actvCity.setText(savedInstanceState.getString("city_of_Russian_Federation"));
        spTypeOfFlat.setSelection(savedInstanceState.getInt("type_of_flat"));
        spDate.setSelection(savedInstanceState.getInt("day"));
        spMonth.setSelection(savedInstanceState.getInt("month"));
        spYear.setSelection(savedInstanceState.getInt("year"));
        spSex.setSelection(savedInstanceState.getInt("sex"));
        cbAgreement.setChecked(savedInstanceState.getBoolean("agreement"));
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        if(code.equals("reg_success"))
        {
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "register_dialog_finish");
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "register_dialog");
        }
    }
}
