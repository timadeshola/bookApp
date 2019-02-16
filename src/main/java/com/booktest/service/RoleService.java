package com.booktest.service;

import com.booktest.jpa.entity.Role;
import com.booktest.resources.model.request.RoleResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {

    Role createRole(RoleResource resource);

    Role updateRole(RoleResource resource);

    void deleteRole(Long roleId);

    Page<Role> viewAllRoles(Predicate predicate, Pageable pageable);

    Optional<Role> viewRoleById(Long roleId);

    Optional<Role> viewRoleByName(String name);

    Boolean toggleRoleStatus(Long roleId);
}
