package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
public class BookResource {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;
}
