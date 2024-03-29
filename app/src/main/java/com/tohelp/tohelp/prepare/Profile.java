package com.tohelp.tohelp.prepare;

import android.content.Context;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.settings.Variable;

import java.util.Objects;

public class Profile
{
    Context context;
    TextInputLayout tilSurname, tilName, tilMiddlename, tilEmail, tilPhone, tilPassword, tilConfirmPassword, tilSubject, tilCity, tilNumberCheck;
    TextView tvSex, tvDateOfBirth;
    Spinner spSex, spDate, spMonth, spYear;
    CheckBox cbAgreement;

    public Profile(TextInputLayout tilPassword, TextInputLayout tilConfirmPassword)
    {
        this.tilPassword = tilPassword;
        this.tilConfirmPassword = tilConfirmPassword;
    }

    public Profile(TextInputLayout tilSurname, TextInputLayout tilName, TextInputLayout tilMiddlename, TextInputLayout tilNumberCheck)
    {
        this.tilSurname = tilSurname;
        this.tilName = tilName;
        this.tilMiddlename = tilMiddlename;
        this.tilNumberCheck = tilNumberCheck;
    }

    public Profile(Context context, TextInputLayout tilSurname, TextInputLayout tilName, TextInputLayout tilMiddlename,
                   TextInputLayout tilEmail, TextInputLayout tilPhone, TextInputLayout tilPassword, TextInputLayout tilConfirmPassword,
                   TextInputLayout tilSubject, TextInputLayout tilCity, CheckBox cbAgreement, Spinner spSex, TextView tvSex,
                   Spinner spDate, Spinner spMonth, Spinner spYear, TextView tvDateOfBirth)
    {
        this.context = context;
        this.tilSurname = tilSurname;
        this.tilName = tilName;
        this.tilMiddlename = tilMiddlename;
        this.tilEmail = tilEmail;
        this.tilPhone = tilPhone;
        this.tilPassword = tilPassword;
        this.tilConfirmPassword = tilConfirmPassword;
        this.tilSubject = tilSubject;
        this.tilCity = tilCity;
        this.cbAgreement = cbAgreement;
        this.spSex = spSex;
        this.tvSex = tvSex;
        this.spDate = spDate;
        this.spMonth = spMonth;
        this.spYear = spYear;
        this.tvDateOfBirth = tvDateOfBirth;
    }

    public Profile(Context context, TextInputLayout tilSurname, TextInputLayout tilName, TextInputLayout tilMiddlename,
                   TextInputLayout tilEmail, TextInputLayout tilPhone, TextInputLayout tilSubject, TextInputLayout tilCity,
                   Spinner spSex, TextView tvSex, Spinner spDate, Spinner spMonth, Spinner spYear, TextView tvDateOfBirth)
    {
        this.context = context;
        this.tilSurname = tilSurname;
        this.tilName = tilName;
        this.tilMiddlename = tilMiddlename;
        this.tilEmail = tilEmail;
        this.tilPhone = tilPhone;
        this.tilSubject = tilSubject;
        this.tilCity = tilCity;
        this.spSex = spSex;
        this.tvSex = tvSex;
        this.spDate = spDate;
        this.spMonth = spMonth;
        this.spYear = spYear;
        this.tvDateOfBirth = tvDateOfBirth;
    }

    private boolean validateSurname()
    {
        String surnameInput= Objects.requireNonNull(tilSurname.getEditText()).getText().toString().trim();
        if (surnameInput.isEmpty())
        {
            tilSurname.setError("Введите фамилию");
            return false;
        }
        else
        {
            tilSurname.setError("");
            return true;
        }
    }

    private boolean validateName()
    {
        String nameInput= Objects.requireNonNull(tilName.getEditText()).getText().toString().trim();
        if (nameInput.isEmpty())
        {
            tilName.setError("Введите имя");
            return false;
        }
        else
        {
            tilName.setError("");
            return true;
        }
    }

    private boolean validateMiddlename()
    {
        String middlenameInput= Objects.requireNonNull(tilMiddlename.getEditText()).getText().toString().trim();
        if (middlenameInput.isEmpty())
        {
            tilMiddlename.setError("Введите отчество");
            return false;
        }
        else
        {
            tilMiddlename.setError("");
            return  true;
        }
    }

    private boolean validateEmail()
    {
        String emailInput= Objects.requireNonNull(tilEmail.getEditText()).getText().toString().trim();
        if (emailInput.isEmpty())
        {
            tilEmail.setError("Введите почту");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            tilEmail.setError("Введен неверный адрес почты");
            return false;
        }
        else
        {
            tilEmail.setError("");
            return true;
        }
    }

    private boolean validateNumberPhone()
    {
        String NumberPhoneInput= Objects.requireNonNull(tilPhone.getEditText()).getText().toString().trim();
        if (NumberPhoneInput.isEmpty())
        {
            tilPhone.setError("Введите номер телефона");
            return false;
        }
        else if(NumberPhoneInput.length()!=15)
        {
            tilPhone.setError("Номер телефона введен не полностью");
            return false;
        }
        else if(!Variable.PHONE_PATTERN.matcher(NumberPhoneInput).matches())
        {
            tilPhone.setError("Использованы некорректные символы в номере телефона");
            return false;
        }
        else
        {
            tilPhone.setError("");
            return true;
        }
    }

    //функция для проверки введенных символов
    //если будут введены, например, русские символы, то будет выведена ошибка
    private boolean checkField(String passwordInput)
    {
        char[] array = passwordInput.toCharArray();
        boolean flag;
        for (int i=0; i<array.length; i++)
        {
            flag = false;
            for(int j=0; j<Variable.array_for_password.length; j++)
            {
                if(array[i]==Variable.array_for_password[j]) {
                    flag = true;
                    break;
                }
            }

            if(!flag) {
                return false;
            }
        }
        return true;
    }

