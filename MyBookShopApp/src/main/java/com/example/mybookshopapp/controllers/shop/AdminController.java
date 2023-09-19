package com.example.mybookshopapp.controllers.shop;

import com.example.mybookshopapp.annotation.RequestParamsTrackable;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.admin.AdminChangeBookStatusDto;
import com.example.mybookshopapp.dto.admin.DeleteItemDto;
import com.example.mybookshopapp.dto.admin.UploadAuthorDto;
import com.example.mybookshopapp.dto.admin.UploadBookDto;
import com.example.mybookshopapp.dto.admin.UploadGenreDto;
import com.example.mybookshopapp.dto.admin.UploadTagDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.user.UserDtoPage;
import com.example.mybookshopapp.services.author.AuthorService;
import com.example.mybookshopapp.services.book.BookService;
import com.example.mybookshopapp.services.bookstatus.BookStatusService;
import com.example.mybookshopapp.services.genre.GenreService;
import com.example.mybookshopapp.services.auth.UserRegService;
import com.example.mybookshopapp.services.feedback.FeedbackService;
import com.example.mybookshopapp.services.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final TagService tagService;
    private final BookService bookService;
    private final FeedbackService reviewService;
    private final UserRegService userService;
    private final BookStatusService statusService;

    @GetMapping("/add/book")
    public String getAddNewBookView(Model model) {
        model.addAttribute("authorsList", authorService.getSortedAuthorList());
        model.addAttribute("genresList", genreService.getSortedGenreList());
        model.addAttribute("tagsList", tagService.getSortedTagsList());
        return "/admin/book";
    }

    @PostMapping("/add/book")
    @RequestParamsTrackable
    @ResponseBody
    public ResultDto addNewBook(UploadBookDto uploadBookDto) throws IOException {
        return bookService.saveOrUpdateBook(uploadBookDto);
    }

    @GetMapping("/add/author")
    public String getAddNewAuthorView() {
        return "/admin/author";
    }

    @PostMapping("/add/author")
    @ResponseBody
    @RequestParamsTrackable
    public ResultDto addNewAuthor(UploadAuthorDto uploadAuthorDto) throws IOException {
        return authorService.saveOrUpdateAuthor(uploadAuthorDto);
    }

    @GetMapping("/add/genre")
    public String showAddNewGenreView(Model model) {
        model.addAttribute("genres", genreService.getSortedGenreList());
        return "/admin/genre";
    }

    @PostMapping("/add/genre")
    @ResponseBody
    @RequestParamsTrackable
    public ResultDto addNewGenre(UploadGenreDto uploadGenreDto) {
        return genreService.saveOrUpdateGenre(uploadGenreDto);
    }

    @GetMapping("/add/tag")
    public String showAddNewTagView() {
        return "/admin/tag";
    }

    @PostMapping("/add/tag")
    @ResponseBody
    @RequestParamsTrackable
    public ResultDto addNewTag(UploadTagDto uploadTagDto) {
        return tagService.saveOrUpdateTag(uploadTagDto);
    }

    @DeleteMapping("/delete/item")
    @ResponseBody
    public ResultDto deleteItem(DeleteItemDto deleteItemDto) {
        String itemId = deleteItemDto.getItemId();
        return switch (deleteItemDto.getItem()) {
            case BOOK -> bookService.deleteBook(itemId);
            case AUTHOR -> authorService.deleteAuthor(itemId);
            case GENRE-> genreService.deleteGenre(itemId);
            case TAG -> tagService.deleteTag(itemId);
            case REVIEW -> reviewService.deleteReview(itemId);
            case USER -> userService.deleteUser(itemId);
        };
    }
    @GetMapping("/users")
    public String users(){
        return "/admin/users";
    }

    @GetMapping("/users/shelf")
    public String usersShelf(@RequestParam("userid") String userId, Model model){
        model.addAttribute("userId", userId);
        model.addAttribute("booksPage", statusService.getBoughtBooks(userId));
        return "/admin/shelf";
    }
    @PostMapping("/admin/changeBookStatus")
    @ResponseBody
    public ResultDto changeBookStatus(@RequestBody AdminChangeBookStatusDto changeBookStatusDto){
        return statusService.changeBookStatusAdmin(changeBookStatusDto);
    }

    @GetMapping("/api/users")
    @ResponseBody
    public UserDtoPage getUsers(RequestDto request){
        return userService.getUserPage(request);
    }

}
