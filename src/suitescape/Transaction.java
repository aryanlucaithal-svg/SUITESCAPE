
package suitescape;


public class Transaction {
    String txnId, bookingId, guest, method, status;
    double total;

    Transaction(String txnId, String bookingId, String guest, String method, double total) {
        this.txnId = txnId;
        this.bookingId = bookingId;
        this.guest = guest;
        this.method = method;
        this.total = total;
        this.status = "Paid";
    }
    
    boolean isPaid(){
        if(status.equals("Paid")){
            return true;
        }
        return false;
    }
    
    boolean isVoided(){
        if(status.equals("Voided")){
            return true;
        }
        return false;
    }
    
    boolean belongsTo(String otherBookingId) {
        return bookingId != null && bookingId.equals(otherBookingId);
}
    
    int compareTxnTo(String otherTxnId){
        if(txnId == null){
            return -1;
        }
        return txnId.compareTo(otherTxnId);
    }
    
    void voidTransaction(){
        this.status = "Voided";
    }
}
