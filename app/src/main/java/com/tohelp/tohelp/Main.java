package com.tohelp.tohelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.tohelp.tohelp.dialogs.DialogDeleteProfile;
import com.tohelp.tohelp.dialogs.DialogEstimate;
import com.tohelp.tohelp.dialogs.DialogQuit;
import com.tohelp.tohelp.dialogs.DialogUpdate;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.settings.Encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Main extends AppCompatActivity
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    TextView tvFullname;
    CircleImageView imageOfAccount;
    SharedPreferences preferences;
    CheckInternetConnection checkInternetConnection;
    String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //установка toolbar
        setSupportActionBar(toolbar);
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                displaySelectedScreen(menuItem.getItemId());
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //проверка Интернета
        checkInternetConnection=new CheckInternetConnection(this);

        //проверка первого запуска приложения
        preferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
        if (preferences.getBoolean("first_start", true))
        {
            displaySelectedScreen(R.id.menu_articles);//открытие окна "Навигатор" при первом запуске приложения
            drawer.openDrawer(GravityCompat.START);//открытие шторки при первом запуске приложения
            preferences.edit().putBoolean("first_start", false).apply();
        }
        else {
            //проверка через savedInstanceState необходима для того, чтобы фрагменты не дублировались
            //фрагменты с сетевыми запросами размещены в onResume для большего количества запросов
            String flag = preferences.getString("flag","first_fragment");
            if (savedInstanceState == null && flag!=null) {
                if (flag.equals("first_fragment")) {
                    displaySelectedScreen(R.id.menu_articles);//открытие окна "Навигатор" при первом запуске приложения
                } else if (flag.equals("third_fragment")) {
                    displaySelectedScreen(R.id.menu_tests);//открытие окна "Онлайн-диагностика" при первом запуске приложения
                } else if (flag.equals("fourth_fragment")) {
                    displaySelectedScreen(R.id.menu_resume);//открытие окна "Мое резюме" при первом запуске приложения
                } else if (flag.equals("fifth_fragment")) {
                    displaySelectedScreen(R.id.menu_tech_support);//открытие окна "Тех.поддержка" при первом запуске приложения
                } else if (flag.equals("sixth_fragment")) {
                    displaySelectedScreen(R.id.menu_courses);//открытие окна "Образование" при первом запуске приложения
                }
            }
        }

        //обработка нажатия на header
        View headerview=navigationView.getHeaderView(0);
        tvFullname=headerview.findViewById(R.id.tvFullname);
        imageOfAccount=headerview.findViewById(R.id.imageView);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences_for_questionary=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
                Intent intent = new Intent(Main.this, MyProfile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //установка ФИО и фотографии
        preferences=getSharedPreferences(Variable.APP_PREFERENCES, MODE_PRIVATE);
        fullname=preferences.getString("shared_surname","")+" "+
                 preferences.getString("shared_name","")+" "+
                 preferences.getString("shared_middlename","");
        tvFullname.setText(fullname);
        if (!preferences.getString("shared_name_of_photo", "without_photo").equals("without_photo")) {
            String path = null;
            try {
                path = Encryption.decrypt(preferences.getString("shared_id",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String name_of_file = preferences.getString("shared_name_of_photo","");
            String url = Variable.place_of_photo_url + path + "/" + name_of_file;
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_account)
                    .error(R.drawable.ic_account)
                    .into(imageOfAccount);
        }
        else
        {
            imageOfAccount.setImageResource(R.drawable.ic_account);
        }

        String flag = preferences.getString("flag", "first_fragment");
        if(flag!=null) {
            if (flag.equals("second_fragment")) {
                displaySelectedScreen(R.id.menu_specialists);//открытие окна "Онлайн-консультант" при первом запуске приложения
            }
        }

        //проверка обновлений
        SharedPreferences preferencesNotification = getSharedPreferences(Variable.APP_NOTIFICATIONS, MODE_PRIVATE);
        if(!preferencesNotification.getBoolean("compare_versions",false))
        {
            //проверка существования фрагмента по тегу
            if(getSupportFragmentManager().findFragmentByTag("update_version_dialog")==null)
            {
                DialogUpdate dialogUpdate = new DialogUpdate();
                dialogUpdate.setCancelable(false);
                dialogUpdate.show(getSupportFragmentManager(), "update_version_dialog");
            }
        }
        //проверка количества нажатий на кнопки
        int count_of_click = preferencesNotification.getInt("count_of_click", Variable.MIN_COUNT_OF_CLICK);
        if(count_of_click==Variable.COUNT_OF_CLICK)
        {
            //проверка существования фрагмента по тегу
            if(getSupportFragmentManager().findFragmentByTag("estimate_dialog")==null)
            {
                DialogEstimate dialogEstimate = new DialogEstimate();
                dialogEstimate.setCancelable(false);
                dialogEstimate.show(getSupportFragmentManager(), "estimate_dialog");
            }
        }
        else if(count_of_click>Variable.COUNT_OF_CLICK && count_of_click<Variable.MAX_COUNT_OF_CLICK)
        {
            preferencesNotification.edit().putInt("count_of_click", Variable.MIN_COUNT_OF_CLICK).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.menuQuestionary:
                intent = new Intent(Main.this, ActivityQuestionary.class);
                startActivity(intent);
                break;
            case R.id.menuAboutApp:
                intent = new Intent(Main.this, Browser.class);
                intent.putExtra("url_phone", Variable.about_application_phone_url);
                intent.putExtra("url_tablet", Variable.about_application_tablet_url);
                startActivity(intent);
                break;
            case R.id.menuConfidence:
                intent = new Intent(Main.this, Browser.class);
                intent.putExtra("url_phone", Variable.document_phone_url);
                intent.putExtra("url_tablet", Variable.document_tablet_url);
                startActivity(intent);
                break;
            case R.id.menuAuthorRules:
                intent = new Intent(Main.this, CopyrightInformation.class);
                startActivity(intent);
                break;
            case R.id.menuDeleteProfile:
                DialogDeleteProfile dialogDeleteProfile=new DialogDeleteProfile();
                dialogDeleteProfile.show(getSupportFragmentManager(), "delete_profile_dialog");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();

    }

    private void displaySelectedScreen(int id)
    {
        if (id != R.id.menu_exit)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (id == R.id.menu_articles) {
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentArticles()).commit();
            } else if (id == R.id.menu_specialists) {
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentSpecialists()).commit();
            }  else if (id == R.id.menu_tests) {
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentTests()).commit();
            } else if (id == R.id.menu_resume) {
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentResume()).commit();
            } else if (id == R.id.menu_tech_support){
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentTechSupport()).commit();
            } else if (id == R.id.menu_courses){
                fragmentManager.beginTransaction().replace(R.id.container, new FragmentCourses()).commit();
            }
        }
        else
        {
            DialogQuit dialogQuit=new DialogQuit();
            dialogQuit.setCancelable(false);
            dialogQuit.show(getSupportFragmentManager(), "quit_dialog");
        }
    }
}
