package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import co.zonetechpark.booktest.booktest.resources.model.response.BookResponse;
import co.zonetechpark.booktest.booktest.resources.model.response.UserResponse;
import co.zonetechpark.booktest.booktest.service.BookService;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/book")
@Api(value = "api/v1/book", description = "Endpoint for book management", tags = "Book Management")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PostMapping("create")
    @ApiOperation(httpMethod = "POST", value = "Resource to create a book", response = UserResponse.class, nickname = "createBook")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Book created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different title"),
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

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PutMapping("update")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a book", response = UserResponse.class, nickname = "updateBook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Book updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the Book ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookResource resource) {
        Book book = bookService.updateBook(resource);
        BookResponse response = new BookResponse();
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDateCreated(book.getDateCreated());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @DeleteMapping("delete")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a book", responseReference = "true", nickname = "deleteBook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Book deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the Book ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteBook(
            @ApiParam(name = "bookId", value = "Provide Book ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @GetMapping("all")
    @ApiOperation(httpMethod = "GET", value = "Resource to view all book", response = Book.class, nickname = "viewAllBooks", notes = "You can perform search operations on this method (e.g www.zonetechpark.com/api/v1/user/all?name=author)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Book"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Page<Book>> viewAllBooks(@QuerydslPredicate(root = Book.class) Predicate predicate,
                                                   @ApiParam(name = "page", value = "default number of page", required = true)
                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @ApiParam(name = "size", value = "default size on result set", required = true)
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<Book> books = bookService.viewAllBooks(predicate, pageable);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("view-book")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by Book ID", response = BookResponse.class, nickname = "viewBookById")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Book"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> viewBookById(
            @ApiParam(name = "bookId", value = "Provide Book ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        Optional<Book> optionalBooks = bookService.viewBookById(bookId);
        BookResponse response = new BookResponse();
        if (optionalBooks.isPresent()) {
            Book book = optionalBooks.get();
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setDateCreated(book.getDateCreated());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("view-role-title")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by Title", response = BookResponse.class, nickname = "viewBookByTitle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Book by title"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BookResponse> viewBookByTitle(
            @ApiParam(name = "title", value = "Provide Book Title", required = true)
            @RequestParam(value = "title") String title) {
        Optional<Book> optionalBooks = bookService.viewBookByTitle(title);
        BookResponse response = new BookResponse();
        if (optionalBooks.isPresent()) {
            Book book = optionalBooks.get();
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setDateCreated(book.getDateCreated());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("view-role-author")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book by Author", response = BookResponse.class, nickname = "viewBookByAuthor")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Book by author"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<BookResponse> viewBookByAuthor(
            @ApiParam(name = "author", value = "Provide Book Author", required = true)
            @RequestParam(value = "author") String author) {
        Optional<Book> optionalBooks = bookService.viewBookByAuthor(author);
        BookResponse response = new BookResponse();
        if (optionalBooks.isPresent()) {
            Book book = optionalBooks.get();
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setDateCreated(book.getDateCreated());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("status")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
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
            @ApiParam(name = "bookId", value = "Provide Book ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        bookService.toggleBookStatus(bookId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping("rating")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @ApiOperation(httpMethod = "PUT", value = "Resource to rate book", responseReference = "true", nickname = "rateBook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle book status successful"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Rating has reach maximum number"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument")
    })
    public ResponseEntity<Boolean> rateBook(
            @ApiParam(name = "bookId", value = "Provide Book ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        bookService.rateBook(bookId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}