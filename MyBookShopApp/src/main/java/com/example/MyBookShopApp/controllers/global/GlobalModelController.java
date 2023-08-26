package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.dto.user.SmallUserDto;
import com.example.MyBookShopApp.mapper.UserMapper;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalModelController {
    private final BookStatusService statusService;
    private final UserMapper mapper;

    //for authorized
    @ModelAttribute("slugsByStatus")
    public Map<String, List<String>> getCartBlockBooksCount() {
        return statusService.getUserBookSlugs();
    }
    @ModelAttribute("currentUser")
    public SmallUserDto getCurrentUser(Authentication authentication) {
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return  new SmallUserDto();
        }
        return mapper.toSmallUserDto((UserEntity) authentication.getPrincipal());
    }


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }
}
