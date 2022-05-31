package com.tohelp.tohelp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.tohelp.tohelp.R;

import java.util.Objects;

public class DialogSimple extends AppCompatDialogFragment
{
    public static String title;
    public static String message;
    AlertDialog.Builder builder;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState)
    {
        builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });

        return builder.create();
    }
}
