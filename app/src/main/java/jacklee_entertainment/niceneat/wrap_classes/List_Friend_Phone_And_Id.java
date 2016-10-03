package jacklee_entertainment.niceneat.wrap_classes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author greg
 * @since 6/26/13
 */
public class List_Friend_Phone_And_Id {

    private List<Friend_Phone_And_Id> list = new ArrayList<Friend_Phone_And_Id>();

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    public List_Friend_Phone_And_Id() {
    }

    public void addFriend_Phone_And_Id(String num, String id) {
        Friend_Phone_And_Id p = new Friend_Phone_And_Id(num, id);
        list.add(p);
    }

    public List<Friend_Phone_And_Id> getList_Friend_Phone_And_Id() {
        return list;
    }

 }
