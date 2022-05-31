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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tohelp.tohelp.lists.Articles;
import com.tohelp.tohelp.lists.ArticlesAdapter;
import com.tohelp.tohelp.settings.CheckInternetConnection;
import com.tohelp.tohelp.settings.MySingleton;
import com.tohelp.tohelp.settings.Variable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfArticles extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.lvTitlesOfArticles)
    ListView lvNameOfArticles;
    @BindView(R.id.progressBarSecondFormQuestionNavigator)
    ProgressBar progressBar;
    @BindView(R.id.tvTryRequest)
    TextView tvTryRequest;
    @BindView(R.id.viewFailedInternetConnection)
    View viewFailedInternetConnection;
    ArticlesAdapter articlesAdapter;
    CheckInternetConnection checkInternetConnection;
    Bundle get_id;
    String id_of_articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_house_problem);

        //настройка ButterKnife
        ButterKnife.bind(this);

        //добавление стрелки "Вверх"
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //получение id статей
        get_id = getIntent().getExtras();
        id_of_articles=(get_id!=null)?get_id.getString("id_of_articles"):"f";

        //создание адаптера
        articlesAdapter = new ArticlesAdapter(this);

        //проверка Интернета
        checkInternetConnection = new CheckInternetConnection(this);

        //обработка нажатия на элемент
        tvTryRequest.setOnClickListener(this);

        if(!checkInternetConnection.isNetworkConnected())
        {
            lvNameOfArticles.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            viewFailedInternetConnection.setVisibility(View.VISIBLE);
        }
        else
        {
            if (savedInstanceState == null)
            {
                request_list_of_articles();
            }
            else
            {
                Parcelable[] array_of_articles = savedInstanceState.getParcelableArray("articles");
                if (array_of_articles != null)
                {
                    if (array_of_articles.length == 0)
                    {
                        request_list_of_articles();
                    }
                    else
                    {
                        lvNameOfArticles.setVisibility(View.VISIBLE);
                        viewFailedInternetConnection.setVisibility(View.GONE);
                        for (int i = 0; i < array_of_articles.length; i++)
                        {
                            articlesAdapter.add((Articles) array_of_articles[i]);
                        }

                    }
                }
            }
        }

        lvNameOfArticles.setAdapter(articlesAdapter);
        lvNameOfArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Articles articles = articlesAdapter.getItem(position);
                if (articles != null) {
                    Intent intent = new Intent(ListOfArticles.this, Browser.class);
                    intent.putExtra("title_navigator", articles.getLinkToArticle());
                    String name_of_file=id_of_articles+articles.getLinkToArticle();
                    String phone_url= Variable.url_articles_phone+name_of_file+"_phone.html";
                    String tablet_url=Variable.url_articles_tablet+name_of_file+"_tablet.html";
                    intent.putExtra("url_phone", phone_url);
                    intent.putExtra("url_tablet", tablet_url);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvTryRequest:
                if (checkInternetConnection.isNetworkConnected()) {
                    request_list_of_articles();
                }
                break;
            default:
                break;
        }
    }

    private void request_list_of_articles()
    {
        articlesAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        viewFailedInternetConnection.setVisibility(View.GONE);
        lvNameOfArticles.setVisibility(View.VISIBLE);

        String parameter_for_request = null;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Variable.request_articles_url+id_of_articles, parameter_for_request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Articles articles = new Articles(jsonObject.getInt("id"),
                                                                 jsonObject.getString("name_of_article"),
                                                                 jsonObject.getString("link_to_article"));
                                articlesAdapter.add(articles);
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
        MySingleton.getInstance(ListOfArticles.this).addToRequestque(request);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Articles []array_of_articles = new Articles[articlesAdapter.getCount()];
        for(int i=0; i<articlesAdapter.getCount(); i++)
        {
            array_of_articles[i] = articlesAdapter.getItem(i);
        }
        outState.putParcelableArray("articles", array_of_articles);
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
}
