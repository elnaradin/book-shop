package com.example.MyBookShopApp.dto.book.request;

import com.example.MyBookShopApp.model.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
public class RequestDto extends PubDatesDto {
    private String slug;
    private String searchWord;
    private Integer offset;
    private Integer limit;
    private StatusType status;
    private Sort.Direction sort;

}
