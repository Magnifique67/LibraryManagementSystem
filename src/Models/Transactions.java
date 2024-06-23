package Models;
import java.sql.Timestamp;
import java.util.*;
public class Transactions {
    private int id;
    private int book_id;
    private int patron_id;
    private Date transaction_date;
    private Date due_date;

    public Transactions(int id, int book_id, int patron_id, Date transaction_date, Date due_date) {
        this.id = id;
        this.book_id = book_id;
        this.patron_id = patron_id;
        this.transaction_date = transaction_date;
        this.due_date = due_date;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBook_id() {
        return book_id;
    }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
    public int getPatron_id() {
        return patron_id;
    }
    public void setPatron_id(int patron_id) {
        this.patron_id = patron_id;
    }
    public Timestamp getTransaction_date() {
        return (Timestamp) transaction_date;
    }
    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }
    public java.sql.Date getDue_date() {
        return (java.sql.Date) due_date;
    }
    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }
}
