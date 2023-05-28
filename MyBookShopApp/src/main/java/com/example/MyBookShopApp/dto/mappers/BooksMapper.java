package com.example.MyBookShopApp.dto.mappers;

import com.example.MyBookShopApp.dto.author.AuthorDto;
import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.dto.book.BooksWithTotalPrice;
import com.example.MyBookShopApp.dto.book.FullBookDto;
import com.example.MyBookShopApp.dto.book.SlugBookDto;
import com.example.MyBookShopApp.dto.tag.TagDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BooksMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    public ArrayList<Book2UserEntity> createEntities(String status, List<Integer> bookIds, UserEntity user) {
        return mapperUtils.getBook2UserEntities(status, bookIds, user);
    }

    @Mapping(source = "tag.id", target = "id")
    @Mapping(source = "tag.name", target = "name")
    public abstract TagDto createTagDto(TagEntity tag, Integer booksAmount);

//    @Mapping(source = "review.id", target = "id")
//    @Mapping(source = "review.user.name", target = "userName")
//    @Mapping(source = "review.time", target = "time")
//    @Mapping(source = "review.text", target = "text")
//    public abstract ReviewDto createReviewsDto(BookReviewEntity review, Integer likeValue, Integer dislikeValue);

    @Mapping(target = "isBestseller", source = "isBestseller", qualifiedByName = "intToBool")
    @Mapping(target = "title", source = "title", qualifiedByName = "trimTitle")
    @Mapping(target = "authors", expression = "java(mapperUtils.getAuthorNames(bookEntity.getId()))")
    @Mapping(target = "discountPrice", expression =
            "java(mapperUtils.calculateDiscountPrice(bookEntity.getPrice(), bookEntity.getDiscount()))")
    @Mapping(target = "status", expression = "java(mapperUtils.getBookStatus(bookEntity.getId()))")
    @Mapping(target = "rating", expression = "java(mapperUtils.calculateBooksRating(bookEntity.getId()))")
    public abstract BookDto createBookDto(BookEntity bookEntity);

    @Mapping(target = "isBestseller", source = "isBestseller", qualifiedByName = "intToBool")
    @Mapping(target = "title", source = "title", qualifiedByName = "trimTitle")
    @Mapping(target = "discountPrice", expression =
            "java(mapperUtils.calculateDiscountPrice(bookEntity.getPrice(), bookEntity.getDiscount()))")
    @Mapping(target = "status", expression = "java(mapperUtils.getBookStatus(bookEntity.getId()))")
    @Mapping(target = "rating", expression = "java(mapperUtils.calculateBooksRating(bookEntity.getId()))")
    @Mapping(target = "authors", expression = "java(mapperUtils.getAuthors(bookEntity.getId()))")
    @Mapping(target = "ratingsByStar", expression = "java(mapperUtils.getRatingsByStar(bookEntity.getId()))")
    @Mapping(target = "ratingsCount", expression = "java(mapperUtils.countAllRatingsByBookId(bookEntity.getId()))")
    @Mapping(target = "tags", expression = "java(mapperUtils.getTags(bookEntity.getId()))")
    @Mapping(target = "reviews", expression = "java(mapperUtils.getReviews(bookEntity.getId()))")
    public abstract SlugBookDto createSlugBookDto(BookEntity bookEntity);

    @Named("intToBool")
    public Boolean intToBoolean(Integer value) {
        return value != 0;
    }

    @Named("trimTitle")
    public String trimTitle(String title) {
        return title.length() > 25 ? title.substring(0, 25).concat("...") : title;
    }


    @Mapping(target = "isBestseller", source = "isBestseller", qualifiedByName = "intToBool")
    public abstract FullBookDto toFullBookDto(BookEntity bookEntity);

    public abstract List<AuthorDto> toAuthorDtos(List<AuthorEntity> authorEntities);

    public abstract List<TagDto> toTagDtos(List<TagEntity> tagEntities);

    @Mapping(target = "isBestseller", source = "isBestseller", qualifiedByName = "intToBool")
    public abstract FullBookDto createFullBookDto(BookEntity bookEntity);


    // TODO: 28.04.2023 change dto
    public BooksWithTotalPrice toBooksWithTotalPrice(List<BookEntity> bookEntitiesByIdIn) {
        List<SlugBookDto> bookDtos = new ArrayList<>();
        for (BookEntity bookEntity : bookEntitiesByIdIn) {
            SlugBookDto bookDto = createSlugBookDto(bookEntity);
            bookDtos.add(bookDto);
        }
        return new BooksWithTotalPrice(bookDtos);
    }
}
