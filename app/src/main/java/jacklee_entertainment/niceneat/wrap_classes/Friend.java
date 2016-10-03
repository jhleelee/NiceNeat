package jacklee_entertainment.niceneat.wrap_classes;

import android.graphics.Bitmap;

public class Friend {
    private String name;
    private String num;
    private String id;
    private Bitmap profilepic;

    // Required default constructor for Firebase serialization / deserialization
    public Friend() {
    }

    public Friend(String name, String id, String num, Bitmap profilepic) {
        this.name = name;
        this.num = num;
        this.id = id;
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }
    public String getNum() {
        return num;}

    public String getId() {
        return id;
    }
    public Bitmap getProfilepic() {
        return profilepic;
    }
}
