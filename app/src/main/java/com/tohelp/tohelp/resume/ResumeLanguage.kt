package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeLanguage(context: Context, document: Document, languages:String): Resume(context, document) {

    val languages:String

    init {
        this.languages = languages
    }

    override fun resume() {
        if(languages!="") {
            val arrayLanguages: Array<String> = languages.split("block").toTypedArray();
            if (arrayLanguages.size > 0) {
                //Заголовок "Иностранные языки"
                val paragraph = Paragraph()
                paragraph.add(Paragraph(context.resources.getString(R.string.languages), headingFont))
                CreateSeparateLine(paragraph)

                for (i in 0..(arrayLanguages.size - 2)) {
                    val arrayLanguage: Array<String> = arrayLanguages[i].split("tag").toTypedArray();
                    paragraph.add(Paragraph(context.resources.getString(R.string.language) + " " + arrayLanguage[0], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.level_of_language) + " " + arrayLanguage[1], userFont))
                    CreateMargin(paragraph)
                }
                document.add(paragraph)
            }
        }
    }
}