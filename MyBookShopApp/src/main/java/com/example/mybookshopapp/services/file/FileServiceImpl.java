package com.example.mybookshopapp.services.file;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.file.BookFileDto;
import com.example.mybookshopapp.errs.FileDownloadLimitReached;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.authors.AuthorEntity;
import com.example.mybookshopapp.model.book.file.FileDownloadEntity;
import com.example.mybookshopapp.model.enums.FileType;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.BookFileRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.FileDownloadRepository;
import com.example.mybookshopapp.services.data.ResourceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileDownloadRepository fileDownloadRepository;
    private final AuthorRepository authorRepository;
    private final BookFileRepository bookFileRepository;
    private final IAuthenticationFacade facade;
    private final Book2UserRepository book2UserRepository;
    private final ResourceStorage storage;
    private final BookRepository bookRepository;
    @Value("${download.max-amount}")
    private int maxDownloadAmount;

    @Override
    public List<BookFileDto> getFilesBySlug(String slug) {
        String status = book2UserRepository.getCodeByBookSlugAndEmail(slug, facade.getCurrentUsername());

        if ((!status.equals(StatusType.PAID.toString()) && !status.equals(StatusType.ARCHIVED.toString())) || isDownloadLimitReached(slug)) {
            return new ArrayList<>();
        }
        return getBookFileDtos(slug);
    }

    private List<BookFileDto> getBookFileDtos(String slug) {
        List<BookFileDto> filesByBookSlug = bookFileRepository.findFilesByBookSlug(slug);
        for (BookFileDto file : filesByBookSlug) {
            int length;
            try {
                length = storage.getBookFileByteArray(file.getHash()).length;
            } catch (IOException e) {
                length = 0;
            }
            file.setLength(FileUtils.byteCountToDisplaySize(length));
        }
        return filesByBookSlug;
    }


    @Override
    public List<BookFileDto> getFilesBySlugAdmin(String slug) {
        return getBookFileDtos(slug);

    }

    @Override
    public void uploadAndSetBookCover(String slug, MultipartFile file) throws IOException {
        String savePath = storage.saveNewFile(slug, file, FileType.BOOK_COVER);
        BookEntity bookToUpdate = bookRepository.findBySlug(slug)
                .orElseThrow(() -> new ItemNotFoundException("exceptionMessage.itemNotFound.book"));
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
    }

    @Override
    public void uploadAndSetAuthorPhoto(String slug, MultipartFile file) throws IOException {
        String savePath = storage.saveNewFile(slug, file, FileType.AUTHOR_PHOTO);
        AuthorEntity author = authorRepository.findBySlug(slug)
                .orElseThrow(() -> new ItemNotFoundException("exceptionMessage.itemNotFound.author"));
        author.setPhoto(savePath);
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public ResponseEntity<ByteArrayResource> downloadBook(String slug, String hash) throws IOException {
        BookEntity book = bookRepository.findBySlug(slug).orElse(null);
        FileDownloadEntity downloadEntity = fileDownloadRepository.findByUserAndBook(facade.getPrincipal(), book)
                .orElse(new FileDownloadEntity(facade.getPrincipal(), book));
        if (downloadEntity.getCount() >= maxDownloadAmount) {
            throw new FileDownloadLimitReached("exceptionMessage.downloadLimitReached");
        }
        int downloadCount = downloadEntity.getCount();
        downloadEntity.setCount(++downloadCount);
        fileDownloadRepository.save(downloadEntity);
        Path path = storage.getBookFilePath(hash);
        log.info("book file path: " + path);
        MediaType mediaType = storage.getBookFileMime(hash);
        log.info("book file mime: " + path);
        byte[] data = storage.getBookFileByteArray(hash);
        log.info("book file data length: " + data.length);
        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + path.getFileName().toString()
                )
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @Override
    public boolean isDownloadLimitReached(String slug) {
        BookEntity book = bookRepository.findBySlug(slug).orElse(null);
        FileDownloadEntity downloadEntity = fileDownloadRepository.findByUserAndBook(facade.getPrincipal(), book)
                .orElse(null);
        int downloadCount = Optional.ofNullable(downloadEntity).map(FileDownloadEntity::getCount).orElse(0);
        return downloadCount >= maxDownloadAmount;
    }
}
