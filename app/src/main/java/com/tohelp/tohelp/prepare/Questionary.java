package com.tohelp.tohelp.prepare;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.tohelp.tohelp.R;

import java.util.Objects;

public class Questionary implements Parcelable
{
    private Context context;
    TextInputLayout tilMyTargets, tilEducationProblem, tilFlatProblem, tilMoneyProblem, tilLawProblem,
                    tilOtherProblem, tilEducationInstitution, tilProfessional, tilInterests;
    TextView tvLevelOfEducation;
    Spinner spLevelOfEducation;
    private String main_target, problem_education, problem_flat, problem_money, problem_law, problem_other,
                   name_education_institution, level_of_education, my_professional, my_interests, status_of_questionary;

    public Questionary(Context context, String main_target, String problem_education, String problem_flat, String problem_money,
                       String problem_law, String problem_other,String name_education_institution, String level_of_education,
                       String my_professional, String my_interests, String status_of_questionary) {
        this.context = context;
        this.main_target = main_target;
        this.problem_education = problem_education;
        this.problem_flat = problem_flat;
        this.problem_money = problem_money;
        this.problem_law = problem_law;
        this.problem_other = problem_other;
        this.name_education_institution = name_education_institution;
        this.level_of_education = level_of_education;
        this.my_professional = my_professional;
        this.my_interests = my_interests;
        this.status_of_questionary = status_of_questionary;
    }

    public Questionary(Context context, TextInputLayout tilMyTargets, TextInputLayout tilEducationProblem, TextInputLayout tilFlatProblem,
                       TextInputLayout tilMoneyProblem, TextInputLayout tilLawProblem, TextInputLayout tilOtherProblem,
                       TextInputLayout tilEducationInstitution, Spinner spLevelOfEducation, TextView tvLevelOfEducation,
                       TextInputLayout tilProfessional, TextInputLayout tilInterests)
    {
        this.context = context;
        this.tilMyTargets = tilMyTargets;
        this.tilEducationProblem = tilEducationProblem;
        this.tilFlatProblem = tilFlatProblem;
        this.tilMoneyProblem = tilMoneyProblem;
        this.tilLawProblem = tilLawProblem;
        this.tilOtherProblem = tilOtherProblem;
        this.tilEducationInstitution = tilEducationInstitution;
        this.spLevelOfEducation = spLevelOfEducation;
        this.tvLevelOfEducation = tvLevelOfEducation;
        this.tilProfessional = tilProfessional;
        this.tilInterests = tilInterests;
    }

    protected Questionary(Parcel in) {
        main_target = in.readString();
        problem_education = in.readString();
        problem_flat = in.readString();
        problem_money = in.readString();
        problem_law = in.readString();
        problem_other = in.readString();
        name_education_institution = in.readString();
        level_of_education = in.readString();
        my_professional = in.readString();
        my_interests = in.readString();
        status_of_questionary = in.readString();
    }

    public static final Creator<Questionary> CREATOR = new Creator<Questionary>() {
        @Override
        public Questionary createFromParcel(Parcel in) {
            return new Questionary(in);
        }

        @Override
        public Questionary[] newArray(int size) {
            return new Questionary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main_target);
        dest.writeString(problem_education);
        dest.writeString(problem_flat);
        dest.writeString(problem_money);
        dest.writeString(problem_law);
        dest.writeString(problem_other);
        dest.writeString(name_education_institution);
        dest.writeString(level_of_education);
        dest.writeString(my_professional);
        dest.writeString(my_interests);
        dest.writeString(status_of_questionary);
    }

    public String getMainTarget() {
        return main_target;
    }

    public void setMainTarget(String main_target) {
        this.main_target = main_target;
    }

    public String getProblemEducation() {
        return problem_education;
    }

    public void setProblemEducation(String problem_education) {
        this.problem_education = problem_education;
    }

    public String getProblemFlat() {
        return problem_flat;
    }

    public void setProblemFlat(String problem_flat) {
        this.problem_flat = problem_flat;
    }

    public String getProblemMoney() {
        return problem_money;
    }

    public void setProblemMoney(String problem_money) {
        this.problem_money = problem_money;
    }

    public String getProblemLaw() {
        return problem_law;
    }

    public void setProblemLaw(String problem_law) {
        this.problem_law = problem_law;
    }

    public String getProblemOther() {
        return problem_other;
    }

    public void setProblemOther(String problem_other) {
        this.problem_other = problem_other;
    }

    public String getNameEducationInstitution() {
        return name_education_institution;
    }

    public void setNameEducationInstitution(String name_education_institution) {
        this.name_education_institution = name_education_institution;
    }

    public int getLevelOfEducation() {
        return new FindItemInSpinner("level_of_education", level_of_education, context).getPosition();
    }

    public String getLevelOfEducationWord()
    {
        return level_of_education;
    }

