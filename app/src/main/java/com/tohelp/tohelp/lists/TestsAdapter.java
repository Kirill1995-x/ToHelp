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

public class TestsAdapter extends ArrayAdapter<Tests>
{
    ArrayList<Tests> testsArrayList;
    Typeface typeface;

    public TestsAdapter(@NonNull Context context) {
        super(context, R.layout.list_item_for_test);
        testsArrayList = new ArrayList<Tests>();
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FiraSans-Bold.ttf");
    }

    @Override
    public void add(@Nullable Tests object) {
        super.add(object);
        testsArrayList.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        testsArrayList.clear();
    }

    @Override
    public int getCount() {
        return testsArrayList.size();
    }

    @Nullable
    @Override
    public Tests getItem(int position) {
        return testsArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        TestHolder testHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.list_item_for_test, parent, false);
            testHolder = new TestHolder();
            testHolder.tvNameOfTest = row.findViewById(R.id.tvNameOfTest);
            testHolder.tvDescriptionOfTest = row.findViewById(R.id.tvDescriptionOfTest);
            row.setTag(testHolder);
        }
        else
        {
            testHolder = (TestHolder) row.getTag();
        }

        Tests tests = (Tests) this.getItem(position);
        if(tests!=null) {
            testHolder.tvNameOfTest.setText(tests.getNameOfTest());
            testHolder.tvNameOfTest.setTypeface(typeface);
            testHolder.tvDescriptionOfTest.setText(tests.getDescriptionOfTest());
        }
        return row;
    }

    static class TestHolder
    {
        TextView tvNameOfTest;
        TextView tvDescriptionOfTest;
    }
}
