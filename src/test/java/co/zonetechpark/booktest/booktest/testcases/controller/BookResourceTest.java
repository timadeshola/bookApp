package co.zonetechpark.booktest.booktest.testcases.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.resources.controller.BookController;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import co.zonetechpark.booktest.booktest.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BookController.class)
public class BookResourceTest {

    @Mock
    private BookService bookService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService)).build();
    }

    @Test
    public void createBookTest() throws Exception {
        BookResource resource = BookResource.builder().id(20L).title("Book App").author("John Adeshola").build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Book book = bookService.createBook(resource);
        if(book!=null) {
            mockMvc
                    .perform(post("/api/v1/book/create")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void updateBookTest() throws Exception {
        BookResource resource = BookResource.builder().id(1L).title("Book App").author("John Adeshola").build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Book book = bookService.updateBook(resource);
        if(book != null) {
            mockMvc
                    .perform(put("/api/v1/book/update")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void deleteBookTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(delete("/api/v1/book/delete")
                .header("Authorization", auth)
                .param("bookId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void viewAllBooksTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> optionalBook = bookService.viewAllBooks(null, pageRequest);
        if(optionalBook != null) {

            when(bookService.viewAllBooks(null, pageRequest)).thenReturn(optionalBook);

            mockMvc.perform(get("/api/v1/book/all")
                    .header("Authorization", auth))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void viewBookByAuthorTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<Book> optionalBook = bookService.viewBookByAuthor("Peter Verhas");
        if (optionalBook.isPresent()) {

            when(bookService.viewBookByAuthor("Peter Verhas")).thenReturn(optionalBook);

            mockMvc.perform(get("/api/v1/book/view-book-author")
                    .header("Authorization", auth)
                    .param("name", "Peter Verhas"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("author").value("Peter Verhas"))
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void viewBookByIdTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<Book> optionalBook = bookService.viewBookById(1L);
        if (optionalBook.isPresent()) {

            when(bookService.viewBookById(1L)).thenReturn(optionalBook);

            mockMvc.perform(get("/api/v1/book/view-book")
                    .header("Authorization", auth)
                    .param("bookId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("author").value("Peter Verhas"))
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void toggleBookStatusTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(put("/api/v1/book/status")
                .header("Authorization", auth)
                .param("bookId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
