package com.tohelp.tohelp.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.settings.Variable;
import com.tohelp.tohelp.Register;
import com.tohelp.tohelp.RegisterCheck;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogCheckAge extends AppCompatDialogFragment implements View.OnClickListener
{
    @BindView(R.id.btnYes)
    Button btnYes;
    @BindView(R.id.btnNo)
    Button btnNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_check_age, null);
        //настрока ButterKnife
        ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        return builder.create();
    }

    private void clickYes()
    {
        Intent intent = new Intent(getActivity(), Register.class);
        startActivity(intent);
        requireActivity().getSharedPreferences(Variable.APP_NOTIFICATIONS, Context.MODE_PRIVATE).edit().putBoolean("check_age", true).apply();
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void clickNo()
    {
        Intent intent = new Intent(getActivity(), RegisterCheck.class);
        startActivity(intent);
        requireActivity().getSharedPreferences(Variable.APP_NOTIFICATIONS, Context.MODE_PRIVATE).edit().putBoolean("check_age", false).apply();
        Objects.requireNonNull(getDialog()).dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnYes:
                clickYes();
                break;
            case R.id.btnNo:
                clickNo();
                break;
            default:
                break;
        }
    }
}
