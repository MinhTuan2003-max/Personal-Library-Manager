# 📚 Personal Library Management System

A full-stack personal library management system that allows users to manage their book collection. The application provides both a RESTful API and a Thymeleaf-based web UI. Users can log in via Google (OAuth2), perform CRUD operations, search and paginate through their books, and upload cover images.

## 🚀 Features

- 🔐 Login via **Google OAuth2**
- 📚 CRUD operations for books:
  - Title
  - Author
  - Genre
  - Description
  - Cover Image
- 🔎 Search books by name or author
- 📄 Pagination for the book list
- 🖼️ Upload book cover images (image stored on disk, filename stored in MongoDB)
- 🌐 RESTful API endpoints
- 👤 Single user role (User only)
- 🧩 Clean Thymeleaf UI

## 🛠 Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, OAuth2
- **Database:** MongoDB
- **Frontend:** Thymeleaf, Bootstrap/CSS
- **Other:** REST API, File Upload

## 📁 Project Structure

```plaintext
src/
└── main/
    ├── java/com/example/library/
    │   ├── controller/     # Web and REST controllers
    │   ├── model/          # Book entity
    │   ├── repository/     # MongoDB repositories
    │   ├── security/       # OAuth2 configuration
    │   └── service/        # Business logic
    └── resources/
        ├── templates/      # Thymeleaf templates
        └── static/         # CSS, JS, Images
```


## 🧪 REST API Endpoints

| Method | Endpoint           | Description             |
|--------|--------------------|-------------------------|
| GET    | `/api/books`       | Get all books           |
| GET    | `/api/books/{id}`  | Get a book by ID        |
| POST   | `/api/books`       | Create a new book       |
| PUT    | `/api/books/{id}`  | Update book information |
| DELETE | `/api/books/{id}`  | Delete a book           |

## 🖼️ Image Upload Handling

- Cover images are stored in the local `uploads/` directory.
- Only the filename is saved in MongoDB for reference.

## 🔐 Authentication

- Users authenticate via Google OAuth2.
- Only logged-in users can access and manage the books.

## 📸 Screenshots

*(Add screenshots here if available)*

## 👤 Author

**Minh Tuấn**  
GitHub: [https://github.com/MinhTuan2003-max](https://github.com/MinhTuan2003-max)

## 📌 License

This project is licensed under the MIT License.

---

> Feel free to fork this repository or use it as a starting point for your own book management system!
