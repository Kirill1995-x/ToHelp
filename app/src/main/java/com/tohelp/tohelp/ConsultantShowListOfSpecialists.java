package com.tohelp.tohelp;

import android.content.Intent;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tohelp.tohelp.lists.ContactAdapter;
import com.tohelp.tohelp.lists.Contacts;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsultantShowListOfSpecialists extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.progressBarConsultantPersonal)
    ProgressBar progressBar;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.llListOfSpecialists)
    LinearLayout llListOfSpecialists;
    ContactAdapter contactAdapter;
    String parameter_for_request=null;
    String id_of_specialist, type_of_specialist, surname, name, middlename, call_hours, phone, name_of_photo;
    CheckInternetConnection checkInternetConnection;
    String type_of_request;
    String type_of_request_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_show_list_of_specialists);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //создание адаптера
        contactAdapter = new ContactAdapter(this);

        //обработка нажатия на элемент
        tvTryRequest.setOnClickListener(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //прием данных из Bundle
        Bundle arguments=getIntent().getExtras();
        if (arguments != null) {
            type_of_request=arguments.getString("type_of_request");
            type_of_request_call=arguments.getString("search_specialist");
        }

        if(!checkInternetConnection.isNetworkConnected())
        {
            llListOfSpecialists.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else {
            if (savedInstanceState == null) {
                request_specialist();
            }
            else
            {
                Parcelable[] contacts_array = savedInstanceState.getParcelableArray("contacts");

                if (contacts_array != null) {
                    if (contacts_array.length == 0) {
                        request_specialist();
                    }
                    else
                    {
                        llListOfSpecialists.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                        for (int i = 0; i < contacts_array.length; i++) {
                            contactAdapter.add((Contacts) contacts_array[i]);
                        }
                    }
                }
            }
        }

        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts contacts=contactAdapter.getItem(position);
                Intent intent=new Intent(ConsultantShowListOfSpecialists.this, ConsultantSendRequest.class);
                intent.putExtra("specialist_contacts", contacts);
                intent.putExtra("search_specialist", type_of_request_call);
                intent.putExtra("type_of_request", type_of_request);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvTryRequest:
                if (checkInternetConnection.isNetworkConnected())
                {
                    request_specialist();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Contacts[]contacts_array=new Contacts[contactAdapter.getCount()];
        for(int i=0; i<contactAdapter.getCount();i++)
        {
            contacts_array[i]=contactAdapter.getItem(i);
        }
        outState.putInt("length_of_array", contacts_array.length);
        outState.putParcelableArray("contacts", contacts_array);
    }

    private String create_url()
    {
        String id = null;
        try {
            id = Encryption.decrypt(getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE).getString("shared_id",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String subject = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                         .getString("shared_subject","").replace(" ","%20");
        String access_token = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE)
                              .getString("shared_access_token","");
        return Variable.request_list_of_specialists_main_url+subject+
               Variable.request_list_of_specialists_first_add_url+type_of_request+
               Variable.request_list_of_specialists_second_add_url+id+
               Variable.request_list_of_specialists_third_add_url+access_token;
    }

    private void request_specialist()
    {
        contactAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        llListOfSpecialists.setVisibility(View.VISIBLE);

        JsonObjectRequest getRequest=new JsonObjectRequest(Request.Method.GET, create_url(), parameter_for_request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject JO = jsonArray.getJSONObject(i);
                                id_of_specialist=JO.getString("id");
                                type_of_specialist=JO.getString("type_of_specialist");
                                surname=JO.getString("surname");
                                name=JO.getString("name");
                                middlename=JO.getString("middlename");
                                call_hours=JO.getString("call_hours");
                                phone=JO.getString("phone_number");
                                name_of_photo=JO.getString("name_of_photo");
                                Contacts contacts=new Contacts(id_of_specialist, type_of_specialist, surname, name, middlename, call_hours, phone, name_of_photo);
                                contactAdapter.add(contacts);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        MySingleton.getInstance(ConsultantShowListOfSpecialists.this).addToRequestque(getRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
