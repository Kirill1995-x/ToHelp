package com.tohelp.tohelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.tohelp.tohelp.settings.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShowResume extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_resume);

        PDFView pdfView  = findViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)//версии Android включая 10 и выше
            {
                Uri uri = bundle.getParcelable("path");
                if (uri != null)
                {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        pdfView.fromStream(inputStream)
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .enableAnnotationRendering(false)
                                .defaultPage(0)
                                .password(null)
                                .scrollHandle(null)
                                .load();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(ShowResume.this, "Файл не найден", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(ShowResume.this, "Неверный путь к файлу", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                String filePath = bundle.getString("path");
                if(filePath!=null) {
                    pdfView.fromFile(new File(filePath))
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .enableAnnotationRendering(false)
                            .defaultPage(0)
                            .password(null)
                            .scrollHandle(null)
                            .load();
                }
                else
                {
                    Toast.makeText(ShowResume.this, "Неверный путь к файлу", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Toast.makeText(ShowResume.this, "Произошла неизвестная ошибка", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        SharedPreferences preferencesNotification = getSharedPreferences(Variable.APP_NOTIFICATIONS, MODE_PRIVATE);
        int count_of_click = preferencesNotification.getInt("count_of_click", Variable.MIN_COUNT_OF_CLICK);
        if(count_of_click<Variable.COUNT_OF_CLICK)
        {
            preferencesNotification.edit().putInt("count_of_click", count_of_click+1).apply();
        }
    }
}