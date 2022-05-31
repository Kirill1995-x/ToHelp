package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tohelp.tohelp.lists.Education;
import com.tohelp.tohelp.lists.EducationAdapter;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentCourses extends Fragment implements View.OnClickListener {

    @BindView(R.id.lvEighthFragment)
    ListView lvEducation;
    @BindView(R.id.progressBarEighthFragment)
    ProgressBar progressBar;
    @BindView(R.id.llListOfEducation)
    LinearLayout llListOfEducation;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    EducationAdapter educationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_courses, container, false);
        //настройка ButterKnife
        ButterKnife.bind(this, v);
        //флаг для фрагментов кроме Навигатора и Онлайн-консультанта
        requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                .putString("flag", "sixth_fragment").apply();

        //создание адаптера
        educationAdapter = new EducationAdapter(requireActivity());

        //обработка нажатия на элемент
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(getActivity()).isNetworkConnected())
        {
            llListOfEducation.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else {
            if(savedInstanceState == null) {
                showListOfEducation();
            }
            else {
                Parcelable[] array_of_education = savedInstanceState.getParcelableArray("education");
                if (array_of_education != null && array_of_education.length != 0) {
                    for (int i = 0; i < array_of_education.length; i++) {
                        educationAdapter.add((Education) array_of_education[i]);
                    }
                }
            }
        }

        lvEducation.setAdapter(educationAdapter);
        lvEducation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Education education = educationAdapter.getItem(position);
                if (education != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(education.getUrl()));
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(getActivity()).isNetworkConnected()) {
                    showListOfEducation();
                }
                break;
            default:
                break;
        }
    }

    private void showListOfEducation()
    {
        educationAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        llListOfEducation.setVisibility(View.VISIBLE);

        String parameter_for_request = null;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Variable.request_education_url, parameter_for_request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Education education = new Education(jsonObject.getInt("id"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("url"),
                                        jsonObject.getString("description"));
                                educationAdapter.add(education);
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
                }
        );
        MySingleton.getInstance(getActivity()).addToRequestque(request);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Education []array_of_education = new Education[educationAdapter.getCount()];
        for(int i=0; i<educationAdapter.getCount(); i++)
        {
            array_of_education[i] = educationAdapter.getItem(i);
        }
        outState.putParcelableArray("education", array_of_education);
    }
}