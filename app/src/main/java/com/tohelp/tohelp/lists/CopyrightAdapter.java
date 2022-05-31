package com.tohelp.tohelp.lists;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tohelp.tohelp.Browser;
import com.tohelp.tohelp.R;

import java.util.ArrayList;


public class CopyrightAdapter extends ArrayAdapter<Copyright> {

    Context context;
    ArrayList<Copyright> copyrightArrayList;
    String url;

    private String name_of_authors[] = {
            "bongkarn thanyakij",
            "qimono",
            "Gustavo Fring",
            "Tumisu",
            "Gustavo Fring",
            "RJA1988",
            "jarmoluk",
            "Aymanejed",
            "OpenClipart-Vectors",
            "mohamed_hassan",
            "nattanan23"
    };

    private String name_of_image[] = {
            "Clipboard with pen near mini cardboard house",
            "Клуб Аукцион Закон",
            "Smiling cowokers standing in modern office",
            "Untitled",
            "Positive doctor in medical uniform talking on cellphone in clinic corridor",
            "Россия Паспорт Документ",
            "Руки Дружба Друзья Дети Ребенок",
            "Ноутбук Офис Рука Дать Бизнес",
            "Автомобиль Полиция Россия Ван",
            "Уборка Чистота Обслуживание Этаж",
            "Деньги Прибыль Финансы Бизнес"
    };

    private String link_to_image[] = {
            "https://www.canva.com/photos/MAECm5bMnpw-clipboard-with-pen-near-mini-cardboard-house/",
            "https://pixabay.com/ru/photos/клуб-аукцион-закон-символ-судья-2492013/" ,
            "https://www.canva.com/photos/MAD_BfjZf5o-smiling-coworkers-standing-in-modern-office/",
            "https://www.canva.com/photos/MADmjBtQaeA/",
            "https://www.canva.com/photos/MAD-e74RKRs-positive-doctor-in-medical-uniform-talking-on-cellphone-in-clinic-corridor/",
            "https://pixabay.com/ru/photos/россия-паспорт-документ-2442842/",
            "https://pixabay.com/ru/photos/руки-дружба-друзья-дети-ребенок-2847508/",
            "https://pixabay.com/ru/photos/ноутбук-офис-рука-дать-бизнес-3196481/",
            "https://pixabay.com/ru/vectors/автомобиль-полиция-россия-ван-1295355/",
            "https://pixabay.com/ru/illustrations/уборка-чистота-обслуживание-этаж-4594891/",
            "https://pixabay.com/ru/photos/деньги-прибыль-финансы-бизнес-2696228/"
    };

    public CopyrightAdapter(@NonNull Context context) {
        super(context, R.layout.list_item_authors);
        this.context = context;
        copyrightArrayList = new ArrayList<Copyright>();
        for(int i=0; i<name_of_authors.length; i++)
        {
            copyrightArrayList.add(new Copyright(name_of_authors[i], name_of_image[i], link_to_image[i]));
        }
    }

    @Override
    public int getCount() {
        return copyrightArrayList.size();
    }

    @Nullable
    @Override
    public Copyright getItem(int position) {
        return copyrightArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        CopyrightHolder copyrightHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.list_item_authors, parent, false);
            copyrightHolder = new CopyrightHolder();
            copyrightHolder.tvNameOfAuthor = row.findViewById(R.id.tvNameOfAuthor);
            copyrightHolder.tvNameOfImage = row.findViewById(R.id.tvNameOfImage);
            copyrightHolder.tvLinkToImage = row.findViewById(R.id.tvLinkToImage);
            row.setTag(copyrightHolder);
        }
        else
        {
            copyrightHolder = (CopyrightHolder)row.getTag();
        }

        final Copyright copyright = this.getItem(position);
        if(copyright!=null)
        {
            copyrightHolder.tvNameOfAuthor.setText(copyright.getNameOfAuthor());
            copyrightHolder.tvNameOfImage.setText(copyright.getNameOfImage());
            copyrightHolder.tvLinkToImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Browser.class);
                    intent.putExtra("url_phone", copyright.getLinkToImage());
                    intent.putExtra("url_tablet",copyright.getLinkToImage());
                    context.startActivity(intent);
                }
            });
        }

        return row;
    }

    static class CopyrightHolder
    {
        TextView tvNameOfAuthor;
        TextView tvNameOfImage;
        TextView tvLinkToImage;
    }
}
