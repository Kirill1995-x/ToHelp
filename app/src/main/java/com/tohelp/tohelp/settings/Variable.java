package com.tohelp.tohelp.settings;
import com.tohelp.tohelp.BuildConfig;

import java.util.regex.Pattern;

public class Variable
{
    public static final String store="play_market";
    //public static final String store="app_gallery";
    public static final String APP_PREFERENCES="my_profile_data";
    public static final String APP_NOTIFICATIONS="my_notifications";
    public static final String url= BuildConfig.URL;
    public static final String url_articles_phone=url+"articles_phone/";
    public static final String url_articles_tablet=url+"articles_tablet/";
    public static final String forgot_password_url=url+"forgot_password.php";
    public static final String login_url=url+"login.php";
    public static final String register_url=url+"register.php";
    public static final String send_my_profile_url=url+"send_my_profile.php";
    public static final String send_questionary_url=url+"send_questionary.php";
    public static final String send_password_url=url+"send_password.php";
    public static final String send_resume_additional_information_url=url+"send_resume_additional_information.php";
    public static final String send_resume_education_url=url+"send_resume_education.php";
    public static final String send_resume_main_skills_url=url+"send_resume_main_skills.php";
    public static final String send_resume_personal_data_url=url+"send_resume_personal_data.php";
    public static final String send_resume_work_experience_url=url+"send_resume_work_experience.php";
    public static final String send_resume_courses_url=url+"send_resume_courses.php";
    public static final String send_resume_projects_url=url+"send_resume_projects.php";
    public static final String send_resume_languages_url=url+"send_resume_languages.php";
    public static final String get_my_profile_url=url+"get_my_profile.php";
    public static final String get_resume_all_data_url=url+"get_resume_all_data.php";
    public static final String get_resume_additional_information_url=url+"get_resume_additional_information.php";
    public static final String get_resume_education_url=url+"get_resume_education.php";
    public static final String get_resume_main_skills_url=url+"get_resume_main_skills.php";
    public static final String get_resume_personal_data_url=url+"get_resume_personal_data.php";
    public static final String get_resume_work_experience_url=url+"get_resume_work_experience.php";
    public static final String get_resume_courses_url=url+"get_resume_courses.php";
    public static final String get_resume_projects_url=url+"get_resume_projects.php";
    public static final String get_resume_languages_url=url+"get_resume_languages.php";
    public static final String compare_information_url=url+"compare_information.php";
    public static final String compare_password_url=url+"compare_password.php";
    //----------------------------------------------------------------------------------------------------------------------
    public static final String requests_specialist_define_url=url+"consultant_define.php?id=";
    public static final String requests_specialist_not_define_url=url+"consultant_not_define.php?id=";
    public static final String requests_specialist_access_token_url="&access_token=";
    public static final String request_list_of_specialists_main_url=url+"consultant_list_of_specialists.php?subject=";
    public static final String request_list_of_specialists_first_add_url="&type_of_request=";
    public static final String request_list_of_specialists_second_add_url="&id=";
    public static final String request_list_of_specialists_third_add_url="&access_token=";
    public static final String request_call_all_url=url+"consultant_send_request_to_all.php";
    public static final String request_call_selected_url=url+"consultant_send_request_to_selected.php";
    public static final String request_complete_url=url+"consultant_complete_request.php";
    public static final String request_search_specialist_url=url+"consultant_search_specialist.php";
    public static final String place_of_photo_url=url+"images_tohelp/";
    public static final String update_image_url=url+"image_update.php";
    public static final String remove_image_url=url+"image_remove.php";
    public static final String photo_of_specialist_url="https://tohelptohelp.ru/specialist/images_specialist/";
    public static final String request_tests_url=url+"get_tests.php?number=";
    public static final String request_articles_url=url+"get_articles.php?id_of_articles=";
    public static final String request_education_url=url+"get_education.php";
    public static final String request_tech_support_url="https://tohelptohelp.ru/tech_support/request_tech_support.php";
    //----------------------------------------------------------------------------------------------------------------------
    public static final String account_delete_url=url+"account_delete.php";
    public static final String account_requests_reset_url=url+"account_requests_reset.php";
    public static final String account_restore_url=url+"account_restore.php";
    //SplashScreen
    public static final String update_version_of_app=url+"update_version_of_app.php";
    //html-страницы
    public static final String document_phone_url=url_articles_phone+"processing_confidential_data.html";
    public static final String document_tablet_url=url_articles_tablet+"processing_confidential_data.html";
    public static final String about_application_phone_url=url_articles_phone+"about_application.html";
    public static final String about_application_tablet_url=url_articles_tablet+"about_application.html";
    //Шифрование
    public static final String secretKey="QCsHDBQmn+Cet2AEBo4J6g";
    public static final String AES="AES";
    public static final String SHA="SHA-256";
    public static final String UTF="UTF-8";
    public static final int REFRESH_PHOTO_CODE=1;
    public static final int DELETE_PHOTO_CODE=2;
    public static final int MIN_COUNT_OF_CLICK=0;
    public static final int COUNT_OF_CLICK=20;
    public static final int MAX_COUNT_OF_CLICK=100;

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-zA-Z])"+
                    "(?=\\S+$)"+
                    ".{8,}"+
                    "$");

    public static final Pattern PHONE_PATTERN =
            Pattern.compile("\\D" + //(
                    "\\d{3}" + //код
                    "\\D" + //)
                    "\\s" + //пробел
                    "\\d{3}" + //первые три цифры номера
                    "\\s" + //пробел
                    "\\d{2}" + //вторые две цифры номера
                    "\\s" + //пробел
                    "\\d{2}" //третьи две цифры номера
            );

    public static final char[]array_for_password =
            {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '!','@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '?', '{', '}', '[', ']', '.', ','};

    public static final  char[]array_number =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
}
