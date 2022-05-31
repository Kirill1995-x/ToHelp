package com.tohelp.tohelp.resume;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.tohelp.tohelp.R;

import java.util.Objects;

public class CheckResume
{
    Context context;
    TextInputLayout tilCareerObjective, tilSalary, tilPersonalCharacteristics, tilHobby, tilWishesForWork,
                    tilBasicSkills, tilProgram;
    TextView tvEmployment, tvSchedule, tvMaritalStatus, tvComputerSkills;
    Spinner spEmployment, spSchedule, spMaritalStatus, spComputerSkills;
    LinearLayout llShow;

    //персональные данные
    public CheckResume(Context context, TextInputLayout tilCareerObjective, TextInputLayout tilSalary, TextView tvEmployment, Spinner spEmployment,
                       TextView tvSchedule, Spinner spSchedule, TextView tvMaritalStatus, Spinner spMaritalStatus)
    {
        this.context = context;
        this.tilCareerObjective = tilCareerObjective;
        this.tilSalary = tilSalary;
        this.tvEmployment = tvEmployment;
        this.tvSchedule = tvSchedule;
        this.tvMaritalStatus = tvMaritalStatus;
        this.spEmployment = spEmployment;
        this.spSchedule = spSchedule;
        this.spMaritalStatus = spMaritalStatus;
    }

    //основные навыки
    public CheckResume(Context context, TextInputLayout tilBasicSkills, TextInputLayout tilProgram,
                       TextView tvComputerSkills, Spinner spComputerSkills)
    {
        this.context = context;
        this.tilBasicSkills = tilBasicSkills;
        this.tilProgram = tilProgram;
        this.tvComputerSkills = tvComputerSkills;
        this.spComputerSkills = spComputerSkills;
    }

    //опыт работы, образование, курсы, проекты, иностраннные языки
    public CheckResume(LinearLayout llShow)
    {
        this.llShow = llShow;
    }

    //дополнительная информация
    public CheckResume(Context context, TextInputLayout tilPersonalCharacteristics, TextInputLayout tilHobby, TextInputLayout tilWishesForWork)
    {
        this.context = context;
        this.tilPersonalCharacteristics = tilPersonalCharacteristics;
        this.tilHobby = tilHobby;
        this.tilWishesForWork = tilWishesForWork;
    }

    //персональные данные
    private boolean validateCareerObjective()
    {
        if(Objects.requireNonNull(tilCareerObjective.getEditText()).getText().toString().trim().isEmpty())
        {
            tilCareerObjective.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilCareerObjective.setError(null);
            return true;
        }
    }

    private boolean validateSalary()
    {
        if(Objects.requireNonNull(tilSalary.getEditText()).getText().toString().trim().isEmpty())
        {
            tilSalary.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilSalary.setError(null);
            return true;
        }
    }

    private boolean validateEmployment()
    {
        if(spEmployment.getSelectedItem().toString().equals("-"))
        {
            tvEmployment.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tvEmployment.setError(null);
            return true;
        }
    }

    private boolean validateSchedule()
    {
        if(spSchedule.getSelectedItem().toString().equals("-"))
        {
            tvSchedule.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tvSchedule.setError(null);
            return true;
        }
    }

    private boolean validateMaritalStatus()
    {
        if(spMaritalStatus.getSelectedItem().toString().equals("-"))
        {
            tvMaritalStatus.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tvMaritalStatus.setError(null);
            return true;
        }
    }

    //основные навыки
    private boolean validateBasicSkills()
    {
        if(Objects.requireNonNull(tilBasicSkills.getEditText()).getText().toString().trim().isEmpty())
        {
            tilBasicSkills.setError("Поле не заполнено");
            return  false;
        }
        else
        {
            tilBasicSkills.setError(null);
            return true;
        }
    }

    private boolean validateProgram()
    {
        if(Objects.requireNonNull(tilProgram.getEditText()).getText().toString().trim().isEmpty())
        {
            tilProgram.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilProgram.setError(null);
            return true;
        }
    }

    private boolean validateComputerSkills()
    {
        if(spComputerSkills.getSelectedItem().toString().trim().equals("-"))
        {
            tvComputerSkills.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tvComputerSkills.setError(null);
            return true;
        }
    }

