package com.tohelp.tohelp.resume;

import android.os.Parcel;
import android.os.Parcelable;

public class DataForResume implements Parcelable
{
    //Персональные данные
    String career_objective, salary, employment, schedule, marital_status;
    Boolean business_trips, moving, having_children;
    //Основные навыки
    String basic_skills, computer_skills, program;
    Boolean military_service;
    Integer drivers_licences;
    //Опыт работы
    String work;
    //Образование
    String education;
    //Курсы
    String courses;
    //Проекты
    String projects;
    //Языки
    String languages;
    //Дополнительно
    String personal_characteristics, hobby, wishes_for_work;

    public DataForResume(String career_objective, String salary, String employment, String schedule, String marital_status,
                         boolean business_trips, boolean moving, boolean having_children,
                         //---
                         String basic_skills, String computer_skills, String program, boolean military_service, int drivers_licences,
                         //---
                         String work, String education, String courses, String projects, String languages,
                         //---
                         String personal_characteristics, String hobby, String wishes_for_work) {
        this.career_objective = career_objective;
        this.salary = salary;
        this.employment = employment;
        this.schedule = schedule;
        this.business_trips = business_trips;
        this.moving = moving;
        this.having_children = having_children;
        this.marital_status = marital_status;
        this.basic_skills = basic_skills;
        this.computer_skills = computer_skills;
        this.program = program;
        this.military_service = military_service;
        this.drivers_licences = drivers_licences;
        this.work = work;
        this.education = education;
        this.courses = courses;
        this.projects = projects;
        this.languages = languages;
        this.personal_characteristics = personal_characteristics;
        this.hobby = hobby;
        this.wishes_for_work = wishes_for_work;
    }

    protected DataForResume(Parcel in) {
        career_objective = in.readString();
        salary = in.readString();
        employment = in.readString();
        schedule = in.readString();
        business_trips = in.readByte() != 0;
        moving = in.readByte() != 0;
        having_children = in.readByte() != 0;
        marital_status = in.readString();
        basic_skills = in.readString();
        computer_skills = in.readString();
        program = in.readString();
        military_service = in.readByte() != 0;
        drivers_licences = in.readInt();
        work = in.readString();
        education = in.readString();
        courses = in.readString();
        projects = in.readString();
        languages = in.readString();
        personal_characteristics = in.readString();
        hobby = in.readString();
        wishes_for_work = in.readString();
    }

    public static final Creator<DataForResume> CREATOR = new Creator<DataForResume>() {
        @Override
        public DataForResume createFromParcel(Parcel in) {
            return new DataForResume(in);
        }

        @Override
        public DataForResume[] newArray(int size) {
            return new DataForResume[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(career_objective);
        dest.writeString(salary);
        dest.writeString(employment);
        dest.writeString(schedule);
        dest.writeByte((byte) (business_trips ? 1 : 0));
        dest.writeByte((byte) (moving ? 1 : 0));
        dest.writeByte((byte) (having_children ? 1 : 0));
        dest.writeString(marital_status);
        dest.writeString(basic_skills);
        dest.writeString(computer_skills);
        dest.writeString(program);
        dest.writeByte((byte) (military_service ? 1 : 0));
        dest.writeInt(drivers_licences);
        dest.writeString(work);
        dest.writeString(education);
        dest.writeString(courses);
        dest.writeString(projects);
        dest.writeString(languages);
        dest.writeString(personal_characteristics);
        dest.writeString(hobby);
        dest.writeString(wishes_for_work);
    }

    public String getCareerObjective() {
        return (career_objective.isEmpty())?"":career_objective;
    }

    public String getSalary() {
        return (salary.isEmpty())?"":salary;
    }

    public String getEmployment() {
        return (employment.isEmpty())?"":employment;
    }

    public String getSchedule() {
        return (schedule.isEmpty())?"":schedule;
    }

    public Boolean isBusinessTrips() {
        return (business_trips == null)?false:business_trips;
    }

    public Boolean isMoving() {
        return (moving == null)?false:moving;
    }

    public Boolean isHavingChildren() {
        return (having_children == null)?false:having_children;
    }

    public String getMaritalStatus() {
        return (marital_status.isEmpty())?"":marital_status;
    }

    public String getBasicSkills() {
        return (basic_skills.isEmpty())?"":basic_skills;
    }

    public String getComputerSkills() {
        return (computer_skills.isEmpty())?"":computer_skills;
    }

    public String getProgram() {
        return (program.isEmpty())?"":program;
    }

    public Boolean isMilitaryService() {
        return (military_service == null)?false:military_service;
    }

    public Integer getDriversLicences() {
        return (drivers_licences == null)?0:drivers_licences;
    }

    public String getWork() {
        return (work.isEmpty())?"":work;
    }

    public String getEducation() {
        return (education.isEmpty())?"":education;
    }

    public String getCourses() {
        return (courses.isEmpty())?"":courses;
    }

    public String getProjects() {
        return (projects.isEmpty())?"":projects;
    }

    public String getLanguages() {
        return (languages.isEmpty())?"":languages;
    }

    public String getPersonalCharacteristics() {
        return (personal_characteristics.isEmpty())?"":personal_characteristics;
    }

    public String getHobby() {
        return (hobby.isEmpty())?"":hobby;
    }

    public String getWishesForWork() {
        return (wishes_for_work.isEmpty())?"":wishes_for_work;
    }
}
