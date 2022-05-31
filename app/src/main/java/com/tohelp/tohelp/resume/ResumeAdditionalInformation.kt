package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeAdditionalInformation(context: Context, document: Document, personalCharacteristics: String, hobby: String, wishesForWork:String):
      Resume(context, document)
{
    val personalCharacteristics:String
    val hobby:String
    val wishesForWork:String

    init {
      this.personalCharacteristics = personalCharacteristics
      this.hobby = hobby
      this.wishesForWork = wishesForWork
    }

    override fun resume() {
        val paragraph = Paragraph()
        if (personalCharacteristics!="" || hobby!="" || wishesForWork!="")
        {
            //Заголовок "Дополнительная информация"
            paragraph.add(Paragraph(context.resources.getString(R.string.additional_information), headingFont))
            CreateSeparateLine(paragraph)
            //Личностные характеристики
            if(personalCharacteristics != "")paragraph.add(Paragraph(context.resources.getString(R.string.personal_characteristic) + " " + personalCharacteristics, userFont))
            //Хобби
            if(hobby != "")paragraph.add(Paragraph(context.resources.getString(R.string.hobby) + " " + hobby, userFont))
            //Пожелания к работе
            if(wishesForWork != "")paragraph.add(Paragraph(context.resources.getString(R.string.wishes_for_work)+" "+wishesForWork, userFont))
        }
        CreateSeparateLine(paragraph)
        paragraph.add(Paragraph(context.resources.getString(R.string.end_of_resume),userFont))
        CreateMargin(paragraph)
        document.add(paragraph)
    }
}