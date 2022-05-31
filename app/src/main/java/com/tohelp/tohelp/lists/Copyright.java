package com.tohelp.tohelp.lists;

public class Copyright
{
    String name_of_author;
    String name_of_image;
    String link_to_image;

    public Copyright(String name_of_author, String name_of_image, String link_to_image) {
        this.name_of_author = name_of_author;
        this.name_of_image = name_of_image;
        this.link_to_image = link_to_image;
    }

    public String getNameOfAuthor() {
        return name_of_author;
    }

    public String getNameOfImage()
    {
        return name_of_image;
    }

    public String getLinkToImage() {
        return link_to_image;
    }
}
