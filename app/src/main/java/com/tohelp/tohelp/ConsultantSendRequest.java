package com.tohelp.tohelp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.lists.Contacts;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultantSendRequest extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvFullNameMessage)
    TextView tvFullName;
    @BindView(R.id.send_request)
    Button btnSendRequest;
    @BindView(R.id.online_consultant_text_of_message)
    EditText etTextOfMessage;
    @BindView(R.id.imageInMessage)
    CircleImageView imageOfAccount;
    @BindView(R.id.progressBarMessage)
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    CheckInternetConnection checkInternetConnection;
    String fullname, surname, name, middlename, call_hours, phone, id_of_specialist, type_of_specialist;
    String type_of_request_call, type_of_request, name_of_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_send_request);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //установка toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        //добавление стрелки "Вверх"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //получение файла со всеми данными о выпускнике
        sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);

        //обработка нажатия на элемент
        btnSendRequest.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //получение  и установка ФИО
        Bundle arguments=getIntent().getExtras();
        if (arguments != null)
        {
            type_of_request_call=arguments.getString("search_specialist");
            type_of_request = arguments.getString("type_of_request");
            if(Objects.requireNonNull(type_of_request_call).equals("choose_specialist"))
            {
                Contacts contacts=(Contacts)getIntent().getParcelableExtra("specialist_contacts");
                if(contacts!=null)
                {
                    id_of_specialist = contacts.getIdOfSpecialist();
                    type_of_specialist = contacts.getTypeOfSpecialist();
                    surname = contacts.getSurname();
                    name = contacts.getName();
                    middlename = contacts.getMiddlename();
                    call_hours = contacts.getCallHours();
                    phone = contacts.getMobile();
                    name_of_photo = contacts.getNameOfPhoto();
                    fullname = surname + " " + name;
                    Picasso.get()
                            .load(contacts.getUrlOfPhoto())
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imageOfAccount);
                }
            }
            else
            {
                fullname = "Онлайн-консультант";
            }
        }
        tvFullName.setText(fullname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send_request:
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSendRequest.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if(checkInternetConnection.isNetworkConnected())
                {
                    if (type_of_request_call.equals("choose_specialist")) {
                        send_request_to_selected_specialist();
                    } else {
                        send_request_to_specialists();
                    }
                }
                else
                {
                    displayAlert("internet_connection_failed",
                            getResources().getString(R.string.error_connection),
                            getResources().getString(R.string.check_connection));
                }
                break;
            default:
                break;
        }
    }

    private void send_request_to_selected_specialist()
    {
        //запуск ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.request_call_selected_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String title = jsonObject.getString("title");
                            String message = jsonObject.getString("message");
                            progressBar.setVisibility(View.GONE);
                            displayAlert(code, title, message);
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
                    params.put("id_of_user", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                params.put("type_of_request", type_of_request);
                params.put("message", etTextOfMessage.getText().toString().trim());
                params.put("id_of_specialist", id_of_specialist);
                params.put("subject_of_country", sharedPreferences.getString("shared_subject",""));
                return params;
            }
        };
        MySingleton.getInstance(ConsultantSendRequest.this).addToRequestque(stringRequest);
    }

    private void send_request_to_specialists()
    {
        //запуск ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.request_call_all_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String title = jsonObject.getString("title");
                            String message = jsonObject.getString("message");
                            progressBar.setVisibility(View.GONE);
                            displayAlert(code, title, message);
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
                    params.put("id_of_user", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                params.put("type_of_request", type_of_request);
                params.put("message", etTextOfMessage.getText().toString().trim());
                params.put("subject_of_country", sharedPreferences.getString("shared_subject",""));
                return params;
            }
        };
        MySingleton.getInstance(ConsultantSendRequest.this).addToRequestque(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void displayAlert (String code, String title, String message)
    {
        if(code.equals("request_call_success"))
        {
            //подсчет количества нажатий на кнопку
            SharedPreferences preferencesNotification = getSharedPreferences(Variable.APP_NOTIFICATIONS, MODE_PRIVATE);
            int count_of_click = preferencesNotification.getInt("count_of_click", Variable.MIN_COUNT_OF_CLICK);
            if(count_of_click<Variable.COUNT_OF_CLICK)
            {
                preferencesNotification.edit().putInt("count_of_click", count_of_click+1).apply();
            }
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "online_consultant_dialog");
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "online_consultant_dialog");
        }
    }
}
