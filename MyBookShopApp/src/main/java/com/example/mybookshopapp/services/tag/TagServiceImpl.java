package com.example.mybookshopapp.services.tag;

import com.example.mybookshopapp.annotation.DurationTrackable;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.tag.ShortTagDto;
import com.example.mybookshopapp.dto.tag.TagDtoProjection;
import com.example.mybookshopapp.dto.admin.UploadTagDto;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.book.tags.TagEntity;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.TagRepository;
import com.example.mybookshopapp.services.bookstatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final BookStatusService statusService;


    @Override
    @Transactional
    public ResultDto saveOrUpdateTag(UploadTagDto uploadTagDto) {
        TagEntity tag;
        if(uploadTagDto.getOldSlug() == null){
            tag = new TagEntity();
        }else {
            tag = tagRepository.findBySlug(uploadTagDto.getSlug()).orElse(new TagEntity());
        }
        tag.setSlug(uploadTagDto.getSlug());
        tag.setName(uploadTagDto.getName());
        tagRepository.save(tag);
        return ResultDto.builder().result(true).build();
    }

    @Override
    @Transactional
    public ResultDto deleteTag(String slug) {
        ResultDto resultDto = new ResultDto();
        long count = tagRepository.deleteBySlug(slug);
        log.info("tags deleted: " + count);
        if(count> 0){
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale("exceptionMessage.itemNotFound.tag"));
        return resultDto;
    }

    @Override
    public List<ShortTagDto> getShortTagsList(String slug) {
        return tagRepository.getTagsByBookSlug(slug);
    }

    @DurationTrackable
    @Override
    public List<TagDtoProjection> getTagsList() {
        return tagRepository.getAllTags();
    }

    @Override
    public BooksPageDto getBooksPageByTag(RequestDto request) {
        Pageable nextPage = PageRequest.of(
                request.getOffset(),
                request.getLimit()
        );
        Page<ShortBookDtoProjection> booksPage = bookRepository.getBooksByTag(
                request.getSlug(),
                nextPage
        );
        Map<String, List<String>> slugsByStatus = statusService.getUserBookSlugs();
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .slugsByStatus(slugsByStatus)
                .build();
    }

    @Override
    public ShortTagDto getShortTagInfo(String tagSlug) {
        Optional<ShortTagDto> shortTagDtoBySlug = tagRepository.getShortTagDtoBySlug(tagSlug);
        return shortTagDtoBySlug.orElseThrow(()-> new ItemNotFoundException("exceptionMessage.itemNotFound.tag"));
    }

    @Override
    public List<ShortTagDto> getSortedTagsList() {
        return tagRepository.getShortTagDtos();
    }


}
