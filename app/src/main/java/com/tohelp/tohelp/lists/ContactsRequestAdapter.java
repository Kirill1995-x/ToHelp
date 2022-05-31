package com.tohelp.tohelp.lists;

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


public class ContactsRequestAdapter extends ArrayAdapter<ContactsRequest>
{

    ArrayList<ContactsRequest> contactsRequestArrayList;

    public ContactsRequestAdapter(@NonNull Context context) {
        super(context, R.layout.row_request);
        contactsRequestArrayList = new ArrayList<ContactsRequest>();
    }

    @Override
    public void add(@Nullable ContactsRequest object) {
        super.add(object);
        contactsRequestArrayList.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        contactsRequestArrayList.clear();

    }

    @Override
    public int getCount() {
        return contactsRequestArrayList.size();
    }

    @Nullable
    @Override
    public ContactsRequest getItem(int position) {
        return contactsRequestArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row=convertView;
        ContactsRequestHolder contactsRequestHolder;
        if(row==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row= Objects.requireNonNull(layoutInflater).inflate(R.layout.row_request, parent, false);
            contactsRequestHolder=new ContactsRequestHolder();
            contactsRequestHolder.fullname=row.findViewById(R.id.fullname_of_specialist);
            contactsRequestHolder.speciality=row.findViewById(R.id.speciality);
            contactsRequestHolder.status=row.findViewById(R.id.status_of_request);
            contactsRequestHolder.imageOfAccount=row.findViewById(R.id.imageOfSpecialistForRequest);
            row.setTag(contactsRequestHolder);
        }
        else
        {
            contactsRequestHolder=(ContactsRequestHolder) row.getTag();
        }

        ContactsRequest contactsRequest=(ContactsRequest) this.getItem(position);

        contactsRequestHolder.fullname.setText(Objects.requireNonNull(contactsRequest).getFullname());
        contactsRequestHolder.speciality.setText(contactsRequest.getTypeOfSpecialist());
        if(Objects.requireNonNull(contactsRequest).getStatus().equals("2"))
        {
            contactsRequestHolder.status.setText("Запрос в работе");
        }
        else
        {
            contactsRequestHolder.status.setText("Запрос не взят в работу");
        }

        Picasso.get()
                .load(contactsRequest.getUrlOfPhoto())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(contactsRequestHolder.imageOfAccount);

        return row;
    }

    static class ContactsRequestHolder
    {
        TextView fullname, status, speciality;
        CircleImageView imageOfAccount;
    }
}
