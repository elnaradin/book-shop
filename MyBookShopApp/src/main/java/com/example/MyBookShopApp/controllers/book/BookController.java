package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.BooksRatingAndPopularityService;
import com.example.MyBookShopApp.services.FileService;
import com.example.MyBookShopApp.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ResourceStorage storage;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final FileService fileService;
    private final ReviewService reviewService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        BookDto book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("tags", bookService.getTagsByBookSlug(slug));
        model.addAttribute("bookFiles", fileService.getFilesBySlug(slug));
        model.addAttribute("bookRatingAmount", booksRatingAndPopularityService.getBookRatingAmount(slug));
        model.addAttribute("ratingAmountByStars", booksRatingAndPopularityService.getRatingAmountByStars(slug));
        model.addAttribute("reviews", reviewService.getBookReviews(slug));
        return "/books/slugmy";
    }


    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@PathVariable String slug, @RequestParam("file") MultipartFile file) throws IOException {
        String savePath = storage.saveNewBookImage(slug, file);
        BookEntity bookToUpdate = bookRepository.findBookEntityBySlug(slug);
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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
