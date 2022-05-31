package com.tohelp.tohelp;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
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


public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.btnForgotPassword)
    Button btForgorPassword;
    @BindView(R.id.tilForgotPasswordEmail)
    TextInputLayout tilForgotPasswordEmail;
    @BindView(R.id.tietForgotPasswordEmail)
    TextInputEditText tietForgotPasswordEmail;
    @BindView(R.id.progressBarForgotPassword)
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //обработка нажатия на элемент
        btForgorPassword.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnForgotPassword:
                confirmInput();
                break;
            default:
                break;
        }
    }

    private boolean validateEmail()
    {
        String emailInput= Objects.requireNonNull(tilForgotPasswordEmail.getEditText()).getText().toString().trim();
        if (emailInput.isEmpty())
        {
            tilForgotPasswordEmail.setError("Введите почту");
            return false;
        }
        else
        {
            tilForgotPasswordEmail.setError(null);
            return true;
        }
    }

    public void confirmInput() {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btForgorPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (checkInternetConnection.isNetworkConnected())
        {
            if (!validateEmail())
            {
                try
                {
                    displayAlert("password_sent_failed",
                                 getResources().getString(R.string.something_went_wrong),
                                 getResources().getString(R.string.check_data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.forgot_password_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try
                                {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    progressBar.setVisibility(View.GONE);
                                    displayAlert(jsonObject.getString("code"),
                                                 jsonObject.getString("title"),
                                                 jsonObject.getString("message"));
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
                        params.put("email", Objects.requireNonNull(tietForgotPasswordEmail.getText()).toString());
                        return params;
                    }
                };
                MySingleton.getInstance(ForgotPassword.this).addToRequestque(stringRequest);
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
        outState.putString("email", Objects.requireNonNull(tietForgotPasswordEmail.getText()).toString().trim());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        tietForgotPasswordEmail.setText(savedInstanceState.getString("email"));
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        if(code.equals("password_was_sent_to_email"))
        {
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "forgot_password_dialog_finish");
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "forgot_password_dialog");
        }
    }
}
