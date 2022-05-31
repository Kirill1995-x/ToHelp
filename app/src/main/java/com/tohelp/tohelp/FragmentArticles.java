package com.tohelp.tohelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tohelp.tohelp.lists.TitlesMain;
import com.tohelp.tohelp.lists.TitlesMainAdapter;
import com.tohelp.tohelp.settings.Variable;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentArticles extends Fragment {

    @BindView(R.id.list_navigator)
    RecyclerView recyclerView;
    SharedPreferences preferences;
    private String titles_navigator[] = {
            "Жилищные вопросы",
            "Обращение к судебным приставам и в прокуратуру",
            "Трудоустройство",
            "Получение социальных услуг бесплатно",
            "Медицинские услуги",
            "Что такое паспорт?",
            "Планирование семьи",
            "Необходимые документы при выпуске",
            "Как себя вести при задержании полицией?",
            "Быт",
            "Финансовая грамотность"
    };

    private int drawable_main[] = {
            R.drawable.listviewfirst,
            R.drawable.listviewsecond,
            R.drawable.listviewthird,
            R.drawable.listviewfourth,
            R.drawable.listviewfifth,
            R.drawable.listviewsixth,
            R.drawable.listviewseventh,
            R.drawable.listvieweighth,
            R.drawable.listviewninth,
            R.drawable.listviewtenth,
            R.drawable.listvieweleventh
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_articles, container, false);

        //настройка ButterKnife
        ButterKnife.bind(this, v);

        //флаг для фрагмента Навигатор
        preferences= requireActivity().
                     getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putString("flag", "first_fragment").apply();

        ArrayList<TitlesMain> titlesMain=new ArrayList<TitlesMain>();
        if(requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        else recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        for (int i=0; i<titles_navigator.length; i++)
        {
            titlesMain.add(new TitlesMain(drawable_main[i], titles_navigator[i]));
        }

        TitlesMainAdapter titlesMainAdapter=new TitlesMainAdapter(titlesMain, getContext());
        recyclerView.setAdapter(titlesMainAdapter);

        return v;
    }
}
