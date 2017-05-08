package sourabh.menwillbemen.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Downloader on 5/6/2017.
 */

public class LanguageData implements Serializable{

    Integer id_language;
    String language_title;

    List<LanguageData> languages;

    public List<LanguageData> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageData> languages) {
        this.languages = languages;
    }

    public Integer getId_language() {
        return id_language;
    }

    public void setId_language(Integer id_language) {
        this.id_language = id_language;
    }

    public String getLanguage_title() {
        return language_title;
    }

    public void setLanguage_title(String language_title) {
        this.language_title = language_title;
    }

}
