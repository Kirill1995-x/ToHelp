package com.tohelp.tohelp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.tohelp.tohelp.R;

public class DialogFinish extends AppCompatDialogFragment
{
    public static String title;
    public static String message;
    AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                requireActivity().finish();
            }
        });

        return builder.create();
    }
}
