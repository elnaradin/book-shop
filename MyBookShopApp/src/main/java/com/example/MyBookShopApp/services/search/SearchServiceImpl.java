package com.example.MyBookShopApp.services.search;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.dto.book.ShortBookDtoProjection;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.google.api.books.Item;
import com.example.MyBookShopApp.dto.google.api.books.Root;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + request.getSearchWord() + "" +
                "&filter=paid-ebooks" +
                "&startIndex=" + request.getOffset() +
                "&maxResults=" + request.getLimit() +
                "&key=" + key;
        Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
        List<ShortBookDtoProjection> books = new ArrayList<>();
        BooksPageDto booksPageDto = null;
        if (root != null && root.getItems() != null) {
            for (Item item : root.getItems()) {
                ShortBookDto book = new ShortBookDto();
                if (item.getVolumeInfo() != null) {
                    ArrayList<String> authors = item.getVolumeInfo().getAuthors();
                    if (authors != null) {
                        book.setAuthors(authors.size() > 1 ? authors.get(0).concat(" и прочие") : authors.get(0));
                    } else {
                        book.setAuthors("");
                    }
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
        }
        return booksPageDto;
    }
}
