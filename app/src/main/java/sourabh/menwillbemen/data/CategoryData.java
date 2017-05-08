package sourabh.menwillbemen.data;


import java.io.Serializable;

public class CategoryData implements Serializable{

    Integer id_category;
    String category_name;
    String created_on;
    String category_icon;

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public Integer getId_category() {
        return id_category;
    }

    public void setId_category(Integer id_category) {
        this.id_category = id_category;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
