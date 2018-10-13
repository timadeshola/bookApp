package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.core.CustomException;
import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.repos.RoleRepository;
import co.zonetechpark.booktest.booktest.resources.model.request.RoleResource;
import co.zonetechpark.booktest.booktest.service.RoleService;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(RoleResource resource) {
        Optional<Role> optionalRole = roleRepository.findRoleByName(resource.getName());
        if(optionalRole.isPresent()) {
            throw new CustomException("Role with this name already exist, please choose a different name", HttpStatus.CONFLICT);
        }
        Role role = new Role();
        role.setName(resource.getName());
        role.setStatus(true);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(RoleResource resource) {
        Optional<Role> optionalRole = roleRepository.findById(resource.getRoleId());
        if(optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setName(resource.getName());
            role.setStatus(true);
            return roleRepository.save(role);
        }
        return null;
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public Page<Role> viewAllRoles(Predicate predicate, Pageable pageable) {
        return roleRepository.findAll(predicate, pageable);
    }

    @Override
    public Optional<Role> viewRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Optional<Role> viewRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public void toggleRoleStatus(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isPresent()) {
            Role role = optionalRole.get();
            if(role.getStatus().equals(true)) {
                role.setStatus(false);
            }else {
                role.setStatus(false);
            }
            roleRepository.saveAndFlush(role);
        }
    }
}
