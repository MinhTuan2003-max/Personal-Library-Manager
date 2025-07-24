package com.luv2code.springboot.demo.plm.service.impl;

import com.luv2code.springboot.demo.plm.exception.BookNotFoundException;
import com.luv2code.springboot.demo.plm.exception.InsufficientFundsException;
import com.luv2code.springboot.demo.plm.model.dto.request.BookCreateRequest;
import com.luv2code.springboot.demo.plm.model.dto.request.BookUpdateRequest;
import com.luv2code.springboot.demo.plm.model.dto.response.BookResponse;
import com.luv2code.springboot.demo.plm.model.entity.Book;
import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.model.enums.Genre;
import com.luv2code.springboot.demo.plm.repository.BookRepository;
import com.luv2code.springboot.demo.plm.repository.UserRepository;
import com.luv2code.springboot.demo.plm.service.BookService;
import com.luv2code.springboot.demo.plm.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(String userId, Pageable pageable) {
        Page<Book> books = bookRepository.findByUserId(userId, pageable);
        return books.map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> searchBooks(String userId, String keyword, Pageable pageable) {
        Page<Book> books = bookRepository.findByUserIdAndTitleOrAuthorContainingIgnoreCase(
                userId, keyword, pageable);
        return books.map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooksByGenre(String userId, Genre genre, Pageable pageable) {
        Page<Book> books = bookRepository.findByUserIdAndGenre(userId, genre, pageable);
        return books.map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(String id, String userId) {
        Book book = bookRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BookNotFoundException("Không tìm thấy sách với ID: " + id));
        return convertToResponse(book);
    }

    @Override
    public BookResponse createBook(BookCreateRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Kiểm tra ngân sách
        if (user.getBudget().compareTo(request.getPrice()) < 0) {
            throw new InsufficientFundsException("Không đủ tiền để mua sách này!");
        }

        // Upload ảnh bìa nếu có
        String coverImagePath = null;
        if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
            coverImagePath = fileUploadService.uploadFile(request.getCoverImage(), "covers");
        }

        // Tạo sách mới
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .description(request.getDescription())
                .price(request.getPrice())
                .coverImagePath(coverImagePath)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        // Trừ tiền và cập nhật thông tin user
        user.setBudget(user.getBudget().subtract(request.getPrice()));
        user.setTotalSpent(user.getTotalSpent().add(request.getPrice()));
        userRepository.save(user);

        // Lưu sách
        Book savedBook = bookRepository.save(book);
        return convertToResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(String id, BookUpdateRequest request, String userId) {
        Book existingBook = bookRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BookNotFoundException("Không tìm thấy sách với ID: " + id));

        // Upload ảnh mới nếu có
        if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
            // Xóa ảnh cũ nếu có
            if (existingBook.getCoverImagePath() != null) {
                fileUploadService.deleteFile(existingBook.getCoverImagePath(), "covers");
            }
            String newCoverImagePath = fileUploadService.uploadFile(request.getCoverImage(), "covers");
            existingBook.setCoverImagePath(newCoverImagePath);
        }

        // Cập nhật thông tin
        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setGenre(request.getGenre());
        existingBook.setDescription(request.getDescription());
        existingBook.setUpdatedAt(LocalDateTime.now());

        Book updatedBook = bookRepository.save(existingBook);
        return convertToResponse(updatedBook);
    }

    @Override
    public boolean deleteBook(String id, String userId) {
        Book book = bookRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BookNotFoundException("Không tìm thấy sách với ID: " + id));

        // Xóa ảnh bìa nếu có
        if (book.getCoverImagePath() != null) {
            fileUploadService.deleteFile(book.getCoverImagePath(), "covers");
        }

        bookRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalBooksCount(String userId) {
        return bookRepository.countByUserId(userId);
    }

    private BookResponse convertToResponse(Book book) {
        BookResponse response = modelMapper.map(book, BookResponse.class);
        if (book.getCoverImagePath() != null) {
            response.setCoverImageUrl("/uploads/covers/" + book.getCoverImagePath());
        }
        return response;
    }
}
