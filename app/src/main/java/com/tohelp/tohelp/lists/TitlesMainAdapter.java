package com.tohelp.tohelp.lists;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tohelp.tohelp.Browser;
import com.tohelp.tohelp.ListOfArticles;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.settings.Variable;

import java.util.ArrayList;

public class TitlesMainAdapter extends RecyclerView.Adapter<TitlesMainAdapter.TitlesMainViewHolder>
{
    ArrayList<TitlesMain> titlesMain;
    LayoutInflater layoutInflater;
    Context context;

    public TitlesMainAdapter(ArrayList<TitlesMain> titlesMain, Context context)
    {
        this.titlesMain=titlesMain;
        this.layoutInflater=LayoutInflater.from(context);
        this.context=context;
    }

    public class TitlesMainViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView description;
        public TitlesMainViewHolder(View view)
        {
            super(view);
            img=(ImageView)view.findViewById(R.id.image_of_article);
            description=(TextView)view.findViewById(R.id.name_of_article);
        }
    }

    @NonNull
    @Override
    public TitlesMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = layoutInflater.inflate(R.layout.list_item_for_main_activity, parent, false);
        return new TitlesMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TitlesMainViewHolder holder, int position)
    {
        TitlesMain title=titlesMain.get(position);
        holder.description.setText(title.getDescription());
        holder.img.setImageResource(title.getImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent;
                if (holder.getBindingAdapterPosition()==0) //Жилищные вопросы
                {
                    intent = new Intent(v.getContext(), ListOfArticles.class);
                    intent.putExtra("id_of_articles", "f");
                }
                else if(holder.getBindingAdapterPosition()==4) //Медицинские услуги
                {
                    intent = new Intent(v.getContext(), ListOfArticles.class);
                    intent.putExtra("id_of_articles", "k");
                }
                else if(holder.getBindingAdapterPosition()==9) //Быт
                {
                    intent = new Intent(v.getContext(), ListOfArticles.class);
                    intent.putExtra("id_of_articles", "b");
                }
                else if(holder.getBindingAdapterPosition()==10) //Финансовая грамотность
                {
                    intent = new Intent(v.getContext(), ListOfArticles.class);
                    intent.putExtra("id_of_articles", "l");
                }
                else
                {
                    intent=new Intent(v.getContext(), Browser.class);
                    String name_of_file="n"+holder.getAdapterPosition();
                    String phone_url= Variable.url_articles_phone+name_of_file+"_phone.html";
                    String tablet_url=Variable.url_articles_tablet+name_of_file+"_tablet.html";
                    intent.putExtra("url_phone", phone_url);
                    intent.putExtra("url_tablet", tablet_url);
                }
                v.getContext().startActivity(intent);
          }
        });
    }

    @Override
    public int getItemCount() {
        return titlesMain.size();
    }
}