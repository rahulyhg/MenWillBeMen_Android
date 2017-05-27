package sourabh.menwillbemen.data;

import java.io.Serializable;
import java.util.List;

public class PostsData implements Serializable {

    private List<PostItemData> latest = null;
    private List<PostItemData> top = null;
    private List<CategoryData> categories = null;
    private List<SettingData> settings = null;
    private List<SettingData> card_colors = null;
    private List<LanguageData> languages = null;
    private List<TranslationData> translations = null;

    public List<TranslationData> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationData> translations) {
        this.translations = translations;
    }

    public List<SettingData> getCard_colors() {
        return card_colors;
    }

    public void setCard_colors(List<SettingData> card_colors) {
        this.card_colors = card_colors;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryData> categories) {
        this.categories = categories;
    }

    public List<PostItemData> getLatest() {
        return latest;
    }

    public void setLatest(List<PostItemData> latest) {
        this.latest = latest;
    }

    public List<PostItemData> getTop() {
        return top;
    }

    public void setTop(List<PostItemData> top) {
        this.top = top;
    }

    public List<SettingData> getSettings() {
        return settings;
    }

    public void setSettings(List<SettingData> settings) {
        this.settings = settings;
    }


    public List<LanguageData> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageData> languages) {
        this.languages = languages;
    }
}