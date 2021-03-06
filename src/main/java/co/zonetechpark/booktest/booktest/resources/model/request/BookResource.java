package co.zonetechpark.booktest.booktest.resources.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResource {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;
}
