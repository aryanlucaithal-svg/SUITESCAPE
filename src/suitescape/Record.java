package suitescape;

public class Record {
    boolean isEvent;
    String date, id, name, mobile, packageName, payment, label;
    int guest;

    public Record(String id, String name, String mobile, String date,
           int guests, String packageName, String payment) {
        this.isEvent = false;
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.date = date;
        this.guest = guests;
        this.packageName = packageName;
        this.payment = payment;
    }
    public Record(String date, String label) {
        this.isEvent = true;
        this.date = date;
        this.label = label;
    }
  
    boolean isOnDate(String d) {
    return date != null && date.equals(d);
    }
    
    boolean hasId(String otherId) {
        return id != null && id.equals(otherId);
    }
}