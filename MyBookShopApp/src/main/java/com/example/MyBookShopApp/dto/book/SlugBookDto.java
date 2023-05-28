package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.dto.author.SmallAuthorsDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.tag.SmallTagsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlugBookDto extends AbstractBook {
    private List<SmallAuthorsDto> authors = new ArrayList<>();
    private List<SmallTagsDto> tags = new ArrayList<>();
    private Integer ratingsCount;
    private Map<String, Integer> ratingsByStar = new HashMap<>();
    private List<ReviewDto> reviews = new ArrayList<>();
    private String description;

}
