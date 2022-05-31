package com.tohelp.tohelp.lists;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tohelp.tohelp.R;
import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends ArrayAdapter<Contacts>
{

    ArrayList<Contacts> contactsArrayList;

    public ContactAdapter(@NonNull Context context)
    {
        super(context, R.layout.row_layout);
        contactsArrayList=new ArrayList<Contacts>();
    }

    public void add(Contacts object) {
        super.add(object);
        contactsArrayList.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        contactsArrayList.clear();
    }

    @Override
    public int getCount()
    {
        return contactsArrayList.size();
    }

    @Nullable
    @Override
    public Contacts getItem(int position)
    {
        return contactsArrayList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row=convertView;
        ContactHolder contactHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row= Objects.requireNonNull(layoutInflater).inflate(R.layout.row_layout, parent, false);
            contactHolder=new ContactHolder();
            contactHolder.tx_name=row.findViewById(R.id.tx_name);
            contactHolder.tx_speciality=row.findViewById(R.id.tx_speciality);
            contactHolder.tx_call_hours=row.findViewById(R.id.tx_call_hours);
            contactHolder.tx_mobile=row.findViewById(R.id.tx_mobile);
            contactHolder.img_specialist=row.findViewById(R.id.imageOfSpecialistForSearching);
            row.setTag(contactHolder);
        }
        else
        {
            contactHolder=(ContactHolder)row.getTag();
        }
        Contacts contacts=(Contacts)this.getItem(position);
        contactHolder.tx_name.setText(Objects.requireNonNull(contacts).getFullName());
        contactHolder.tx_speciality.setText(contacts.getTypeOfSpecialist());
        contactHolder.tx_call_hours.setText(Objects.requireNonNull(contacts).getCallHours());
        contactHolder.tx_mobile.setText("+7"+contacts.getMobile());
        Picasso.get()
                .load(contacts.getUrlOfPhoto())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(contactHolder.img_specialist);
        return row;
    }

    static class ContactHolder
    {
        TextView tx_name, tx_speciality, tx_call_hours, tx_mobile;
        CircleImageView img_specialist;
    }
}
