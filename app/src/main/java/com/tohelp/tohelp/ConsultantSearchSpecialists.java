package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogSimple;
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

public class ConsultantSearchSpecialists extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.btnRequestCall)
    Button btnRequestCallSpecialist;
    @BindView(R.id.spRequestChooseHelp)
    Spinner spRequestChooseHelp;
    @BindView(R.id.spRequestChooseSpecialist)
    Spinner spRequestChooseSpecialist;
    @BindView(R.id.tvRequestChooseHelp)
    TextView tvRequestChooseHelp;
    @BindView(R.id.tvRequestChooseSpecialist)
    TextView tvRequestChooseSpecialist;
    @BindView(R.id.tvSubjectOfCountry)
    TextView tvSubjectOfCountry;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.imgCreateRequest)
    ImageView imgCreateRequest;
    @BindView(R.id.progressBarRequestCall)
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;
    String [] array_of_request;
    String [] array_type_of_request;
    String selected_type="";//переменная для хранения типа задания

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_search_specialists);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //Проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //обработка нажатия на элементы
        btnRequestCallSpecialist.setOnClickListener(this);
        imgCreateRequest.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //установка заголовка и местоположения в зависимости от нажатой кнопки в FragmentSecond
        tvSubjectOfCountry.setText(getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE).getString("shared_subject",""));
        tvCity.setText(getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE).getString("shared_city",""));

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //Ввод адаптера для работы с массивами
        array_of_request=getResources().getStringArray(R.array.array_request);
        array_type_of_request=getResources().getStringArray(R.array.array_type_of_request);
        //адаптер для Spinner
        ArrayAdapter adapter_choose_help=ArrayAdapter.createFromResource(this, R.array.array_request, R.layout.spinner_layout);
        ArrayAdapter adapter_choose_specialist=ArrayAdapter.createFromResource(this, R.array.array_choose_specialist, R.layout.spinner_layout);
        adapter_choose_help.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_choose_specialist.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spRequestChooseHelp.setAdapter(adapter_choose_help);
        spRequestChooseSpecialist.setAdapter(adapter_choose_specialist);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRequestCall:
                confirmInput();
                break;
            case R.id.imgCreateRequest:
                displayAlert("code",
                             getResources().getString(R.string.create_request_hint),
                             getResources().getString(R.string.create_request_message));
                break;
            default:
                break;
        }
    }

    public void confirmInput()
    {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnRequestCallSpecialist.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(checkInternetConnection.isNetworkConnected())
        {
            if(spRequestChooseHelp.getSelectedItem().toString().trim().equals("-"))
            {
                tvRequestChooseHelp.setError("Не выбран тип запроса");
                displayAlert("code",
                             getResources().getString(R.string.something_went_wrong),
                             getResources().getString(R.string.check_data));
            }
            else
            {
                tvRequestChooseHelp.setError(null);
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //получение токена задания
                for (int i=0; i<array_of_request.length; i++)
                {
                    if(spRequestChooseHelp.getSelectedItem().toString().trim().equals(array_of_request[i]))
                    {
                        selected_type=array_type_of_request[i];
                        break;
                    }
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.request_search_specialist_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    progressBar.setVisibility(View.GONE);
                                    if (code.equals("find_specialist_success"))
                                    {
                                        String type_of_request_call=(spRequestChooseSpecialist.getSelectedItem().toString().trim().equals("Да"))?
                                                                "choose_specialist":"all_specialists";
                                        //переход на соответствующую Activity в зависимости от type_of_request_call
                                        Intent intent=(type_of_request_call.equals("choose_specialist"))?
                                                new Intent(ConsultantSearchSpecialists.this, ConsultantShowListOfSpecialists.class):
                                                new Intent(ConsultantSearchSpecialists.this, ConsultantSendRequest.class);
                                        intent.putExtra("type_of_request", selected_type);
                                        intent.putExtra("search_specialist", type_of_request_call);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (code.equals("find_specialist_failed"))
                                    {
                                        displayAlert(code,
                                                     jsonObject.getString("title"),
                                                     jsonObject.getString("message"));
                                    }
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
                        try {
                            params.put("id", Encryption.decrypt(getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                                                                        .getString("shared_id","")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        params.put("access_token", getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                                                   .getString("shared_access_token",""));
                        params.put("subject", getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                                              .getString("shared_subject",""));
                        params.put("type_of_request", selected_type);
                        return params;
                    }
                };
                MySingleton.getInstance(ConsultantSearchSpecialists.this).addToRequestque(stringRequest);
            }
        }
        else
        {
            displayAlert("code",
                         getResources().getString(R.string.error_connection),
                         getResources().getString(R.string.check_connection));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("choose_help", spRequestChooseHelp.getSelectedItemPosition());
        outState.putInt("choose_specialist", spRequestChooseSpecialist.getSelectedItemPosition());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        spRequestChooseHelp.setSelection(savedInstanceState.getInt("choose_help"));
        spRequestChooseSpecialist.setSelection(savedInstanceState.getInt("choose_specialist"));
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
    public void displayAlert (String code, String title, String message)
    {
        DialogSimple dialog=new DialogSimple();
        DialogSimple.title=title;
        DialogSimple.message=message;
        dialog.show(getSupportFragmentManager(), "request_call_dialog");
    }
}