    //опыт работы
    private boolean validateOldOrganization()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            EditText etOldOrganization = view.findViewById(R.id.etOldOrganization);
            if(etOldOrganization.getText().toString().equals(""))
            {
                result=false;
                etOldOrganization.setError("Введите название организации");
            }
            else
            {
                etOldOrganization.setError(null);
            }
        }
        return result;
    }

    private boolean validateOldPosition()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            EditText etOldPosition = view.findViewById(R.id.etOldPosition);
            if(etOldPosition.getText().toString().equals(""))
            {
                result=false;
                etOldPosition.setError("Введите название должности");
            }
            else
            {
                etOldPosition.setError(null);
            }
        }
        return result;
    }

    private boolean validateOldResponsibility()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            EditText etOldResponsibility = view.findViewById(R.id.etOldResponsibility);
            if(etOldResponsibility.getText().toString().equals(""))
            {
                result=false;
                etOldResponsibility.setError("Опишите свои обязанности");
            }
            else
            {
                etOldResponsibility.setError(null);
            }
        }
        return result;
    }

    private boolean validateOldEmployment()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            TextView tvOldEmployment = view.findViewById(R.id.tvOldEmployment);
            Spinner spOldEmployment = view.findViewById(R.id.spOldEmployment);
            if(spOldEmployment.getSelectedItem().toString().equals("-"))
            {
                result=false;
                tvOldEmployment.setError("Поле не заполнено");
            }
            else
            {
                tvOldEmployment.setError(null);
            }
        }
        return result;
    }

    private boolean validateStartOfWork()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            TextView tvStartOfWork = view.findViewById(R.id.tvStartOfWork);
            Spinner spStartOfWorkMonth = view.findViewById(R.id.spStartOfWorkPeriodMonth);
            Spinner spStartOfWorkYear = view.findViewById(R.id.spStartOfWorkPeriodYear);
            if(spStartOfWorkMonth.getSelectedItem().toString().equals("Месяц") || spStartOfWorkYear.getSelectedItem().toString().equals("Год"))
            {
                result=false;
                tvStartOfWork.setError("Начало рабочего периода не указано");
            }
            else
            {
                tvStartOfWork.setError(null);
            }
        }
        return result;
    }

    private boolean validateEndOfWork()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            TextView tvEndOfWork = view.findViewById(R.id.tvEndOfWork);
            CheckBox cbWorkNow = view.findViewById(R.id.cbWorkNow);
            Spinner spEndOfWorkMonth = view.findViewById(R.id.spEndOfWorkPeriodMonth);
            Spinner spEndOfWorkYear = view.findViewById(R.id.spEndOfWorkPeriodYear);
            if(cbWorkNow.isChecked() || (!spEndOfWorkMonth.getSelectedItem().toString().equals("Месяц") && !spEndOfWorkYear.getSelectedItem().toString().equals("Год")))
            {
                tvEndOfWork.setError(null);
            }
            else
            {
                result=false;
                tvEndOfWork.setError("Конец рабочего периода не указан");
            }
        }
        return result;
    }

    //образование
    private boolean validateNameOfEducation()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            EditText etNameOfEducation = view.findViewById(R.id.etNameOfEducation);
            if(etNameOfEducation.getText().toString().equals(""))
            {
                result=false;
                etNameOfEducation.setError("Введите название учебного заведения");
            }
            else
            {
                etNameOfEducation.setError(null);
            }
        }
        return result;
    }

    private boolean validateStartAndEndOfEducation()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            TextView tvStartAndEndOfEducation = view.findViewById(R.id.tvStartAndEndOfEducation);
            Spinner spStartOfEducation = view.findViewById(R.id.spStartOfEducation);
            Spinner spEndOfEducation = view.findViewById(R.id.spEndOfEducation);
            if(spStartOfEducation.getSelectedItem().toString().equals("Год") || spEndOfEducation.getSelectedItem().toString().equals("Год"))
            {
                result=false;
                tvStartAndEndOfEducation.setError("Укажите года начала и конца обучения");
            }
            else
            {
                tvStartAndEndOfEducation.setError(null);
            }
        }
        return result;
    }

    private boolean validateLevelOfEducation()
    {
        boolean result = true;
        for(int i=0; i<llShow.getChildCount();i++)
        {
            View view = llShow.getChildAt(i);
            TextView tvLevelOfEducation = view.findViewById(R.id.tvLevelOfEducationResume);
            Spinner spLevelOfEducation = view.findViewById(R.id.spLevelOfEducationResume);
            if(spLevelOfEducation.getSelectedItem().toString().equals("-"))
            {
                result=false;
                tvLevelOfEducation.setError("Не указан уровень образования");
            }
            else
            {
                tvLevelOfEducation.setError(null);
            }
        }
        return result;
    }

    //курсы
    private boolean validateCourses()
    {
        boolean course = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etCourse = view.findViewById(R.id.etCourse);
            if(etCourse.getText().toString().isEmpty())
            {
                etCourse.setError("Введите название курса");
                course=false;
            }
            else {
                etCourse.setError(null);
            }
        }
        return course;
    }

    private boolean validateDescribeCourses()
    {
        boolean describe_courses = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etDescribeCourses = view.findViewById(R.id.etDescribeCourse);
            if (etDescribeCourses.getText().toString().isEmpty())
            {
                etDescribeCourses.setError("Расскажите про полученные навыки");
                describe_courses=false;
            }
            else{
                etDescribeCourses.setError(null);
            }
        }
        return describe_courses;
    }

    //проекты
    private boolean validateProjects()
    {
        boolean project = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etProject = view.findViewById(R.id.etProject);
            if(etProject.getText().toString().isEmpty())
            {
                etProject.setError("Введите название проекта");
                project=false;
            }
            else {
                etProject.setError(null);
            }
        }
        return project;
    }

    private boolean validateDescribeProjects()
    {
        boolean describe_project = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etDescribeProject = view.findViewById(R.id.etDescribeProject);
            if (etDescribeProject.getText().toString().isEmpty())
            {
                etDescribeProject.setError("Опишите проект");
                describe_project=false;
            }
            else{
                etDescribeProject.setError(null);
            }
        }
        return describe_project;
    }

    //иностранные языки
    private boolean validateLanguages()
    {
        boolean language = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etLanguage = view.findViewById(R.id.etLanguage);
            if(etLanguage.getText().toString().isEmpty())
            {
                etLanguage.setError("Введите язык");
                language=false;
            }
            else {
                etLanguage.setError(null);
            }
        }
        return language;
    }

    private boolean validateLevelOfLanguages()
    {
        boolean level_of_language = true;
        for (int i = 0; i < llShow.getChildCount(); i++)
        {
            View view = llShow.getChildAt(i);
            EditText etLevelOfLanguage = view.findViewById(R.id.etLevelOfLanguage);
            if (etLevelOfLanguage.getText().toString().isEmpty())
            {
                etLevelOfLanguage.setError("Укажите уровень владения зыком");
                level_of_language=false;
            }
            else{
                etLevelOfLanguage.setError(null);
            }
        }
        return level_of_language;
    }

    //дополнительно
    private boolean validatePersonalCharacteristics()
    {
        if(Objects.requireNonNull(tilPersonalCharacteristics.getEditText()).getText().toString().trim().isEmpty())
        {
            tilPersonalCharacteristics.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilPersonalCharacteristics.setError(null);
            return true;
        }
    }

    private boolean validateHobby()
    {
        if(Objects.requireNonNull(tilHobby.getEditText()).getText().toString().trim().isEmpty())
        {
            tilHobby.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilHobby.setError(null);
            return true;
        }
    }

    private boolean validateWishesForWork()
    {
        if(Objects.requireNonNull(tilWishesForWork.getEditText()).getText().toString().trim().isEmpty())
        {
            tilWishesForWork.setError("Поле не заполнено");
            return false;
        }
        else
        {
            tilWishesForWork.setError(null);
            return true;
        }
    }

    public boolean CheckFieldsPersonalData()
    {
        return (!validateCareerObjective() | !validateSalary() | !validateEmployment() | !validateSchedule() | !validateMaritalStatus());
    }

    public boolean CheckFieldsMainSkills()
    {
        return (!validateBasicSkills() | !validateProgram() | !validateComputerSkills());
    }

    public boolean CheckFieldsWorkExperience()
    {
        return (!validateOldOrganization() | !validateOldPosition() | !validateOldResponsibility() | !validateOldEmployment() | !validateStartOfWork() | !validateEndOfWork());
    }

    public boolean CheckFieldsEducation()
    {
        return (!validateNameOfEducation() | !validateStartAndEndOfEducation() | !validateLevelOfEducation());
    }

    public boolean CheckFieldsCourses()
    {
        return (!validateCourses() | !validateDescribeCourses());
    }

    public boolean CheckFieldsProjects()
    {
        return (!validateProjects() | !validateDescribeProjects());
    }

    public boolean CheckFieldsLanguages()
    {
        return (!validateLanguages() | !validateLevelOfLanguages());
    }

    public boolean CheckFieldsAdditionalInformation()
    {
        return (!validatePersonalCharacteristics() | !validateHobby() | !validateWishesForWork());
    }
}
