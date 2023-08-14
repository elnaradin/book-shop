package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.dto.request.PubDatesDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BooksPageDto extends PubDatesDto {
    private boolean hasNext;
    private Long count;
    private List<ShortBookDto> books;
    private Integer totalPrice;
    private Integer totalDiscountPrice;
    private List<String> slugs;
    private Map<String, List<String>> slugsByStatus;

}
