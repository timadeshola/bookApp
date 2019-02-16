package com.booktest.resources.model.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class RoleResponse {

    private String name;
    private Boolean status;
    private Timestamp dateCreated;
}
