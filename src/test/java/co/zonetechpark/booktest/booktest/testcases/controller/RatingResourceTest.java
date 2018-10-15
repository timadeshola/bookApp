package co.zonetechpark.booktest.booktest.testcases.controller;

import co.zonetechpark.booktest.booktest.core.utils.AppUtils;
import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.resources.controller.RatingController;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;
import co.zonetechpark.booktest.booktest.service.RatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(RatingController.class)
public class RatingResourceTest {

    @Mock
    private RatingService ratingService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RatingController(ratingService)).build();
    }

    @Test
    public void ratingTest() throws Exception {

        RatingResource resource = RatingResource.builder().id(1L).rating(98D).comment("Great Job!").bookId(1L).build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Rating rating = ratingService.rating(resource);
        if (rating != null) {

            when(ratingService.rating(resource)).thenReturn(rating);

            mockMvc.perform(post("/api/v1/rating/rating")
                    .header("Authorization", auth)
                    .content(AppUtils.toJSON(resource)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void ratingAverageTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Double ratingAverage = ratingService.ratingAverage(1L);
        if (ratingAverage != null) {

            when(ratingService.ratingAverage(1L)).thenReturn(ratingAverage);

            mockMvc.perform(get("/api/v1/rating/average")
                    .header("Authorization", auth)
                    .param("bookId", "1"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

}
