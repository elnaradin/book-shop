package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.ShortBookDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.google.api.books.Item;
import com.example.mybookshopapp.dto.google.api.books.Root;
import com.example.mybookshopapp.dto.google.api.books.VolumeInfo;
import com.example.mybookshopapp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    @Value("${google.books.api.key}")
    private String key;

    @Override
    public BooksPageDto getPageOfSearchResultBooks(RequestDto request) {
        Pageable nextPage = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDtoProjection> booksPage = bookRepository
                .findBooksByTitleContaining(request.getSearchWord(), nextPage);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }

    @Override
    public BooksPageDto getPageOfGoogleBooksApiSearchResult(RequestDto request) {
        String requestUrl = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + request.getSearchWord() + "" +
                "&filter=paid-ebooks" +
                "&startIndex=" + request.getOffset() +
                "&maxResults=" + request.getLimit() +
                "&key=" + key;
        Root root = restTemplate.getForEntity(requestUrl, Root.class).getBody();
        List<ShortBookDtoProjection> books = new ArrayList<>();
        BooksPageDto booksPageDto;
        if (root == null || root.getItems() == null) {
            return null;
        }
        for (Item item : root.getItems()) {
            ShortBookDto book = new ShortBookDto();
            if (item.getVolumeInfo() != null) {
                ArrayList<String> authors = Optional.of(item.getVolumeInfo())
                        .map(VolumeInfo::getAuthors).orElse(null);

                book.setAuthors(Optional.ofNullable(authors)
                        .map(a -> a.size() > 1 ? a.get(0).concat(getMessageForLocale("authors.andOthers")) : a.get(0))
                        .orElse(""));

                book.setTitle(item.getVolumeInfo().getTitle());
                book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
            }
            if (item.getSaleInfo() != null && item.getSaleInfo().getRetailPrice() != null) {
                book.setDiscountPrice(item.getSaleInfo().getRetailPrice().getAmount());
                Integer oldPrice = item.getSaleInfo().getListPrice().getAmount();
                book.setPrice(oldPrice);
            } else {
                book.setDiscountPrice(0);
                book.setPrice(0);
            }
            book.setDiscount(0);
            books.add(book);

        }
        booksPageDto = BooksPageDto.builder()
                .books(books)
                .count(root.getTotalItems())
                .hasNext(books.size() == request.getLimit())
                .build();

        return booksPageDto;
    }
}
