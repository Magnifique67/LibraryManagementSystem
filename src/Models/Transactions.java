package Models;

import java.sql.Timestamp;
import java.sql.Date;

public class Transactions {
    private int id;
    private int book_id;
    private int patron_id;
    private String book_title;
    private String patron_firstName;
    private String patron_lastName;
    private Timestamp transaction_date;
    private Date due_date;
    private boolean returned;

    public Transactions(int id, int book_id, int patron_id, String book_title, String patron_firstName, String patron_lastName, Timestamp transaction_date, Date due_date, boolean returned) {
        this.id = id;
        this.book_id = book_id;
        this.patron_id = patron_id;
        this.book_title = book_title;
        this.patron_firstName = patron_firstName;
        this.patron_lastName = patron_lastName;
        this.transaction_date = transaction_date;
        this.due_date = due_date;
        this.returned = returned;
    }

    // Getters for each field
    public int getId() {
        return id;
    }

    public int getBook_id() {
        return book_id;
    }

    public int getPatron_id() {
        return patron_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getPatron_firstName() {
        return patron_firstName;
    }

    public String getPatron_lastName() {
        return patron_lastName;
    }

    public Timestamp getTransaction_date() {
        return transaction_date;
    }

    public Date getDue_date() {
        return due_date;
    }

    public boolean isReturned() {
        return returned;
    }

    // Setters for each field
    public void setId(int id) {
        this.id = id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setPatron_id(int patron_id) {
        this.patron_id = patron_id;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public void setPatron_firstName(String patron_firstName) {
        this.patron_firstName = patron_firstName;
    }

    public void setPatron_lastName(String patron_lastName) {
        this.patron_lastName = patron_lastName;
    }

    public void setTransaction_date(Timestamp transaction_date) {
        this.transaction_date = transaction_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
