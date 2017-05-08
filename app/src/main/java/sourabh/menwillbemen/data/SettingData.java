package sourabh.menwillbemen.data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Downloader on 5/3/2017.
 */

public class SettingData implements Serializable {

    String name;
    String value;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
