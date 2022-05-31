package com.tohelp.tohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.resume.CheckResume;
import com.tohelp.tohelp.resume.Projects;
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

public class ActivityProjects extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert {

    @BindView(R.id.svProjects)
    ScrollView svProjects;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.llShowProjects)
    LinearLayout llShowProjects;
    @BindView(R.id.btnAddProject)
    Button btnAddProject;
    @BindView(R.id.btnSaveProjects)
    Button btnSaveProjects;
    @BindView(R.id.progressBarProjects)
    ProgressBar progressBar;
    Projects Projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_projects);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //обработка нажатия на кнопку
        btnAddProject.setOnClickListener(this);
        btnSaveProjects.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            svProjects.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            //получение данных
            if(savedInstanceState==null)getProjects();
            else
            {
                svProjects.setVisibility(View.VISIBLE);
                viewFailedInternetConnection.setVisibility(View.GONE);
                //проверка ProgressBar
                if(savedInstanceState.getBoolean("showProgressBar")) progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAddProject:
                addProject();
                break;
            case R.id.btnSaveProjects:
                sendProjects();
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(this).isNetworkConnected())
                {
                    if(llShowProjects.getChildCount()>0) {
                        svProjects.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                    }
                    else getProjects();
                }
                break;
            default:
                break;
        }
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

    private void getProjects()
    {
        svProjects.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_projects_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                Projects = new Projects(jsonObject.getString("projects"));
                                splitProjects(Projects.getProjects());
                            }
                            else if(code.equals("failed"))
                            {
                                displayAlert(code, jsonObject.getString("title"), jsonObject.getString("message"));
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
                try {
                    params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id", "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestque(stringRequest);
    }

    private void sendProjects()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnSaveProjects.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!new CheckInternetConnection(this).isNetworkConnected())
        {
            displayAlert("internet_connection_failed", getResources().getString(R.string.error_connection), getResources().getString(R.string.check_connection));
        }
        else
        {
            if(new CheckResume(llShowProjects).CheckFieldsProjects())
            {
                displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_resume_projects_url,
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
                        SharedPreferences sharedPreferences = getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
                        try {
                            params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("projects", uniteProjects());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestque(stringRequest);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(Projects!=null && !uniteProjects().equals(Projects.getProjects()))
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "resume_edit_data_dialog");
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("showProgressBar", progressBar.isShown());
        outState.putString("unite_projects", uniteProjects());
        outState.putParcelable("projects", Projects);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Projects = savedInstanceState.getParcelable("projects");
        String unite_projects = savedInstanceState.getString("unite_projects");
        unite_projects=(unite_projects==null)?"":unite_projects;
        splitProjects(unite_projects);
    }

    private void addProject() {
        if(llShowProjects.getChildCount()<10) {
            View view = getLayoutInflater().inflate(R.layout.pattern_project, null, false);

            view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llShowProjects.removeView(view);
                }
            });

            llShowProjects.addView(view);
        }
        else
        {
            displayAlert("input_failed", getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.check_count_of_blocks));
        }
    }

    public String uniteProjects()
    {
        StringBuilder projects= new StringBuilder();
        //---
        for(int i=0; i<llShowProjects.getChildCount(); i++)
        {
            View view = llShowProjects.getChildAt(i);
            EditText etProject = view.findViewById(R.id.etProject);
            EditText etDescribeProject = view.findViewById(R.id.etDescribeProject);
            String project = (etProject.getText().toString().trim().isEmpty())?" ":etProject.getText().toString().trim();
            String describe_project = (etDescribeProject.getText().toString().trim().isEmpty())?" ":etDescribeProject.getText().toString().trim();
            projects.append(project).append("tag").append(describe_project).append("block");
        }
        return projects.toString();
    }

    public void splitProjects(String projects)
    {
        if(!projects.equals("")) {
            String[] array_of_projects = projects.split("block");
            //---
            for (int i = 0; i < array_of_projects.length; i++) {
                View view = getLayoutInflater().inflate(R.layout.pattern_project, null, false);
                EditText etProject = view.findViewById(R.id.etProject);
                EditText etDescribeProject = view.findViewById(R.id.etDescribeProject);

                view.findViewById(R.id.fabRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llShowProjects.removeView(view);
                    }
                });

                String[] split = array_of_projects[i].split("tag");
                String project = (split[0].equals(" ")) ? "" : split[0];
                String describe_project = (split[1].equals(" ")) ? "" : split[1];
                etProject.setText(project);
                etDescribeProject.setText(describe_project);
                llShowProjects.addView(view);
            }
        }
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        if(code.equals("resume_get_success"))
        {
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            if(Projects==null)
                Projects = new Projects(uniteProjects());
            else
                Projects.setProjects(uniteProjects());
            dialog.show(getSupportFragmentManager(), "resume_dialog");
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "resume_dialog");
        }

    }
}