    private  boolean validatePassword()
    {
        String password= Objects.requireNonNull(tilPassword.getEditText()).getText().toString().trim();
        if (password.isEmpty())
        {
            tilPassword.setError("Введите пароль");
            return false;
        }
        else if(password.contains(" "))
        {
            tilPassword.setError("Пароль не должен содержать пробелов");
            return false;
        }
        else if(password.length()>71)
        {
            tilPassword.setError("Пароль должен быть короче 72 символов");
            return false;
        }
        else if (!Variable.PASSWORD_PATTERN.matcher(password).matches())
        {
            tilPassword.setError("Пароль слабый");
            return false;
        }
        else if (!checkField(password))
        {
            tilPassword.setError("Пароль содержит запрещенные символы, например, кириллицу");
            return false;
        }
        else
        {
            tilPassword.setError("");
            return true;
        }
    }

    private  boolean validateConfirmPassword()
    {
        String confirmPassword= Objects.requireNonNull(tilConfirmPassword.getEditText()).getText().toString().trim();
        if (confirmPassword.isEmpty())
        {
            tilConfirmPassword.setError("Подтвердите пароль");
            return false;
        }
        else if(confirmPassword.contains(" "))
        {
            tilConfirmPassword.setError("Пароль не должен содержать пробелов");
            return false;
        }
        else if (!Variable.PASSWORD_PATTERN.matcher(confirmPassword).matches() || !checkField(confirmPassword))
        {
            tilConfirmPassword.setError("Пароль должен быть не короче 8 символов и обязательно содержать латинские буквы и цифры");
            return false;
        }
        else
        {
            tilConfirmPassword.setError("");
            return true;
        }
    }

    //проверка верности введенного региона
    private boolean checkAutoCompleteTextView()
    {
        String actvCheckSubjectOfRussianFederation=tilSubject.getEditText().getText().toString().trim();
        String[] array_subject_of_Russian_Federation = context.getResources().getStringArray(R.array.array_subjects);
        for (int i=0; i<=array_subject_of_Russian_Federation.length-1; i++)
        {
            if (actvCheckSubjectOfRussianFederation.equals(array_subject_of_Russian_Federation[i]))
            {
                return true;
            }
        }
        return false;
    }


    private boolean validateSubjectOfRussianFederation()
    {
        String subjectOfRussianFederationInput=tilSubject.getEditText().getText().toString().trim();
        if (subjectOfRussianFederationInput.isEmpty())
        {
            tilSubject.setError("Введите название региона");
            return false;
        }
        else if(!checkAutoCompleteTextView())
        {
            tilSubject.setError("Введено неверное название. Используйте варианты из выпадающего списка");
            return false;
        }
        else
        {
            tilSubject.setError("");
            return  true;
        }
    }

    private boolean validateCityOfRussianFederation()
    {
        String cityOfRussianFederationInput=tilCity.getEditText().getText().toString().trim();
        if (cityOfRussianFederationInput.isEmpty())
        {
            tilCity.setError("Введите название города");
            return false;
        }
        else
        {
            tilCity.setError("");
            return  true;
        }
    }

    private boolean validateAgreement()
    {
        if (!cbAgreement.isChecked())
        {
            cbAgreement.setError("Поставьте галочку для согласия");
            return false;
        }
        else
        {
            cbAgreement.setError(null);
            return true;
        }
    }

    private boolean validateSex()
    {
        if (spSex.getSelectedItem().toString().trim().equals("-"))
        {
            tvSex.setError("Не выбран пол");
            return false;
        }
        else
        {
            tvSex.setError(null);
            return true;
        }
    }

    private boolean validateDayOfBirth()
    {
        if (spDate.getSelectedItem().toString().trim().equals("День")|
                spMonth.getSelectedItem().toString().trim().equals("Месяц")|
                spYear.getSelectedItem().toString().trim().equals("Год"))
        {
            tvDateOfBirth.setError("Не выбран день рождения");
            return false;
        }
        else
        {
            tvDateOfBirth.setError(null);
            return true;
        }
    }

    private boolean validateCheckNumber()
    {
        String checkNumber= Objects.requireNonNull(tilNumberCheck.getEditText()).getText().toString().trim();
        if(checkNumber.isEmpty())
        {
            tilNumberCheck.setError("Введите номер");
            return false;
        }
        else if(checkNumber.length()!=8)
        {
            tilNumberCheck.setError("Некорректный регистрационный номер");
            return false;
        }
        else
        {
            tilNumberCheck.setError("");
            return true;
        }
    }

    public boolean CheckFieldsChangePassword()
    {
        return (!validatePassword() | !validateConfirmPassword());
    }

    public boolean CheckFieldsRegisterCheck()
    {
        return (!validateSurname() | !validateName() | !validateMiddlename() | !validateCheckNumber());
    }

    public boolean CheckFieldsRegister()
    {
        return (!validateSurname() | !validateName() | !validateMiddlename() |
                !validateEmail() | !validateNumberPhone() | !validatePassword() |
                !validateConfirmPassword() | !validateAgreement() | !validateDayOfBirth() |
                !validateSex() | !validateSubjectOfRussianFederation() | !validateCityOfRussianFederation());
    }

    public boolean CheckFieldsMyProfile()
    {
        return (!validateSurname() | !validateName() | !validateMiddlename() |
                !validateEmail() | !validateNumberPhone() | !validateDayOfBirth() |
                !validateSex() | !validateSubjectOfRussianFederation() | !validateCityOfRussianFederation());
    }

}
