package sourabh.menwillbemen.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.data.PostItemData;

public class PostsData implements Serializable {

    private List<PostItemData> latest = null;
    private List<PostItemData> top = null;
    private List<CategoryData> categories = null;
    private List<SettingData> settings = null;
    private List<SettingData> card_colors = null;

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








}