    public void setLevelOfEducation(int number_of_level_of_education) {
        String[] array_level_of_education = context.getResources().getStringArray(R.array.array_level_education);
        level_of_education = array_level_of_education[number_of_level_of_education];
    }

    public String getMyProfessional() {
        return my_professional;
    }

    public void setMyProfessional(String my_professional) {
        this.my_professional = my_professional;
    }

    public String getMyInterests() {
        return my_interests;
    }

    public void setMyInterests(String my_interests) {
        this.my_interests = my_interests;
    }

    public int getStatusOfQuestionary() {
        return (status_of_questionary.equals("1"))? View.GONE:View.VISIBLE;
    }

    public void setStatusOfQuestionary(int id_of_status) {
        status_of_questionary=(id_of_status==View.GONE)?"1":"0";
    }

    private boolean validateMainTarget()
    {
        String MyTargets= Objects.requireNonNull(tilMyTargets.getEditText()).getText().toString().trim();
        if (MyTargets.isEmpty())
        {
            tilMyTargets.setError("Опишите Ваши цели на ближайший год");
            return false;
        }
        else
        {
            tilMyTargets.setError(null);
            return true;
        }
    }

    private boolean validateProblemEducation()
    {
        String EducationProblem= Objects.requireNonNull(tilEducationProblem.getEditText()).getText().toString().trim();
        if (EducationProblem.isEmpty())
        {
            tilEducationProblem.setError("Расскажите о проблемах с учебой");
            return false;
        }
        else
        {
            tilEducationProblem.setError(null);
            return true;
        }
    }

    private boolean validateProblemFlat()
    {
        String FlatProblem= Objects.requireNonNull(tilFlatProblem.getEditText()).getText().toString().trim();
        if (FlatProblem.isEmpty())
        {
            tilFlatProblem.setError("Расскажите о проблемах с жильем");
            return false;
        }
        else
        {
            tilFlatProblem.setError(null);
            return true;
        }
    }

    private boolean validateProblemMoney()
    {
        String MoneyProblem= Objects.requireNonNull(tilMoneyProblem.getEditText()).getText().toString().trim();
        if (MoneyProblem.isEmpty())
        {
            tilMoneyProblem.setError("Расскажите о денежных проблемах");
            return false;
        }
        else
        {
            tilMoneyProblem.setError(null);
            return true;
        }
    }

    private boolean validateProblemLaw()
    {
        String LawProblem= Objects.requireNonNull(tilLawProblem.getEditText()).getText().toString().trim();
        if (LawProblem.isEmpty())
        {
            tilLawProblem.setError("Расскажите о проблемах с законом");
            return false;
        }
        else
        {
            tilLawProblem.setError(null);
            return true;
        }
    }

    private boolean validateProblemOther()
    {
        String OtherProblem= Objects.requireNonNull(tilOtherProblem.getEditText()).getText().toString().trim();
        if (OtherProblem.isEmpty())
        {
            tilOtherProblem.setError("Расскажите о других проблемах");
            return false;
        }
        else
        {
            tilOtherProblem.setError(null);
            return true;
        }
    }

    private boolean validateNameEducationInstitution()
    {
        String EducationInstitution= Objects.requireNonNull(tilEducationInstitution.getEditText()).getText().toString().trim();
        if (EducationInstitution.isEmpty())
        {
            tilEducationInstitution.setError("Укажите название учебного заведения");
            return false;
        }
        else
        {
            tilEducationInstitution.setError(null);
            return true;
        }
    }

    private boolean validateLevelOfEducation()
    {
        if (spLevelOfEducation.getSelectedItem().toString().trim().equals("-"))
        {
            tvLevelOfEducation.setError("Не указан уровень образования");
            return false;
        }
        else
        {
            tvLevelOfEducation.setError(null);
            return true;
        }
    }

    private boolean validateMyProfessional()
    {
        String MyProfessional= Objects.requireNonNull(tilProfessional.getEditText()).getText().toString().trim();
        if (MyProfessional.isEmpty())
        {
            tilProfessional.setError("Укажите Вашу профессию");
            return false;
        }
        else
        {
            tilProfessional.setError(null);
            return true;
        }
    }

    private boolean validateMyInterests()
    {
        String MyInterests= Objects.requireNonNull(tilInterests.getEditText()).getText().toString().trim();
        if (MyInterests.isEmpty())
        {
            tilInterests.setError("Опишите Ваши интересы");
            return false;
        }
        else
        {
            tilInterests.setError(null);
            return true;
        }
    }

    public boolean checkFieldsQuestionary()
    {
        return (!validateMainTarget() | !validateProblemEducation() | !validateProblemFlat() | !validateProblemMoney() |
                !validateProblemLaw() | !validateProblemOther() | !validateNameEducationInstitution() |
                !validateLevelOfEducation() | !validateMyProfessional() | !validateMyInterests());
    }
}
