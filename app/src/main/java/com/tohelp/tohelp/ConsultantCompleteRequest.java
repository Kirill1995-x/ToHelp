package com.tohelp.tohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.lists.ContactsRequest;
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

public class ConsultantCompleteRequest extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.specialist_fullname)
    TextView tvFullname;
    @BindView(R.id.speciality_of_specialist)
    TextView tvSpeciality;
    @BindView(R.id.specialist_workhours)
    TextView tvWorkhours;
    @BindView(R.id.specialist_workphone)
    TextView tvWorkphone;
    @BindView(R.id.type_of_help)
    TextView tvTypeOfHelp;
    @BindView(R.id.specialist_text_of_request)
    TextView tvTextOfRequest;
    @BindView(R.id.btnSuccessSolution)
    Button btnSolution;
    @BindView(R.id.btnFailedSolution)
    Button btnFailed;
    @BindView(R.id.progressBarConsultantStatus)
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;
    String id_of_request, surname, name, middlename, type_of_specialist, workhours, workphone, text_of_request, url_of_photo;
    int type_of_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_complete_request);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //обработка нажатия на элементы
        btnSolution.setOnClickListener(this);
        btnFailed.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        ContactsRequest contactsRequest=(ContactsRequest)getIntent().getParcelableExtra("contactsRequest");
        if (contactsRequest!=null) {
            surname=contactsRequest.getSurname();
            name=contactsRequest.getName();
            middlename=contactsRequest.getMiddlename();
            id_of_request=contactsRequest.getIdOfRequest();
            type_of_request=contactsRequest.getRequest();
            type_of_specialist=contactsRequest.getTypeOfSpecialist();
            workhours=contactsRequest.getWorkhours();
            workphone=contactsRequest.getWorkphone();
            text_of_request=contactsRequest.getTextOfRequest();
            url_of_photo=contactsRequest.getUrlOfPhoto();
        }

        //заполнение активити из результатов запроса
        ParametersOfRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSuccessSolution:
                change_status_of_request("3");
                break;
            case R.id.btnFailedSolution:
                change_status_of_request("4");
                break;
            default:
                break;
        }
    }

    private void ParametersOfRequest()
    {
        //получение ФИО специалиста
        String Fullname=(!surname.isEmpty()|!name.isEmpty()|!middlename.isEmpty())?(surname+" "+name+" "+middlename):"не определен";
        tvFullname.setText(Fullname);
        //получение специализации специалиста
        tvSpeciality.setText(type_of_specialist);
        //получение рабочих часов специалиста
        String WorkHours=(workhours.isEmpty())?"-":workhours;
        tvWorkhours.setText(WorkHours);
        //получение рабочего номера телефона специалиста
        String WorkPhone=(workphone.isEmpty())?"-":("+7"+workphone);
        tvWorkphone.setText(WorkPhone);
        //получение типа требуемой помощи
        tvTypeOfHelp.setText(type_of_request);
        //получение текста запроса
        String TextOfRequest=(text_of_request.isEmpty())?"-":text_of_request;
        tvTextOfRequest.setText(TextOfRequest);
    }

    private void change_status_of_request(String status)
    {
        if (checkInternetConnection.isNetworkConnected())
        {
            //запуск ProgressBar
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.request_complete_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                progressBar.setVisibility(View.GONE);
                                if (code.equals("change_status_failed"))
                                {
                                    displayAlert(code, jsonObject.getString("title"), jsonObject.getString("message"));
                                }
                                else if (code.equals("change_status_success"))
                                {
                                    finish();
                                    //подсчет количества нажатий на кнопку
                                    SharedPreferences preferencesNotification = getSharedPreferences(Variable.APP_NOTIFICATIONS, MODE_PRIVATE);
                                    int count_of_click = preferencesNotification.getInt("count_of_click", Variable.MIN_COUNT_OF_CLICK);
                                    if(count_of_click<Variable.COUNT_OF_CLICK)
                                    {
                                        preferencesNotification.edit().putInt("count_of_click", count_of_click+1).apply();
                                    }
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
                    try {
                        params.put("id_of_user", Encryption.decrypt(getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                                                 .getString("shared_id","")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    params.put("access_token", getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                                               .getString("shared_access_token",""));
                    params.put("id", id_of_request);
                    params.put("status", status);
                    return params;
                }
            };
            MySingleton.getInstance(ConsultantCompleteRequest.this).addToRequestque(stringRequest);
        }
        else
        {
            displayAlert("code", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        DialogSimple dialog=new DialogSimple();
        DialogSimple.title=title;
        DialogSimple.message=message;
        dialog.show(getSupportFragmentManager(), "change_status_dialog");
    }
}
