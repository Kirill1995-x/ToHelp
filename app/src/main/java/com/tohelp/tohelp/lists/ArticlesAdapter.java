package com.tohelp.tohelp.lists;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.tohelp.tohelp.R;
import java.util.ArrayList;

public class ArticlesAdapter extends ArrayAdapter<Articles>
{
    ArrayList<Articles> articlesArrayList;
    Typeface typeface;

    public ArticlesAdapter(@NonNull Context context) {
        super(context, R.layout.list_item);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FiraSans-Bold.ttf");
        articlesArrayList = new ArrayList<Articles>();
    }

    @Override
    public void add(@Nullable Articles object) {
        super.add(object);
        articlesArrayList.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        articlesArrayList.clear();
    }

    @Override
    public int getCount() {
        return articlesArrayList.size();
    }

    @Nullable
    @Override
    public Articles getItem(int position) {
        return articlesArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ArticleHolder articleHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.list_item, parent, false);
            articleHolder = new ArticleHolder();
            articleHolder.tvNameOfArticle = row.findViewById(R.id.tvTitleOfArticle);
            row.setTag(articleHolder);
        }
        else
        {
            articleHolder = (ArticleHolder) row.getTag();
        }

        Articles articles = (Articles) this.getItem(position);
        if(articles!=null) {
            articleHolder.tvNameOfArticle.setText(articles.getNameOfArticle());
            articleHolder.tvNameOfArticle.setTypeface(typeface);
        }
        return row;
    }

    static class ArticleHolder
    {
        TextView tvNameOfArticle;
    }
}
