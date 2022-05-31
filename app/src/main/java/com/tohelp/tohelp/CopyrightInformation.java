package com.tohelp.tohelp;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.tohelp.tohelp.lists.CopyrightAdapter;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CopyrightInformation extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvLicence)
    TextView tvLinkForLicence;
    @BindView(R.id.lvListOfAuthors)
    ListView lvListOfAuthors;
    CopyrightAdapter copyrightAdapter;
    String link_to_licence = "https://creativecommons.org/publicdomain/zero/1.0/deed.ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright_information);

        //Настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //создание адаптера
        copyrightAdapter = new CopyrightAdapter(CopyrightInformation.this);
        lvListOfAuthors.setAdapter(copyrightAdapter);

        //обработка нажатия на элемент
        tvLinkForLicence.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvLicence:
                Intent intent = new Intent(CopyrightInformation.this, Browser.class);
                intent.putExtra("url_phone", link_to_licence);
                intent.putExtra("url_tablet", link_to_licence);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}