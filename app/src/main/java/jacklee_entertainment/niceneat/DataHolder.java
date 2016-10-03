//package jacklee_entertainment.niceneat;
//
//
//import com.quickblox.users.model.QBUser;
//
//
//
//public class DataHolder {
//
//    private static DataHolder dataHolder;
//
//    private QBUser signInQbUser;
//    private boolean signInUsingSocial;
//
//    public static synchronized DataHolder getDataHolder() {
//        if (dataHolder == null) {
//            dataHolder = new DataHolder();
//        }
//        return dataHolder;
//    }
//
//
//
//    public QBUser getSignInQbUser() {
//        return signInQbUser;
//    }
//
//
//
//    public void setSignInQbUser(QBUser singInQbUser) {
//        signInQbUser = singInQbUser;
//    }
//
//    public boolean checkSignInUsingSocial() {
//        return signInUsingSocial;
//    }
//
//    public void setSignInUsingSocial(boolean s) {
//        this.signInUsingSocial = s;
//    }
//}
