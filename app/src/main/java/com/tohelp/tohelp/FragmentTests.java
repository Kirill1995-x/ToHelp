package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tohelp.tohelp.lists.Tests;
import com.tohelp.tohelp.lists.TestsAdapter;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentTests extends Fragment implements View.OnClickListener
{
    @BindView(R.id.lvFourthFragment)
    ListView lvNameOfTest;
    @BindView(R.id.progressBarFourthFragment)
    ProgressBar progressBar;
    @BindView(R.id.tvSearchTests)
    TextView tvSearchTests;
    @BindView(R.id.spSearchTests)
    Spinner spSearchTests;
    @BindView(R.id.llListOfTests)
    LinearLayout llListOfTests;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    TestsAdapter testsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tests, container, false);
        //настройка ButterKnife
        ButterKnife.bind(this, v);
        //флаг для фрагментов кроме Навигатора и Онлайн-консультанта
        requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                .putString("flag", "third_fragment").apply();

        //связь массивов с массивами из string.xml
        ArrayAdapter adapter_for_category_of_tests=ArrayAdapter.createFromResource(requireActivity(),
                                                    R.array.array_tests, R.layout.spinner_layout);
        adapter_for_category_of_tests.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spSearchTests.setAdapter(adapter_for_category_of_tests);

        //создание адаптера
        testsAdapter = new TestsAdapter(requireActivity());

        if(savedInstanceState!=null)
        {
            spSearchTests.setSelection(savedInstanceState.getInt("category_test"));
            Parcelable[] array_of_tests = savedInstanceState.getParcelableArray("tests");
            if (array_of_tests != null && array_of_tests.length!=0)
            {
                for (int i = 0; i < array_of_tests.length; i++)
                {
                    testsAdapter.add((Tests) array_of_tests[i]);
                }
            }
        }

        //обработка нажатия на элемент
        tvTryRequest.setOnClickListener(this);

        lvNameOfTest.setAdapter(testsAdapter);
        lvNameOfTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tests tests = testsAdapter.getItem(position);
                if (tests != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tests.getLinkToTest()));
                    startActivity(intent);
                }
            }
        });

        spSearchTests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confirmInput(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTryRequest:
                confirmInput(String.valueOf(spSearchTests.getSelectedItemPosition()+1));
                break;
            default:
                break;
        }
    }

    private void confirmInput(String number)
    {
        if(!new CheckInternetConnection(getActivity()).isNetworkConnected())
        {
            llListOfTests.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            testsAdapter.clear();
            progressBar.setVisibility(View.VISIBLE);
            viewFailedInternetConnection.setVisibility(View.GONE);
            llListOfTests.setVisibility(View.VISIBLE);

            String parameter_for_request = null;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Variable.request_tests_url + number, parameter_for_request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Tests tests = new Tests(jsonObject.getInt("id"),
                                            jsonObject.getString("name_of_test"),
                                            jsonObject.getString("description_of_test"),
                                            jsonObject.getString("link_to_test"));
                                    testsAdapter.add(tests);
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Tests []array_of_tests = new Tests[testsAdapter.getCount()];
        for(int i=0; i<testsAdapter.getCount(); i++)
        {
            array_of_tests[i] = testsAdapter.getItem(i);
        }
        outState.putParcelableArray("tests", array_of_tests);
        outState.putInt("category_test", spSearchTests.getSelectedItemPosition());
    }
}
