package com.booktest.resources.controller;

import com.booktest.core.utils.AppUtils;
import com.booktest.jpa.entity.Book;
import com.booktest.jpa.entity.QBook;
import com.booktest.jpa.entity.elasticsearch.ElasticsearchBook;
import com.booktest.resources.model.request.BookResource;
import com.booktest.resources.model.response.BookResponse;
import com.booktest.resources.model.response.PaginateResponse;
import com.booktest.resources.model.response.UserResponse;
import com.booktest.service.BookService;
import com.booktest.service.JpaService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import io.swagger.annotations.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/book")
@Api(value = "api/v1/book", description = "Endpoint for book management", tags = "ElasticsearchBook Management")
public class BookController {

    private BookService bookService;
    private JpaService jpaService;
    private ModelMapper modelMapper;

    public BookController(BookService bookService, JpaService jpaService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.jpaService = jpaService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @PostMapping("create")
    @ApiOperation(httpMethod = "POST", value = "Resource to create a book", response = UserResponse.class, nickname = "createBook")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! ElasticsearchBook created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different book title"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookResource resource) {
        Book book = bookService.createBook(resource);
        BookResponse response = new BookResponse();
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDateCreated(book.getDateCreated());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @PutMapping("update")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a book", response = UserResponse.class, nickname = "updateBook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! ElasticsearchBook updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the ElasticsearchBook ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookResource resource) {
        Book book = bookService.updateBook(resource);
        BookResponse response = new BookResponse();
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDateCreated(book.getDateCreated());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @DeleteMapping("delete")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a book", responseReference = "true", nickname = "deleteBook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! ElasticsearchBook deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the ElasticsearchBook ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteBook(
            @ApiParam(name = "bookId", value = "Provide ElasticsearchBook ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @DeleteMapping("multi-delete")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a book", responseReference = "true", nickname = "deleteBooks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Books deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the ElasticsearchBook ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteBooks(
            @ApiParam(name = "bookIds", value = "Provide ElasticsearchBook IDs", required = true)
            @RequestParam(value = "bookIds") List<Long> bookIds) {
        bookService.deleteBooks(bookIds);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @GetMapping("all")
    @ApiOperation(httpMethod = "GET", value = "Resource to view all book with search params", response = Book.class, nickname = "viewAllBooks", notes = "You can perform search operations on this method (e.g www.zonetechpark.com/api/v1/book/all?name=author)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All ElasticsearchBook"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<PaginateResponse> viewAllBooks(@QuerydslPredicate(root = Book.class) Predicate predicate,
                                                         @ApiParam(name = "start", value = "default number of page", required = true) @RequestParam(value = "start", defaultValue = "0") int start,
                                                         @ApiParam(name = "limit", value = "default size on result set", required = true) @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                         @ApiParam(name = "author", value = "Author name") @RequestParam(value = "author", required = false) String author,
                                                         @ApiParam(name = "isbn", value = "ElasticsearchBook ISBN number") @RequestParam(value = "isbn", required = false) String isbn,
                                                         @ApiParam(name = "title", value = "ElasticsearchBook title") @RequestParam(value = "title", required = false) String title,
                                                         @ApiParam(name = "startDate", value = "Date book created") @RequestParam(value = "startDate", required = false) String startDate,
                                                         @ApiParam(name = "endDate", value = "Date book created") @RequestParam(value = "endDate", required = false) String endDate,
                                                         @ApiParam(name = "status", value = "Account status") @RequestParam(value = "status", required = false) Boolean status) throws ParseException {

        QBook qBook = QBook.book;
        JPAQuery<Book> bookJPAQuery = jpaService.startJPAQeuryFrom(qBook).where(predicate).orderBy(qBook.dateCreated.desc());

        if(startDate != null) {
            Date startDateQuery = DateUtils.parseDate(startDate, AppUtils.DEFAULT_DATE_TIME_FORMAT, DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern());
            bookJPAQuery.where(qBook.dateCreated.goe(new Timestamp(startDateQuery.getTime())));
        }else {
            qBook.dateCreated.isNull();
        }
        if(endDate != null) {
            Date endDateQuery = DateUtils.parseDate(endDate, AppUtils.DEFAULT_DATE_TIME_FORMAT, DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern());
            bookJPAQuery.where(qBook.dateCreated.lt(new Timestamp(DateUtils.addDays(endDateQuery, 1).getTime())));
        }

        if(author != null) {
            bookJPAQuery.where(qBook.author.equalsIgnoreCase(author));
        }else {
            qBook.author.isNull();
        }

        if(isbn != null) {
            bookJPAQuery.where(qBook.isbn.equalsIgnoreCase(isbn));
        } else {
            qBook.isbn.isNull();
        }

        if(title != null) {
            bookJPAQuery.where(qBook.title.equalsIgnoreCase(title));
        } else {
            qBook.isbn.isNull();
        }

        if(status != null) {
            bookJPAQuery.where(qBook.status.eq(status));
        } else {
            qBook.status.isNull();
        }

        QueryResults<Book> fetchResults = jpaService.fetchResults(bookJPAQuery.offset(start).limit(limit));

        PaginateResponse response = new PaginateResponse();
        response.setContents(fetchResults.getResults());
        response.setTotalElements(fetchResults.getTotal());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("bookId")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by ElasticsearchBook ID", response = BookResponse.class, nickname = "viewBookById")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a ElasticsearchBook"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> viewBookById(
            @ApiParam(name = "bookId", value = "Provide ElasticsearchBook ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        Optional<ElasticsearchBook> optionalBooks = bookService.viewBookById(bookId);
        if (optionalBooks.isPresent()) {
            ElasticsearchBook book = optionalBooks.get();
            BookResponse response = modelMapper.map(book, BookResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("title")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by Title", response = BookResponse.class, nickname = "viewBookByTitle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a ElasticsearchBook by title"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BookResponse> viewBookByTitle(
            @ApiParam(name = "title", value = "Provide ElasticsearchBook Title", required = true)
            @RequestParam(value = "title") String title) {
        Optional<ElasticsearchBook> optionalBooks = bookService.viewBookByTitle(title);
        if (optionalBooks.isPresent()) {
            ElasticsearchBook book = optionalBooks.get();
            BookResponse response = modelMapper.map(book, BookResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("author")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by Author", response = BookResponse.class, nickname = "viewBookByAuthor")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a ElasticsearchBook by author"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> viewBookByAuthor(
            @ApiParam(name = "author", value = "Provide ElasticsearchBook Author", required = true)
            @RequestParam(value = "author") String author) {
        Optional<ElasticsearchBook> optionalBooks = bookService.viewBookByAuthor(author);
        if (optionalBooks.isPresent()) {
            ElasticsearchBook book = optionalBooks.get();
            BookResponse response = modelMapper.map(book, BookResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("isbn")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by ISBN Number", response = BookResponse.class, nickname = "viewBookByIsbn")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a ElasticsearchBook by ISBN Number"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> viewBookByIsbn(
            @ApiParam(name = "isbn", value = "Provide ElasticsearchBook ISBN Number", required = true) @RequestParam(value = "isbn") String isbn) {
        Optional<ElasticsearchBook> optionalBooks = bookService.viewBookByIsbn(isbn);
        if (optionalBooks.isPresent()) {
            ElasticsearchBook book = optionalBooks.get();
            BookResponse response = modelMapper.map(book, BookResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("status")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @ApiOperation(httpMethod = "PUT", value = "Resource to toggle book status", responseReference = "true", nickname = "toggleBookStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle book status successful"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<Boolean> toggleBookStatus(
            @ApiParam(name = "bookId", value = "Provide ElasticsearchBook ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        bookService.toggleBookStatus(bookId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("findAll")
    public ResponseEntity<?> findAll(@RequestParam(required = false) String text, @RequestParam int start, @RequestParam int limit) {
        if(text == null || text.isEmpty()) {
            String message = "Enter a search parameter";
            return new ResponseEntity<>(AppUtils.toJSON(message), HttpStatus.BAD_REQUEST);
        }
        Page<ElasticsearchBook> bookList = bookService.findAll(text, start, limit);
        PaginateResponse response = new PaginateResponse();
        response.setContents(bookList.getContent());
        response.setTotalElements(bookList.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
