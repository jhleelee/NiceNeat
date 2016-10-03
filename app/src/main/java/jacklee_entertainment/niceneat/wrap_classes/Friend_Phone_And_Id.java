package jacklee_entertainment.niceneat.wrap_classes;

/**
 * @author greg
 * @since 6/26/13
 */
public class Friend_Phone_And_Id {
    private String num;
    private String id;

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    public Friend_Phone_And_Id() {
    }

    public Friend_Phone_And_Id(String num, String id) {
        this.num = num;
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public String getId() {
        return id;
    }
}
