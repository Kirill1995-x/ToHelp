package com.tohelp.tohelp.resume

import android.content.Context
import android.content.SharedPreferences
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R
import com.tohelp.tohelp.settings.Variable
import com.tohelp.tohelp.prepare.FindItemInSpinner
import com.tohelp.tohelp.prepare.GetArrayOfBirth
import java.util.*

class ResumePersonalData(context: Context, document: Document, careerObjective: String, salary: String,
                         employment: String, schedule: String, ready_for_business_trips: Boolean,
                         ready_for_moving: Boolean, having_children: Boolean, marital_status: String): Resume(context, document)
{
      val careerObjective:String
      val salary:String
      val employment:String
      val schedule:String
      val ready_for_business_trips:Boolean
      val ready_for_moving:Boolean
      val having_children:Boolean
      val marital_status:String

      init {
          this.careerObjective = careerObjective
          this.salary = salary
          this.employment = employment
          this.schedule = schedule
          this.ready_for_business_trips = ready_for_business_trips
          this.ready_for_moving = ready_for_moving
          this.having_children = having_children
          this.marital_status = marital_status
      }

    fun getResultArray(sex: String, business_trips: Boolean, moving: Boolean, marital_status: String, having_children: Boolean): Array<String>
    {
        val number_of_marital_status:Int = FindItemInSpinner("marital_status", marital_status, context).position;
        val array_business_trips_male:Array<String> = arrayOf("готов к командировкам", "не готов к командировкам")
        val array_business_trips_female:Array<String> = arrayOf("готова к командировкам", "не готова к командировкам")
        val array_moving_male:Array<String> = arrayOf("готов к переезду", "не готов к переезду")
        val array_moving_female:Array<String> = arrayOf("готова к переезду", "не готова к переезду")
        val array_marital_status_male:Array<String> = arrayOf("-", "холост", "женат", "разведен", "отдельное проживание", "вдовец")
        val array_marital_status_female:Array<String> = arrayOf("-", "не замужем", "замужем", "разведена", "отдельное проживание", "вдова")
        val array_having_children:Array<String> = arrayOf("есть дети", "нет детей")
        val array_of_result: Array<String> = arrayOf("", "", "", "")

        if (sex == "Мужской") {
            array_of_result[0] = if (business_trips) array_business_trips_male[0] else array_business_trips_male[1]
            array_of_result[1] = if (moving) array_moving_male[0] else array_moving_male[1]
            array_of_result[2] = array_marital_status_male[number_of_marital_status]
            array_of_result[3] = if (having_children) array_having_children[0] else array_having_children[1]
        } else {
            array_of_result[0] = if (business_trips) array_business_trips_female[0] else array_business_trips_female[1]
            array_of_result[1] = if (moving) array_moving_female[0] else array_moving_female[1]
            array_of_result[2] = array_marital_status_female[number_of_marital_status]
            array_of_result[3] = if (having_children) array_having_children[0] else array_having_children[1]
        }

        return array_of_result
    }


    fun computeCountOfYears(context: Context, date_of_born: String, month_of_born: String, year_of_born: String):String
    {
          //связь массивов со списками из strins.xml
          val arrayOfBirth: GetArrayOfBirth = GetArrayOfBirth()
          val arrayDate:Array<String> =  arrayOfBirth.CreateArrayDate()
          val arrayMonth:Array<String> = arrayOfBirth.CreateArrayMonth()
          val arrayYear:Array<String> = arrayOfBirth.CreateArrayYears()
          val arrayDateNumber: Array<Int> = arrayOfBirth.CreateArrayDateNumber()
          val arrayMonthNumber: Array<Int> = arrayOfBirth.CreateArrayMonthNumber()
          val arrayYearNumber: Array<Int> = arrayOfBirth.CreateArrayYearsNumber()

          var count_of_years = 0
          var date_app = 0
          var month_app = 0
          var year_app = 0

          for (i in 0..(arrayDate.size-1))
          {
              if (date_of_born == arrayDate[i])
                  date_app = arrayDateNumber[i]
          }

          for (i in 0..(arrayMonth.size-1))
          {
              if(month_of_born == arrayMonth[i])
                  month_app = arrayMonthNumber[i]
          }

          for (i in 0..(arrayYear.size-1))
          {
              if (year_of_born == arrayYear[i])
                  year_app = arrayYearNumber[i]
          }


          val calendar = GregorianCalendar()
          val date_calendar = calendar.get(Calendar.DATE)
          val month_calendar = calendar.get(Calendar.MONTH)+1
          val year_calendar = calendar.get(Calendar.YEAR)

          count_of_years =
                  if (month_app < month_calendar) {year_calendar - year_app}
                  else if (month_app == month_calendar) {if (date_app <= date_calendar) year_calendar - year_app else year_calendar - year_app - 1 }
                  else {year_calendar - year_app - 1}

          return "$count_of_years лет ($date_of_born $month_of_born $year_of_born)"
    }

    override fun resume()
    {
          val sharedPreferences:SharedPreferences = context.getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE)
          val surname:String = sharedPreferences.getString("shared_surname","").toString()
          val name:String = sharedPreferences.getString("shared_name", "").toString()
          val middlename:String = sharedPreferences.getString("shared_middlename", "").toString()
          val phoneNumber:String = "+7"+sharedPreferences.getString("shared_phone_number", "").toString()
          val email:String = sharedPreferences.getString("shared_email", "").toString()
          val city:String = sharedPreferences.getString("shared_city","").toString()
          val sex:String = sharedPreferences.getString("shared_sex","").toString()
          val date_of_born:String = sharedPreferences.getString("shared_date_of_born", "").toString()
          val month_of_born:String = sharedPreferences.getString("shared_month_of_born", "").toString()
          val year_of_born:String = sharedPreferences.getString("shared_year_of_born", "").toString()
          var paragraph:Paragraph = Paragraph()
          val array_of_result:Array<String> = getResultArray(sex, ready_for_business_trips, ready_for_moving, marital_status, having_children)
          //Заголовок "Персональные данные"
          paragraph.add(Paragraph(context.resources.getString(R.string.personal_data), headingFont))
          CreateSeparateLine(paragraph)
          //ФИО пользователя
          paragraph.add(Paragraph(context.resources.getString(R.string.full_name) + " " + surname + " " + name + " " + middlename, userFont))
          //Количество лет
          paragraph.add(Paragraph(context.resources.getString(R.string.number_of_years) + " " + computeCountOfYears(context, date_of_born, month_of_born, year_of_born), userFont))
          //Пол
          paragraph.add(Paragraph(context.resources.getString(R.string.sex) + " " + sex, userFont))
          //Номер телефона
          paragraph.add(Paragraph(context.resources.getString(R.string.phone_number) + " " + phoneNumber, userFont))
          //email
          paragraph.add(Paragraph(context.resources.getString(R.string.email_resume) + " " + email, userFont))
          //Город проживания
          paragraph.add(Paragraph(context.resources.getString(R.string.city_resume)+" "+city, userFont))
          //Желаемая должность
          if(careerObjective != "")paragraph.add(Paragraph(context.resources.getString(R.string.career_objective) + " " + careerObjective, userFont))
          //Зарплата
          if(salary != "")paragraph.add(Paragraph(context.resources.getString(R.string.salary) + " " + salary+" "+context.resources.getString(R.string.rubles), userFont))
          //Занятость
          if(employment != "")paragraph.add(Paragraph(context.resources.getString(R.string.employment) + " " + employment, userFont))
          //График работы
          if(schedule != "")paragraph.add(Paragraph(context.resources.getString(R.string.schedule) + " " + schedule, userFont))
          //Командировки
          paragraph.add(Paragraph(context.resources.getString(R.string.business_trips) + " " + array_of_result[0], userFont))
          //Переезд
          paragraph.add(Paragraph(context.resources.getString(R.string.moving) + " " + array_of_result[1], userFont))
          //Семейное положение, наличие детей
          if(array_of_result[2] != "-")paragraph.add(Paragraph(context.resources.getString(R.string.marital_status) + " " + array_of_result[2] + " , " + array_of_result[3], userFont))

          CreateMargin(paragraph)
          document.add(paragraph)
    }
}