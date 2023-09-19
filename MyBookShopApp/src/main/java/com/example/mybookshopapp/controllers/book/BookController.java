package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.FileService;
import com.example.mybookshopapp.services.GenreService;
import com.example.mybookshopapp.services.FeedbackService;
import com.example.mybookshopapp.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final FileService fileService;
    private final FeedbackService ratingService;
    private final TagService tagService;
    private final BookStatusService statusService;
    private final IAuthenticationFacade facade;
    private final GenreService genreService;

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model)  {
        model.addAttribute("authors", authorService.getAuthorsListBySlug(slug));
        model.addAttribute("tags", tagService.getShortTagsList(slug));
        model.addAttribute("reviews", ratingService.getBookReviews(slug));
        if(facade.isAdmin()){
            model.addAttribute("bookFiles", fileService.getFilesBySlugAdmin(slug));
            model.addAttribute("book", bookService.getBookUpdateData(slug));
            model.addAttribute("genres", genreService.getShortGenresListBySlug(slug));
            model.addAttribute("authorsList", authorService.getSortedAuthorList());
            model.addAttribute("genresList", genreService.getSortedGenreList());
            model.addAttribute("tagsList", tagService.getSortedTagsList());
            return "/books/slug";
        }
        model.addAttribute("book", bookService.getFullBookInfoBySlug(slug));
        model.addAttribute("ratings", ratingService.getBookRating(slug));
        if (facade.isAuthenticated()) {
            model.addAttribute("downloadLimitReached", fileService.isDownloadLimitReached(slug));
            model.addAttribute("bookFiles", fileService.getFilesBySlug(slug));
            model.addAttribute("myRating", ratingService.getBookRatingOfCurrentUser(slug));
            model.addAttribute("status", statusService.getBookStatus(slug));
        }
        return "/books/slug";
    }


    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@PathVariable String slug, @RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadAndSetBookCover(slug, file);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{slug}/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable String slug, @PathVariable String hash) throws IOException {
       return fileService.downloadBook(slug, hash);
    }
}
