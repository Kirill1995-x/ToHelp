package com.tohelp.tohelp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.tohelp.tohelp.dialogs.DialogRequestPermission;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.resume.DataForResume;
import com.tohelp.tohelp.resume.Resume;
import com.tohelp.tohelp.resume.ResumeAdditionalInformation;
import com.tohelp.tohelp.resume.ResumeCourses;
import com.tohelp.tohelp.resume.ResumeEducation;
import com.tohelp.tohelp.resume.ResumeLanguage;
import com.tohelp.tohelp.resume.ResumeMainSkills;
import com.tohelp.tohelp.resume.ResumePersonalData;
import com.tohelp.tohelp.resume.ResumeProjects;
import com.tohelp.tohelp.resume.ResumeWorkExperience;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentResume extends Fragment implements View.OnClickListener,
                                                        com.tohelp.tohelp.interfaces.displayAlert
{
    @BindView(R.id.svFifthFragment)
    ScrollView svFifthFragment;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.imgResume)
    ImageView imgResume;
    @BindView(R.id.tvPersonalData)
    TextView tvPersonalData;
    @BindView(R.id.tvWorkExperience)
    TextView tvWorkExperience;
    @BindView(R.id.tvMainSkills)
    TextView tvMainSkills;
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvCourses)
    TextView tvCourses;
    @BindView(R.id.tvProjects)
    TextView tvProjects;
    @BindView(R.id.tvAdditionalInformation)
    TextView tvAdditionalInformation;
    @BindView(R.id.btnCreatePDF)
    Button btnCreatePDF;
    @BindView(R.id.progressBarFifthFragment)
    ProgressBar progressBar;
    Intent intent;
    DataForResume dataForResume;

    ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>()
            {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean write_external_storage = (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=null)?
                            result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE):false;
                    boolean read_external_storage = (result.get(Manifest.permission.READ_EXTERNAL_STORAGE)!=null)?
                            result.get(Manifest.permission.READ_EXTERNAL_STORAGE):false;
                    if(write_external_storage && read_external_storage)
                       preparePDF();
                    else
                    {
                        DialogRequestPermission dialogRequestPermission = new DialogRequestPermission();
                        dialogRequestPermission.setCancelable(false);
                        dialogRequestPermission.show(requireActivity().getSupportFragmentManager(), "permission_dialog");
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_resume, container, false);

        //настройка ButterKnife
        ButterKnife.bind(this,v);

        //флаг для фрагментов кроме Навигатора и Онлайн-консультанта
        requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                .putString("flag", "fourth_fragment").apply();

        //обработка нажатий на кнопки
        btnCreatePDF.setOnClickListener(this);
        tvTryRequest.setOnClickListener(this);
        imgResume.setOnClickListener(this);
        tvPersonalData.setOnClickListener(this);
        tvWorkExperience.setOnClickListener(this);
        tvMainSkills.setOnClickListener(this);
        tvLanguage.setOnClickListener(this);
        tvEducation.setOnClickListener(this);
        tvCourses.setOnClickListener(this);
        tvProjects.setOnClickListener(this);
        tvAdditionalInformation.setOnClickListener(this);

        //проверка ProgressBar
        if(savedInstanceState!=null)
        {
            dataForResume = savedInstanceState.getParcelable("data_for_resume");
            if(savedInstanceState.getBoolean("showProgressBar"))progressBar.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!new CheckInternetConnection(getActivity()).isNetworkConnected())
        {
            svFifthFragment.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            getResume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCreatePDF:
                try {
                    createPDF();
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvTryRequest:
                if(new CheckInternetConnection(getActivity()).isNetworkConnected())
                {
                    getResume();
                }
                break;
            case R.id.imgResume:
                displayAlert("code", getResources().getString(R.string.hint_resume), getResources().getString(R.string.fill_fields_resume));
                break;
            case R.id.tvPersonalData:
                intent = new Intent(getActivity(), ActivityPersonalData.class);
                startActivity(intent);
                break;
            case R.id.tvWorkExperience:
                intent = new Intent(getActivity(), ActivityWorkExperience.class);
                startActivity(intent);
                break;
            case R.id.tvMainSkills:
                intent = new Intent(getActivity(), ActivityMainSkills.class);
                startActivity(intent);
                break;
            case R.id.tvEducation:
                intent = new Intent(getActivity(), ActivityEducation.class);
                startActivity(intent);
                break;
            case R.id.tvLanguage:
                intent = new Intent(getActivity(), ActivityLanguages.class);
                startActivity(intent);
                break;
            case R.id.tvCourses:
                intent = new Intent(getActivity(), ActivityCourses.class);
                startActivity(intent);
                break;
            case R.id.tvProjects:
                intent = new Intent(getActivity(), ActivityProjects.class);
                startActivity(intent);
                break;
            case R.id.tvAdditionalInformation:
                intent = new Intent(getActivity(), ActivityAdditionalInformation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getResume()
    {
        progressBar.setVisibility(View.VISIBLE);
        svFifthFragment.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        //---
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.get_resume_all_data_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("success"))
                            {
                                boolean business_trips = (jsonObject.getString("business_trips").equals("1"));
                                boolean moving = (jsonObject.getString("moving")).equals("1");
                                boolean having_children = (jsonObject.getString("having_children")).equals("1");
                                boolean military_service = (jsonObject.getString("military_service")).equals("1");
                                dataForResume = new DataForResume(jsonObject.getString("career_objective"), jsonObject.getString("salary"),
                                        jsonObject.getString("employment"), jsonObject.getString("schedule"),  jsonObject.getString("marital_status"),
                                        business_trips, moving, having_children, jsonObject.getString("basic_skills"), jsonObject.getString("computer_skills"),
                                        jsonObject.getString("program"), military_service, jsonObject.getInt("drivers_licences"), jsonObject.getString("work"),
                                        jsonObject.getString("education"), jsonObject.getString("courses"), jsonObject.getString("projects"),
                                        jsonObject.getString("languages"), jsonObject.getString("personal_characteristics"), jsonObject.getString("hobby"),
                                        jsonObject.getString("wishes_for_work"));
                            }
                            else if (code.equals("not_created"))
                            {
                                dataForResume = new DataForResume("", "", "", "", "", false,
                                                                false, false, "", "", "", false, 0,
                                                                "", "", "", "", "","","","");
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
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
                try {
                    params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id", "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
    }

    private void createPDF() throws IOException, DocumentException {
        //проверка разрешений для обращения в директориям
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
           Build.VERSION.SDK_INT<Build.VERSION_CODES.Q &&
           requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED &&
           requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
        {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            getPermissions.launch(permissions);
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) preparePDF();
        else preparePDF();
    }

    private void preparePDF()
    {
        Intent intent = new Intent(getActivity(), ShowResume.class);
        OutputStream outputStream = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)//версии Android включая 10 и выше
        {
            ContentResolver contentResolver = requireContext().getContentResolver();
            String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
            String[] args = {"Резюме.pdf"};
            contentResolver.delete(MediaStore.Files.getContentUri("external"), selection, args);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Резюме.pdf");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = requireContext().getContentResolver()
                      .insert(MediaStore.Files.getContentUri("external"), values);
            if(uri!=null) {
                intent.putExtra("path", uri);
                try {
                    outputStream = requireContext().getContentResolver().openOutputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else //версии ниже Android 10
        {
            String filePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+"Резюме.pdf";//директория файла
            intent.putExtra("path", filePath);
            File file = new File(filePath);//создание файла
            if(file.exists())//проверка файла на существование
            {
                if(!file.delete()) Toast.makeText(getActivity(), "Произошла ошибка при удалении файла", Toast.LENGTH_LONG).show();
            }

            try {
                outputStream = new FileOutputStream(filePath);//класс для записи байт в файл
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try{
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, outputStream);

            document.open();

            new Resume(requireContext(), document).resume();
            new ResumePersonalData(requireContext(), document,  dataForResume.getCareerObjective(),
                    dataForResume.getSalary(), dataForResume.getEmployment(), dataForResume.getSchedule(),
                    dataForResume.isBusinessTrips(), dataForResume.isMoving(), dataForResume.isHavingChildren(), dataForResume.getMaritalStatus()).resume();
            new ResumeMainSkills(requireContext(), document, dataForResume.getBasicSkills(),
                    dataForResume.getComputerSkills(),  dataForResume.getProgram(), dataForResume.isMilitaryService(), dataForResume.getDriversLicences()).resume();
            new ResumeWorkExperience(requireContext(), document, dataForResume.getWork()).resume();
            new ResumeEducation(requireContext(), document, dataForResume.getEducation()).resume();
            new ResumeLanguage(requireContext(), document, dataForResume.getLanguages()).resume();
            new ResumeCourses(requireContext(), document, dataForResume.getCourses()).resume();
            new ResumeProjects(requireContext(), document, dataForResume.getProjects()).resume();
            new ResumeAdditionalInformation(requireContext(), document, dataForResume.getPersonalCharacteristics(),
                    dataForResume.getHobby(), dataForResume.getWishesForWork()).resume();

            document.close();

            Toast.makeText(getActivity(), "Резюме.pdf сохранен в Загрузки", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //открытие PDF в ViewActivity
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data_for_resume", dataForResume);
        outState.putBoolean("showProgressBar", progressBar.isShown());
    }

    @Override
    public void displayAlert(String code, String title, String message)
    {
        DialogSimple dialog=new DialogSimple();
        DialogSimple.title=title;
        DialogSimple.message=message;
        dialog.show(requireActivity().getSupportFragmentManager(), "resume_dialog");
    }

}
