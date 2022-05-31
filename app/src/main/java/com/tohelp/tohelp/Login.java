package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogCheckAge;
import com.tohelp.tohelp.dialogs.DialogRestoreAccount;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.tilLogin)
    TextInputLayout tilLogin;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tietLogin)
    TextInputEditText tietLogin;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvForgotPassword)
    TextView tvforgotpassword;
    @BindView(R.id.tvRegisterLink)
    TextView tvRegisterLink;
    @BindView(R.id.progressBarLogin)
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    StringBuilder phone_number;//переменная для хранения номера телефона
    boolean isEmail;//переменная для хранения результатов проверки логина

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //проверка подключения к Интернету
        checkInternetConnection=new CheckInternetConnection(this);

        //получение файла для записи данных с сервера
        sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);

        //обработка нажатия на элементы
        tvRegisterLink.setOnClickListener(this);
        tvforgotpassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.tvRegisterLink:
                DialogCheckAge dialogCheckAge = new DialogCheckAge();
                dialogCheckAge.setCancelable(false);
                dialogCheckAge.show(getSupportFragmentManager(), "check_age_dialog");
                break;
            case R.id.tvForgotPassword:
                intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                confirmInput();
                break;
            default:
                break;
        }
    }

    private boolean checkFieldLogin() {
        char[] array = Objects.requireNonNull(tietLogin.getText()).toString().trim().toCharArray();

        isEmail = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '@') {
                isEmail = true;
                return true;
            }
        }

        ArrayList<Character> array_for_phone_number = new ArrayList<Character>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < Variable.array_number.length; j++) {
                if (array[i] == Variable.array_number[j]) {
                    array_for_phone_number.add(array[i]);
                    break;
                }
            }
        }

        if (array_for_phone_number.size() != 11) {
            tilLogin.setError("Поле заполнено некорректно");
            return false;
        } else {
            array_for_phone_number.remove(0);
            array_for_phone_number.add(0, '(');
            array_for_phone_number.add(4, ')');
            array_for_phone_number.add(5, ' ');
            array_for_phone_number.add(9, ' ');
            array_for_phone_number.add(12, ' ');
            phone_number = new StringBuilder();
            for (int i = 0; i < array_for_phone_number.size(); i++) {
                phone_number.append(array_for_phone_number.get(i));
            }
            return true;
        }
    }

    private boolean validateLogin()
    {
        String loginInput= Objects.requireNonNull(tietLogin.getText()).toString().trim();
        if (loginInput.isEmpty())
        {
            tilLogin.setError("Введите почту или номер телефона");
            return false;
        }
        else
        {
            tilLogin.setError(null);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String passwordInput= Objects.requireNonNull(tietPassword.getText()).toString().trim();
        if (passwordInput.isEmpty())
        {
            tilPassword.setError("Введите пароль");
            return false;
        }
        else
        {
            tilPassword.setError(null);
            return true;
        }
    }

    public void confirmInput() {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnLogin.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (checkInternetConnection.isNetworkConnected()) {
            if (!validateLogin() | !validatePassword())
            {
                displayAlert("check_data",
                             getResources().getString(R.string.something_went_wrong),
                             getResources().getString(R.string.check_data));
            }
            else if(!checkFieldLogin())
            {
                displayAlert("check_data",
                             getResources().getString(R.string.something_went_wrong),
                             getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.login_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("account_deleted")) {
                                        progressBar.setVisibility(View.GONE);
                                        DialogRestoreAccount dialog = new DialogRestoreAccount();
                                        DialogRestoreAccount.email = jsonObject.getString("email");
                                        dialog.show(getSupportFragmentManager(), "login_dialog");
                                    }
                                    else if(code.equals("login_success"))
                                    {
                                        Intent intent = new Intent(Login.this, Main.class);
                                        editor = sharedPreferences.edit();
                                        try
                                        {
                                            editor.putString("shared_id", Encryption.encrypt(jsonObject.getString("id")));
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        editor.putString("shared_surname", jsonObject.getString("surname"));
                                        editor.putString("shared_name", jsonObject.getString("name"));
                                        editor.putString("shared_middlename", jsonObject.getString("middlename"));
                                        editor.putString("shared_child_home", jsonObject.getString("child_home"));
                                        editor.putString("shared_email", jsonObject.getString("email"));
                                        editor.putString("shared_phone_number", jsonObject.getString("phone_number"));
                                        editor.putString("shared_city", jsonObject.getString("city"));
                                        editor.putString("shared_subject", jsonObject.getString("subject_of_country"));
                                        editor.putString("shared_registration_address", jsonObject.getString("registration_address"));
                                        editor.putString("shared_factual_address", jsonObject.getString("factual_address"));
                                        editor.putString("shared_type_of_flat", jsonObject.getString("type_of_flat"));
                                        editor.putString("shared_sex", jsonObject.getString("sex"));
                                        editor.putString("shared_date_of_born", jsonObject.getString("date_of_born"));
                                        editor.putString("shared_month_of_born", jsonObject.getString("month_of_born"));
                                        editor.putString("shared_year_of_born", jsonObject.getString("year_of_born"));
                                        editor.putString("shared_name_of_photo", jsonObject.getString("name_of_photo"));
                                        editor.putString("shared_access_token", jsonObject.getString("access_token"));
                                        editor.apply();
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        displayAlert(code,
                                                     jsonObject.getString("title"),
                                                     jsonObject.getString("message"));
                                    }
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
                        if(isEmail) {
                            params.put("login", Objects.requireNonNull(tietLogin.getText()).toString());
                        }
                        else
                        {
                            params.put("login", phone_number.toString());
                        }
                        params.put("password", Objects.requireNonNull(tietPassword.getText()).toString());
                        return params;
                    }
                };
                MySingleton.getInstance(Login.this).addToRequestque(stringRequest);
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
        outState.putString("login", Objects.requireNonNull(tietLogin.getText()).toString().trim());
        outState.putString("password", Objects.requireNonNull(tietPassword.getText()).toString().trim());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        tietLogin.setText(savedInstanceState.getString("login"));
        tietPassword.setText(savedInstanceState.getString("password"));
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        DialogSimple dialog = new DialogSimple();
        DialogSimple.title = title;
        DialogSimple.message = message;
        dialog.show(getSupportFragmentManager(), "login_dialog");
    }
}