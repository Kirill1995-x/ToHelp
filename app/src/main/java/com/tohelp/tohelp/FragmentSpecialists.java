package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tohelp.tohelp.lists.ContactsRequest;
import com.tohelp.tohelp.lists.ContactsRequestAdapter;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentSpecialists extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srlListOfRequests)
    SwipeRefreshLayout srlListOfRequests;
    @BindView(R.id.spDefineSpecialist)
    Spinner spDefineSpecialist;
    @BindView(R.id.idSendRequest)
    FloatingActionButton fabSendRequest;
    @BindView(R.id.progressBarSecondFragment)
    ProgressBar progressBar;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.rlListOfRequests)
    RelativeLayout rlListOfRequests;
    @BindView(R.id.list_navigator_consultant)
    ListView variable_array_list_of_specialist;
    SharedPreferences preferences;
    SharedPreferences preferences_type_of_request;
    CheckInternetConnection checkInternetConnection;
    String parameter_for_request=null;
    String id_of_request, type_of_request, status, id_of_specialist, type_of_specialist, surname,
           name, middlename, workhours, workphone, text_of_request, name_of_photo;
    ContactsRequestAdapter contactsRequestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_specialists, container, false);

        //настройка ButterKnife
        ButterKnife.bind(this, v);

        //флаг для фрагмента Онлайн-консультант
        preferences= requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putString("flag", "second_fragment").apply();

        //Изменение стиля Spinner
        ArrayAdapter adapter_for_define_specialist = ArrayAdapter.createFromResource(requireActivity(), R.array.array_specialist_define, R.layout.spinner_layout);
        adapter_for_define_specialist.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spDefineSpecialist.setAdapter(adapter_for_define_specialist);
        preferences_type_of_request=getActivity().getSharedPreferences(Variable.APP_NOTIFICATIONS, Context.MODE_PRIVATE);

        //создание адаптера
        contactsRequestAdapter=new ContactsRequestAdapter(getActivity());

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(getActivity());

        //выбор типа запросов
        spDefineSpecialist.setSelection(preferences_type_of_request.getInt("type_of_request", 0));

        //обработка нажатия на элементы
        tvTryRequest.setOnClickListener(this);
        fabSendRequest.setOnClickListener(this);

        //обработка обновления списка
        srlListOfRequests.setOnRefreshListener(this);

        variable_array_list_of_specialist.setAdapter(contactsRequestAdapter);
        variable_array_list_of_specialist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ContactsRequest contactsRequest=contactsRequestAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ConsultantCompleteRequest.class);
                intent.putExtra("contactsRequest", contactsRequest);
                startActivity(intent);
            }
        });
        spDefineSpecialist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences_type_of_request.edit().putInt("type_of_request", position).apply();
                confirmInput();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});

        return v;
    }

    @Override
    public void onRefresh() {
        srlListOfRequests.setRefreshing(false);
        confirmInput();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.tvTryRequest:
                confirmInput();
                break;
            case R.id.idSendRequest:
                intent=new Intent(getActivity(), ConsultantSearchSpecialists.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private String createURL(String main_url)
    {
        SharedPreferences preferences= requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
        String id=null;
        try {
            id= Encryption.decrypt(preferences.getString("shared_id",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String access_token = preferences.getString("shared_access_token","");
        return main_url+id+Variable.requests_specialist_access_token_url+access_token;
    }

    private void confirmInput()
    {
        if(!new CheckInternetConnection(getActivity()).isNetworkConnected())
        {
            rlListOfRequests.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            switch (preferences_type_of_request.getInt("type_of_request",0))
            {
                case 0:
                    requests_define_specialist();
                    break;
                case 1:
                    requests_not_define_specialist();
                    break;
                default:
                    break;
            }
        }
    }

    private void requests_define_specialist()
    {
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        rlListOfRequests.setVisibility(View.VISIBLE);
        contactsRequestAdapter.clear();

        JsonObjectRequest getRequest=new JsonObjectRequest(Request.Method.GET, createURL(Variable.requests_specialist_define_url), parameter_for_request,
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
                                id_of_request=JO.getString("id");
                                type_of_request=JO.getString("type_of_request");
                                status=JO.getString("status");
                                surname =JO.getString("surname");
                                name=JO.getString("name");
                                middlename=JO.getString("middlename");
                                workhours=JO.getString("call_hours");
                                workphone=JO.getString("phone_number");
                                text_of_request=JO.getString("message_of_user");
                                id_of_specialist=JO.getString("id_of_specialist");
                                type_of_specialist=JO.getString("type_of_specialist");
                                name_of_photo=JO.getString("photo_of_specialist");
                                ContactsRequest contactsRequest=new ContactsRequest(id_of_request, type_of_request, status, id_of_specialist, type_of_specialist,
                                        surname, name, middlename, workhours, workphone, text_of_request, name_of_photo);
                                contactsRequestAdapter.add(contactsRequest);
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
        MySingleton.getInstance(getActivity()).addToRequestque(getRequest);
    }

    private void requests_not_define_specialist()
    {
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        rlListOfRequests.setVisibility(View.VISIBLE);
        contactsRequestAdapter.clear();

        JsonObjectRequest getRequest=new JsonObjectRequest(Request.Method.GET, createURL(Variable.requests_specialist_not_define_url), parameter_for_request,
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
                                id_of_request=JO.getString("id");
                                type_of_request=JO.getString("type_of_request");
                                surname ="";
                                name="";
                                middlename="";
                                status=JO.getString("status");
                                workhours="";
                                workphone="";
                                text_of_request=JO.getString("message_of_user");
                                id_of_specialist="";
                                type_of_specialist="";
                                name_of_photo="";
                                ContactsRequest contactsRequest=new ContactsRequest(id_of_request, type_of_request, status, id_of_specialist, type_of_specialist,
                                        surname, name, middlename, workhours, workphone, text_of_request, name_of_photo);
                                contactsRequestAdapter.add(contactsRequest);
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
        MySingleton.getInstance(getActivity()).addToRequestque(getRequest);
    }
}