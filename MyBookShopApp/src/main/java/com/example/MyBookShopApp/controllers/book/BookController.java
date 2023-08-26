package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.author.AuthorService;
import com.example.MyBookShopApp.services.book.BookService;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import com.example.MyBookShopApp.services.file.FileService;
import com.example.MyBookShopApp.services.ratingAndReview.RatingReviewService;
import com.example.MyBookShopApp.services.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final AuthorService authorService;
    private final ResourceStorage storage;
    private final FileService fileService;
    private final RatingReviewService ratingService;
    private final TagService tagService;
    private final BookStatusService statusService;

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model, Authentication authentication) {
        model.addAttribute("book", bookService.getFullBookInfoBySlug(slug));
        model.addAttribute("authors", authorService.getAuthorsList(slug));
        model.addAttribute("ratings", ratingService.getBookRating(slug));
        model.addAttribute("tags", tagService.getShortTagsList(slug));
        model.addAttribute("bookFiles", fileService.getFilesBySlug(slug));
        model.addAttribute("reviews", ratingService.getBookReviews(slug));
        if (authentication != null) {
            model.addAttribute("myRating", ratingService.getBookRatingOfCurrentUser(slug));
            model.addAttribute("status", statusService.getBookStatus(slug));
            statusService.changeBookStatus(new ChangeStatusPayload(List.of(slug), StatusType.RECENTLY_VIEWED));
        }
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(
            @PathVariable String slug,
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        String savePath = storage.saveNewBookImage(slug, file);
        BookEntity bookToUpdate = bookRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("book not found"));
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);
        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime: " + path);
        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data length: " + data.length);
        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + path.getFileName().toString()
                )
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
