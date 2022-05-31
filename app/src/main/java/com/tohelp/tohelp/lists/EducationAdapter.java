package com.tohelp.tohelp.lists;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tohelp.tohelp.R;

import java.util.ArrayList;

public class EducationAdapter extends ArrayAdapter<Education>
{
    ArrayList<Education> educationArrayList;
    Typeface typeface;


    public EducationAdapter(@NonNull Context context)
    {
        super(context, R.layout.list_item_for_education);
        educationArrayList = new ArrayList<Education>();
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FiraSans-Bold.ttf");
    }

    @Override
    public void add(@Nullable Education object) {
        super.add(object);
        educationArrayList.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        educationArrayList.clear();
    }

    @Override
    public int getCount() {
        return educationArrayList.size();
    }

    @Nullable
    @Override
    public Education getItem(int position) {
        return educationArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        EducationHolder educationHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.list_item_for_education, parent, false);
            educationHolder = new EducationHolder();
            educationHolder.tvNameEducation = row.findViewById(R.id.tvTitleEducation);
            educationHolder.tvDescriptionEducation = row.findViewById(R.id.tvDescriptionEducation);
            row.setTag(educationHolder);
        }
        else
        {
            educationHolder = (EducationHolder) row.getTag();
        }

        Education education = (Education) this.getItem(position);
        if(education!=null) {
            educationHolder.tvNameEducation.setText(education.getTitle());
            educationHolder.tvNameEducation.setTypeface(typeface);
            educationHolder.tvDescriptionEducation.setText(education.getDescription());
        }
        return row;
    }

    static class EducationHolder
    {
        TextView tvNameEducation;
        TextView tvDescriptionEducation;
    }
}
