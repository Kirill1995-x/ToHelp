package com.tohelp.tohelp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santalu.maskedittext.MaskEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tohelp.tohelp.dialogs.DialogEditData;
import com.tohelp.tohelp.dialogs.DialogFinish;
import com.tohelp.tohelp.dialogs.DialogPassword;
import com.tohelp.tohelp.dialogs.DialogRequestPermission;
import com.tohelp.tohelp.dialogs.DialogSimple;
import com.tohelp.tohelp.prepare.FindItemInSpinner;
import com.tohelp.tohelp.prepare.GetArrayOfBirth;
import com.tohelp.tohelp.prepare.PhotoDelete;
import com.tohelp.tohelp.prepare.PhotoUpload;
import com.tohelp.tohelp.prepare.Profile;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity implements View.OnClickListener, com.tohelp.tohelp.interfaces.displayAlert
{
    @BindView(R.id.svMyProfile)
    ScrollView svMyProfile;
    @BindView(R.id.tilMyProfileSurname)
    TextInputLayout tilSurname;
    @BindView(R.id.tilMyProfileName)
    TextInputLayout tilName;
    @BindView(R.id.tilMyProfileMiddlename)
    TextInputLayout tilMiddlename;
    @BindView(R.id.tilMyProfileChildHome)
    TextInputLayout tilChildHome;
    @BindView(R.id.tilMyProfileEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilMyProfilePhone)
    TextInputLayout tilPhone;
    @BindView(R.id.tilMyProfileRegistrationAddress)
    TextInputLayout tilRegistrationAddress;
    @BindView(R.id.tilMyProfileFactualAddress)
    TextInputLayout tilFactualAddress;
    @BindView(R.id.tilMyProfileSubject)
    TextInputLayout tilSubject;
    @BindView(R.id.tilMyProfileCity)
    TextInputLayout tilCity;
    @BindView(R.id.tietMyProfileSurname)
    TextInputEditText tietSurname;
    @BindView(R.id.tietMyProfileName)
    TextInputEditText tietName;
    @BindView(R.id.tietMyProfileMiddlename)
    TextInputEditText tietMiddlename;
    @BindView(R.id.tietMyProfileEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietMyProfilePhone)
    MaskEditText metPhone;
    @BindView(R.id.tietMyProfileChildHome)
    TextInputEditText tietChildHome;
    @BindView(R.id.tietMyProfileRegistrationAddress)
    TextInputEditText tietRegistrationAddress;
    @BindView(R.id.tietMyProfileFactualAddress)
    TextInputEditText tietFactualAddress;
    @BindView(R.id.spMyProfileTypeOfFlat)
    Spinner spTypeOfFlat;
    @BindView(R.id.spMyProfileDate)
    Spinner spDate;
    @BindView(R.id.spMyProfileMonth)
    Spinner spMonth;
    @BindView(R.id.spMyProfileYear)
    Spinner spYear;
    @BindView(R.id.spMyProfileSex)
    Spinner spSex;
    @BindView(R.id.tvMyProfileDateOfBirth)
    TextView tvDateOfBirth;
    @BindView(R.id.tvMyProfileSex)
    TextView tvSex;
    @BindView(R.id.actvMyProfileSubject)
    AutoCompleteTextView actvSubject;
    @BindView(R.id.actvMyProfileCity)
    AutoCompleteTextView actvCity;
    @BindView(R.id.btnRefreshData)
    Button btnRefreshData;
    @BindView(R.id.btnChangePassword)
    Button btnChangingPassword;
    @BindView(R.id.btnRefreshPhoto)
    Button btnRefreshPhoto;
    @BindView(R.id.btnRemovePhoto)
    Button btnRemovePhoto;
    @BindView(R.id.imgMyProfile)
    CircleImageView imgMyProfile;
    @BindView(R.id.progressBarMyProfile)
    ProgressBar progressBar;
    Bitmap bitmap;
    SharedPreferences sharedPreferences;
    CheckInternetConnection checkInternetConnection;
    String [] array_subject_of_Russian_Federation;
    String [] array_city_of_Russian_Federation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //установка флага
        sharedPreferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().putString("flag", "first_fragment").apply();

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //обработка нажатия на элементы
        btnRefreshData.setOnClickListener(this);
        btnRefreshPhoto.setOnClickListener(this);
        btnRemovePhoto.setOnClickListener(this);
        btnChangingPassword.setOnClickListener(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //Связь массивов со списками из strings.xml
        array_subject_of_Russian_Federation=getResources().getStringArray(R.array.array_subjects);
        array_city_of_Russian_Federation=getResources().getStringArray(R.array.array_cities);
        //Ввод адаптера для работы с массивами: региона и города
        ArrayAdapter<String> adapter_for_subject=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_subject_of_Russian_Federation);
        ArrayAdapter<String> adapter_for_city=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_city_of_Russian_Federation);
        actvSubject.setAdapter(adapter_for_subject);
        actvCity.setAdapter(adapter_for_city);
        //Изменение стиля Spinner
        ArrayAdapter<String> adapter_for_date_of_birth = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayDate());
        ArrayAdapter<String> adapter_for_month_of_birth = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayMonth());
        ArrayAdapter<String> adapter_for_year_of_birth = new ArrayAdapter<String>(this, R.layout.spinner_layout_birth, new GetArrayOfBirth().CreateArrayYears());
        ArrayAdapter adapter_for_sex=ArrayAdapter.createFromResource(this, R.array.array_sex, R.layout.spinner_layout);
        ArrayAdapter adapter_type_of_flat=ArrayAdapter.createFromResource(this, R.array.array_type_of_flat, R.layout.spinner_layout);
        adapter_for_sex.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_date_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_month_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_for_year_of_birth.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter_type_of_flat.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spSex.setAdapter(adapter_for_sex);
        spDate.setAdapter(adapter_for_date_of_birth);
        spMonth.setAdapter(adapter_for_month_of_birth);
        spYear.setAdapter(adapter_for_year_of_birth);
        spTypeOfFlat.setAdapter(adapter_type_of_flat);

        //загрузка фотографии
        if (!sharedPreferences.getString("shared_name_of_photo", "without_photo").equals("without_photo")) {

            String path = null;
            try {
                path = Encryption.decrypt(sharedPreferences.getString("shared_id",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String name_of_file = sharedPreferences.getString("shared_name_of_photo","");
            String url = Variable.place_of_photo_url + path + "/" + name_of_file;
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_account)
                    .error(R.drawable.ic_account)
                    .into(imgMyProfile);
        }

        //проверка ProgressBar
        if(savedInstanceState!=null && savedInstanceState.getBoolean("showProgressBar"))
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        if(savedInstanceState==null)
        {
            tietSurname.setText(sharedPreferences.getString("shared_surname", ""));
            tietName.setText(sharedPreferences.getString("shared_name", ""));
            tietMiddlename.setText(sharedPreferences.getString("shared_middlename", ""));
            tietChildHome.setText(sharedPreferences.getString("shared_child_home",""));
            tietEmail.setText(sharedPreferences.getString("shared_email",""));
            metPhone.setText(sharedPreferences.getString("shared_phone_number",""));
            actvCity.setText(sharedPreferences.getString("shared_city",""));
            actvSubject.setText(sharedPreferences.getString("shared_subject", ""));
            tietRegistrationAddress.setText(sharedPreferences.getString("shared_registration_address",""));
            tietFactualAddress.setText(sharedPreferences.getString("shared_factual_address",""));
            int date = new FindItemInSpinner("date", sharedPreferences.getString("shared_date_of_born",""),this).getPosition();
            spDate.setSelection(date);
            int month = new FindItemInSpinner("month", sharedPreferences.getString("shared_month_of_born",""), this).getPosition();
            spMonth.setSelection(month);
            int year = new FindItemInSpinner("year", sharedPreferences.getString("shared_year_of_born", ""), this).getPosition();
            spYear.setSelection(year);
            int sex = new FindItemInSpinner("sex", sharedPreferences.getString("shared_sex",""), this).getPosition();
            spSex.setSelection(sex);
            int type_of_flat = new FindItemInSpinner("type_of_flat", sharedPreferences.getString("shared_type_of_flat",""), this).getPosition();
            spTypeOfFlat.setSelection(type_of_flat);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRefreshData:
                confirmInput();
                break;
            case R.id.btnRefreshPhoto:
                getPermissionForImage(Variable.REFRESH_PHOTO_CODE);
                break;
            case R.id.btnRemovePhoto:
                getPermissionForImage(Variable.DELETE_PHOTO_CODE);
                break;
            case R.id.btnChangePassword:
                DialogPassword passwordDialog=new DialogPassword();
                passwordDialog.show(getSupportFragmentManager(), "password_dialog");
                break;
            default:
                break;
        }
    }

    private void getPermissionForImage(int code)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
           Build.VERSION.SDK_INT<Build.VERSION_CODES.Q &&
           checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED &&
           checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
        {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, code);
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
        {
            switch (code) {
                case Variable.REFRESH_PHOTO_CODE:
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1).start(MyProfile.this);
                    break;
                case Variable.DELETE_PHOTO_CODE:
                    deleteImage();
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (code) {
                case Variable.REFRESH_PHOTO_CODE:
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1).start(MyProfile.this);
                    break;
                case Variable.DELETE_PHOTO_CODE:
                    deleteImage();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Variable.REFRESH_PHOTO_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1).start(MyProfile.this);
                }
                else
                {
                    DialogRequestPermission dialogRequestPermission = new DialogRequestPermission();
                    dialogRequestPermission.setCancelable(false);
                    dialogRequestPermission.show(getSupportFragmentManager(), "permission_dialog");
                }
                break;
            case Variable.DELETE_PHOTO_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    deleteImage();
                }
                else
                {
                    DialogRequestPermission dialogRequestPermission = new DialogRequestPermission();
                    dialogRequestPermission.setCancelable(false);
                    dialogRequestPermission.show(getSupportFragmentManager(), "permission_dialog");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result!=null)
            {
                Uri selectedImage = result.getUri();
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } else {
                        ImageDecoder.Source resources = ImageDecoder.createSource(getContentResolver(), selectedImage);
                        bitmap = ImageDecoder.decodeBitmap(resources);
                    }

                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(Bitmap bitmap_from_profile)
    {
        //запуск ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        final PhotoUpload photoUpload = new PhotoUpload(bitmap_from_profile , MyProfile.this);
        //установка изображения с измененными размерами
        imgMyProfile.setImageBitmap(photoUpload.getResizeBitmap());

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Variable.update_image_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String title = jsonObject.getString("title");
                            String message = jsonObject.getString("message");
                            progressBar.setVisibility(View.GONE);
                            if(!code.equals("send_photo_success"))
                            {
                                displayAlert(code, title, message);
                            }
                            else
                            {
                                sharedPreferences.edit().putString("shared_name_of_photo", jsonObject.getString("new_name_of_photo")).apply();
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params=new HashMap<String,String>();
                params.put("id", photoUpload.getId());
                params.put("access_token", photoUpload.getAccessToken());
                params.put("old_name_of_photo", photoUpload.getOldNameOfPhoto());
                params.put("image", photoUpload.imageToString());
                return params;
            }
        };
        MySingleton.getInstance(MyProfile.this).addToRequestque(stringRequest);
    }

    private void deleteImage()
    {
        if(!sharedPreferences.getString("shared_name_of_photo","without_photo").equals("without_photo")) {
            //запуск ProgressBar
            progressBar.setVisibility(View.VISIBLE);
            //---
            final PhotoDelete photoDelete = new PhotoDelete(MyProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.remove_image_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                progressBar.setVisibility(View.GONE);
                                if (!code.equals("delete_photo_success")) {
                                    displayAlert(code, jsonObject.getString("title"), jsonObject.getString("message"));
                                } else {
                                    bitmap = null;
                                    imgMyProfile.setImageResource(R.drawable.ic_account);
                                    photoDelete.setWithoutPhoto();
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
                    Map<String, String> params = new HashMap<>();
                    params.put("id", photoDelete.getId());
                    params.put("access_token", photoDelete.getAccessToken());
                    params.put("name_of_photo", photoDelete.getNameOfPhoto());
                    return params;
                }
            };
            MySingleton.getInstance(MyProfile.this).addToRequestque(stringRequest);
        }
    }

    public void confirmInput()
    {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(btnRefreshData.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Profile profile = new Profile(this, tilSurname, tilName, tilMiddlename, tilEmail, tilPhone,
                                      tilSubject, tilCity, spSex, tvSex, spDate, spMonth, spYear, tvDateOfBirth);
        if (checkInternetConnection.isNetworkConnected())
        {
            if (profile.CheckFieldsMyProfile())
            {
                displayAlert("input_failed",
                        getResources().getString(R.string.something_went_wrong),
                        getResources().getString(R.string.check_data));
            }
            else
            {
                //запуск ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                //---
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.send_my_profile_url,
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
                                    if (code.equals("my_profile_email_failed"))
                                    {
                                        tietEmail.setText(jsonObject.getString("email_for_my_profile"));
                                    }
                                    else if (code.equals("my_profile_phone_failed"))
                                    {
                                        metPhone.setText(jsonObject.getString("phone_for_my_profile"));
                                    }
                                    else if (code.equals("my_profile_email_and_phone_failed"))
                                    {
                                        metPhone.setText(jsonObject.getString("phone_for_my_profile"));
                                        tietEmail.setText(jsonObject.getString("email_for_my_profile"));
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
                            params.put("id", Encryption.decrypt(sharedPreferences.getString("shared_id","")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        params.put("access_token", sharedPreferences.getString("shared_access_token",""));
                        params.put("surname", Objects.requireNonNull(tilSurname.getEditText()).getText().toString().trim());
                        params.put("name", Objects.requireNonNull(tilName.getEditText()).getText().toString().trim());
                        params.put("middlename", Objects.requireNonNull(tilMiddlename.getEditText()).getText().toString().trim());
                        params.put("child_home", Objects.requireNonNull(tilChildHome.getEditText()).getText().toString().trim());
                        params.put("email", Objects.requireNonNull(tilEmail.getEditText()).getText().toString().trim());
                        params.put("phone_number", Objects.requireNonNull(tilPhone.getEditText()).getText().toString().trim());
                        params.put("city", actvCity.getText().toString().trim());
                        params.put("subject_of_country", actvSubject.getText().toString().trim());
                        params.put("registration_address", Objects.requireNonNull(tilRegistrationAddress.getEditText()).getText().toString().trim());
                        params.put("factual_address", Objects.requireNonNull(tilFactualAddress.getEditText()).getText().toString().trim());
                        params.put("type_of_flat", spTypeOfFlat.getSelectedItem().toString().trim());
                        params.put("sex", spSex.getSelectedItem().toString().trim());
                        params.put("date_of_born", spDate.getSelectedItem().toString().trim());
                        params.put("month_of_born", spMonth.getSelectedItem().toString().trim());
                        params.put("year_of_born", spYear.getSelectedItem().toString().trim());
                        return params;
                    }
                };
                MySingleton.getInstance(MyProfile.this).addToRequestque(stringRequest);
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
        outState.putString("surname", Objects.requireNonNull(tietSurname.getText()).toString().trim());
        outState.putString("name", Objects.requireNonNull(tietName.getText()).toString().trim());
        outState.putString("middlename", Objects.requireNonNull(tietMiddlename.getText()).toString().trim());
        outState.putString("child_home", Objects.requireNonNull(tietChildHome.getText()).toString().trim());
        outState.putString("email", Objects.requireNonNull(tietEmail.getText()).toString().trim());
        outState.putString("phone", Objects.requireNonNull(metPhone.getText()).toString().trim());
        outState.putString("registration_address", Objects.requireNonNull(tietRegistrationAddress.getText()).toString().trim());
        outState.putString("factual_address", Objects.requireNonNull(tietFactualAddress.getText()).toString().trim());
        outState.putString("subject_of_Russian_Federation", actvSubject.getText().toString().trim());
        outState.putString("city_of_Russian_Federation", actvCity.getText().toString().trim());
        outState.putInt("sex", spSex.getSelectedItemPosition());
        outState.putInt("type_of_flat", spTypeOfFlat.getSelectedItemPosition());
        outState.putInt("day", spDate.getSelectedItemPosition());
        outState.putInt("month", spMonth.getSelectedItemPosition());
        outState.putInt("year", spYear.getSelectedItemPosition());
        outState.putBoolean("showProgressBar", progressBar.isShown());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        tietSurname.setText(savedInstanceState.getString("surname"));
        tietName.setText(savedInstanceState.getString("name"));
        tietMiddlename.setText(savedInstanceState.getString("middlename"));
        tietChildHome.setText(savedInstanceState.getString("child_home"));
        tietEmail.setText(savedInstanceState.getString("email"));
        metPhone.setText(savedInstanceState.getString("phone"));
        tietRegistrationAddress.setText(savedInstanceState.getString("registration_address"));
        tietFactualAddress.setText(savedInstanceState.getString("factual_address"));
        actvSubject.setText(savedInstanceState.getString("subject_of_Russian_Federation"));
        actvCity.setText(savedInstanceState.getString("city_of_Russian_Federation"));
        spSex.setSelection(savedInstanceState.getInt("sex"));
        spTypeOfFlat.setSelection(savedInstanceState.getInt("type_of_flat"));
        spDate.setSelection(savedInstanceState.getInt("day"));
        spMonth.setSelection(savedInstanceState.getInt("month"));
        spYear.setSelection(savedInstanceState.getInt("year"));
        if (savedInstanceState.getParcelable("bitmap")!=null)
        {
            bitmap=(Bitmap)savedInstanceState.getParcelable("bitmap");
            imgMyProfile.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed()
    {
        SharedPreferences preferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
        if (!Objects.requireNonNull(tietSurname.getText()).toString().trim().equals(preferences.getString("shared_surname","")) |
            !Objects.requireNonNull(tietName.getText()).toString().trim().equals(preferences.getString("shared_name", "")) |
            !Objects.requireNonNull(tietMiddlename.getText()).toString().trim().equals(preferences.getString("shared_middlename","")) |
            !Objects.requireNonNull(tietChildHome.getText()).toString().trim().equals(preferences.getString("shared_child_home","")) |
            !Objects.requireNonNull(tietEmail.getText()).toString().trim().equals(preferences.getString("shared_email", "")) |
            !Objects.requireNonNull(metPhone.getText()).toString().trim().equals(preferences.getString("shared_phone_number","")) |
            !Objects.requireNonNull(tietRegistrationAddress.getText()).toString().trim().equals(preferences.getString("shared_registration_address","")) |
            !Objects.requireNonNull(tietFactualAddress.getText()).toString().trim().equals(preferences.getString("shared_factual_address","")) |
            !actvCity.getText().toString().trim().equals(preferences.getString("shared_city","")) |
            !actvSubject.getText().toString().trim().equals(preferences.getString("shared_subject","")) |
            !spTypeOfFlat.getSelectedItem().toString().equals(preferences.getString("shared_type_of_flat","")) |
            !spDate.getSelectedItem().toString().equals(preferences.getString("shared_date_of_born","")) |
            !spMonth.getSelectedItem().toString().equals(preferences.getString("shared_month_of_born","")) |
            !spYear.getSelectedItem().toString().equals(preferences.getString("shared_year_of_born","")) |
            !spSex.getSelectedItem().toString().equals(preferences.getString("shared_sex","")))
        {
            DialogEditData dialogEditData=new DialogEditData();
            dialogEditData.setCancelable(false);
            dialogEditData.show(getSupportFragmentManager(), "my_profile_edit_data_dialog");
        }
        else
        {
            super.onBackPressed();
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

    @Override
    public void displayAlert(String code, String title, String message)
    {
        if (code.equals("my_profile_get_success"))
        {
            SharedPreferences preferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            DialogFinish dialog=new DialogFinish();
            DialogFinish.title=title;
            DialogFinish.message=message;
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "my_profile_dialog_finish");
            editor.putString("shared_surname", Objects.requireNonNull(tilSurname.getEditText()).getText().toString().trim());
            editor.putString("shared_name", Objects.requireNonNull(tilName.getEditText()).getText().toString().trim());
            editor.putString("shared_middlename", Objects.requireNonNull(tilMiddlename.getEditText()).getText().toString().trim());
            editor.putString("shared_child_home", Objects.requireNonNull(tilChildHome.getEditText()).getText().toString().trim());
            editor.putString("shared_email", Objects.requireNonNull(tilEmail.getEditText()).getText().toString().trim());
            editor.putString("shared_phone_number", Objects.requireNonNull(tilPhone.getEditText()).getText().toString().trim());
            editor.putString("shared_city", actvCity.getText().toString().trim());
            editor.putString("shared_subject", actvSubject.getText().toString().trim());
            editor.putString("shared_registration_address", Objects.requireNonNull(tilRegistrationAddress.getEditText()).getText().toString().trim());
            editor.putString("shared_factual_address", Objects.requireNonNull(tilFactualAddress.getEditText()).getText().toString().trim());
            editor.putString("shared_type_of_flat", spTypeOfFlat.getSelectedItem().toString().trim());
            editor.putString("shared_date_of_born", spDate.getSelectedItem().toString().trim());
            editor.putString("shared_sex", spSex.getSelectedItem().toString().trim());
            editor.putString("shared_month_of_born", spMonth.getSelectedItem().toString().trim());
            editor.putString("shared_year_of_born", spYear.getSelectedItem().toString().trim());
            editor.apply();
        }
        else
        {
            DialogSimple dialog=new DialogSimple();
            DialogSimple.title=title;
            DialogSimple.message=message;
            dialog.show(getSupportFragmentManager(), "my_profile_dialog");
        }
    }
}
