package com.library.service;

import com.library.dto.BookResponse;
import com.library.dto.CreateBookRequest;
import com.library.exception.BadRequestException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponse register(CreateBookRequest request) {
        bookRepository.findFirstByIsbn(request.getIsbn()).ifPresent(existing -> {
            if (!existing.getTitle().equalsIgnoreCase(request.getTitle()) ||
                !existing.getAuthor().equalsIgnoreCase(request.getAuthor())) {
                throw new BadRequestException("ISBN already exists with a different title/author");
            }
        });

        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> listAll() {
        return bookRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new com.library.exception.NotFoundException("Book not found"));
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor());
    }
}

