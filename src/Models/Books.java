package Models;
import java.sql.Date;

public class Books {
    private int id;
    private String title;
    private Date published_date;
    private String isbn;
    private Boolean availability;
    public Books(int id, String title, Date published_date, String isbn, Boolean availability) {
        this.id = id;
        this.title = title;
        this.published_date = published_date;
        this.isbn = isbn;
        this.availability = availability;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublished_date() {
        return published_date;
    }
    public void setPublished_date(Date published_date) {
        this.published_date = published_date;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public Boolean getAvailability() {
        return availability;
    }
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}
