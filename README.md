# **Library Management System Requirements Document**

## **Overview**

The **Library Management System (LMS)** is designed to streamline the operations of a library, enabling efficient management of books, patrons, and librarian activities. The system will support book borrowing and returning, patron due date updates, book searches, and librarian management of books and patron transactions. The application will be developed using **Java** for the backend and **JavaFX** for the user interface.

## **Functional Requirements**

### **For Librarians**

1. **Manage Books**
    - **Add New Books**: Librarians can add new book records to the library’s catalog.
    - **Update Book Information**: Librarians can update details of existing books.
    - **Delete Books**: Librarians can remove books from the library’s catalog.

2. **Manage Patron Transactions**
    - **Track Borrowed Books**: Keep a record of all borrowed books, including borrow and due dates.
    - **Track Returned Books**: Keep a record of returned books, including the return date.
    - **Oversee Due Date Extensions**: Approve or reject due date extension requests from patrons.

### **For Patrons**

1. **Borrow Books**
    - Patrons can borrow available books from the library.
    - The system should record the borrow date and automatically calculate the initial due date.

2. **Return Books**
    - Patrons can return borrowed books.
    - The system should update the return date and change the book status to available.

3. **Update Due Date**
    - Patrons can request an extension of the due date for borrowed books.
    - The system should allow for one or more due date extensions, subject to librarian approval.

4. **Search Books**
    - Patrons can search for books by various attributes such as title, author, genre, or publication year.

## **Non-Functional Requirements**

1. **Usability**
    - The system should have an intuitive and user-friendly interface developed using JavaFX.
    - The system should provide clear feedback for user actions (e.g., confirmation messages for successful transactions, error messages for invalid actions).

2. **Performance**
    - The system should handle multiple simultaneous transactions efficiently.
    - Book searches should return results within a reasonable time frame.

3. **Security**
    - Only authorized librarians should be able to manage books and patron transactions.
    - Patron data should be protected, and only authorized access should be allowed.
    - All transactions should be securely recorded.

4. **Reliability**
    - The system should ensure data integrity and consistency.
    - The system should have backup and recovery mechanisms to prevent data loss.

5. **Scalability**
    - The system should be able to handle an increasing number of books and patrons without significant performance degradation.


## **User Interface Requirements**

1. **Librarian Dashboard**
    - Access to manage books (add, update, delete).
    - Access to view and manage patron transactions.
    - Notifications for pending due date extension requests.

2. **Patron Dashboard**
    - Access to borrow and return books.
    - Option to request due date extensions.
    - Search functionality to find books.
    - Overview of current borrowings and their due dates.

### **JavaFX UI Elements**
- **Tables**: For displaying lists of books and transactions.
- **Forms**: For adding and updating book and patron information.
- **Buttons**: For actions such as borrow, return, add, update, and delete.
- **Search Bar**: For searching books.
- **Dialogs**: For confirmation and error messages.

## **System Requirements**

1. **Hardware Requirements**
    - Client devices (computers) for librarians and patrons to access the system.

2. **Software Requirements**
    - Java Development Kit (JDK) for backend development.
    - JavaFX for the user interface.
    - Database management system (PostgreSQL).
    - Integrated Development Environment (IDE)  IntelliJ IDEA.

## **Development Environment Setup**

1. **Install JDK**: Ensure the latest JDK is installed.
2. **Set Up JavaFX**: Include JavaFX libraries in your project.
3. **Database Configuration**: Set up the database and create necessary tables.
4. **IDE Configuration**: Configure your IDE to support Java and JavaFX development.

## **Glossary**

- **Librarian**: An authorized user who can manage books and oversee patron transactions.
- **Patron**: A user who can borrow and return books, update due dates, and search for books.
- **Book Status**: The current state of a book, such as available or borrowed.

