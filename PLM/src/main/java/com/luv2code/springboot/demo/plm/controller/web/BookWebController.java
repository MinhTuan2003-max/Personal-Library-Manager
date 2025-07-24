package com.luv2code.springboot.demo.plm.controller.web;

import com.luv2code.springboot.demo.plm.model.dto.request.BookCreateRequest;
import com.luv2code.springboot.demo.plm.model.dto.request.BudgetAddRequest;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BookWebController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model,
                       OAuth2AuthenticationToken token,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
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

        model.addAttribute("books", books);
        model.addAttribute("user", user);
        model.addAttribute("search", search);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("totalBooks", bookService.getTotalBooksCount(user.getId()));

        return "index";
    }

    @GetMapping("/books/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookCreateRequest());
        model.addAttribute("genres", Genre.values());
        return "book/form";
    }

    @PostMapping("/books/add")
    public String addBook(@Valid @ModelAttribute("book") BookCreateRequest request,
                          BindingResult result,
                          OAuth2AuthenticationToken token,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "book/form";
        }

        try {
            User user = userService.findOrCreateUser(token);
            bookService.createBook(request, user.getId());
            redirectAttributes.addFlashAttribute("success", "Thêm sách thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable String id,
                             OAuth2AuthenticationToken token,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findOrCreateUser(token);
            bookService.deleteBook(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Xóa sách thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/budget/add")
    public String addBudget(@Valid @ModelAttribute BudgetAddRequest request,
                            BindingResult result,
                            OAuth2AuthenticationToken token,
                            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Số tiền không hợp lệ!");
            return "redirect:/";
        }

        try {
            User user = userService.findOrCreateUser(token);
            userService.addBudget(user.getId(), request.getAmount());
            redirectAttributes.addFlashAttribute("success",
                    "Đã thêm " + String.format("%,.0f", request.getAmount()) + " VND vào ngân sách!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }
}
