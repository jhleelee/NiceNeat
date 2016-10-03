package jacklee_entertainment.niceneat.utils;

import java.util.Date;

public class ContactListEntry {
    public enum EntryType {
        FRIEND_ENTRY, CONTACT_ENTRY
    }

    private ContactListEntry.EntryType entryType;
    private String displayName;
    private Date lastRequestAt;
    private String phone;
    private String nationalCode;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalCode() {
        return this.nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public ContactListEntry.EntryType getEntryType() {
        return this.entryType;
    }

    public void setEntryType(ContactListEntry.EntryType entryType) {
        this.entryType = entryType;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getLastRequestAt() {
        return this.lastRequestAt;
    }

    public void setLastRequestAt(Date lastRequestAt) {
        this.lastRequestAt = lastRequestAt;
    }
}
