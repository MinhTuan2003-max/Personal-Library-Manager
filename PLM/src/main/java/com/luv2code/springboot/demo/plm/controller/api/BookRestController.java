package com.luv2code.springboot.demo.plm.controller.api;

import com.luv2code.springboot.demo.plm.model.dto.request.BookCreateRequest;
import com.luv2code.springboot.demo.plm.model.dto.response.ApiResponse;
import com.luv2code.springboot.demo.plm.model.dto.response.BookResponse;
import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.model.enums.Genre;
import com.luv2code.springboot.demo.plm.service.BookService;
import com.luv2code.springboot.demo.plm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getAllBooks(
            OAuth2AuthenticationToken token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Genre genre) {

        User user = userService.findOrCreateUser(token);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<BookResponse> books;
        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(user.getId(), search, pageable);
        } else if (genre != null) {
            books = bookService.getBooksByGenre(user.getId(), genre, pageable);
        } else {
            books = bookService.getAllBooks(user.getId(), pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(
            @PathVariable String id,
            OAuth2AuthenticationToken token) {

        User user = userService.findOrCreateUser(token);
        BookResponse book = bookService.getBookById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @ModelAttribute BookCreateRequest request,
            OAuth2AuthenticationToken token) {

        User user = userService.findOrCreateUser(token);
        BookResponse book = bookService.createBook(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(
            @PathVariable String id,
            OAuth2AuthenticationToken token) {

        User user = userService.findOrCreateUser(token);
        bookService.deleteBook(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Xóa sách thành công"));
    }
}
