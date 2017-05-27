package sourabh.menwillbemen.data;

import java.io.Serializable;

/**
 * Created by Sourabh on 5/19/2017.
 */

public class TranslationData implements Serializable {

    String multilang_msg_title;
    String translation;
    Integer id_language;

    public String getMultilang_msg_title() {
        return multilang_msg_title;
    }

    public void setMultilang_msg_title(String multilang_msg_title) {
        this.multilang_msg_title = multilang_msg_title;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Integer getId_language() {
        return id_language;
    }

    public void setId_language(Integer id_language) {
        this.id_language = id_language;
    }
}
