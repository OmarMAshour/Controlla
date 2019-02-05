package Data;

import static com.controlla.controlla.MainActivity.firebaseManager;

public class OBD_Variable {

    private String key;
    private String name;

    public OBD_Variable (String key, String name){
        this.key = key;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
