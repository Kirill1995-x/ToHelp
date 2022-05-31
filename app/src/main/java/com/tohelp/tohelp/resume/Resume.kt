package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.draw.LineSeparator
import com.tohelp.tohelp.R

open class Resume constructor(context: Context, document: Document)
{
    val context:Context
    val document:Document
    val baseFont:BaseFont
    val titleFont:Font
    val headingFont:Font
    val userFont:Font

    init {
        this.context = context
        this.document = document
        baseFont = BaseFont.createFont("/assets/fonts/PlayBold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        titleFont = Font(baseFont, 20f, Font.BOLD)
        headingFont = Font(baseFont, 16f, Font.BOLD)
        userFont = Font(baseFont, 12f)
    }

    open fun resume()
    {
        //"Резюме"
        var paragraph:Paragraph = Paragraph(Chunk(context.getResources().getString(R.string.resume), titleFont))
        paragraph.alignment = Element.ALIGN_CENTER
        document.add(paragraph)
    }

    fun CreateSeparateLine(paragraph: Paragraph)
    {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0)
        paragraph.add(lineSeparator)
        paragraph.add(Paragraph(" "))
    }

    fun CreateMargin(paragraph: Paragraph) {
        paragraph.add(Paragraph(" "))
    }

}