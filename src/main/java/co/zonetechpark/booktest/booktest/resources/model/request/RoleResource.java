package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
public class RoleResource {

    private Long roleId;

    @NotNull
    private String name;
}
