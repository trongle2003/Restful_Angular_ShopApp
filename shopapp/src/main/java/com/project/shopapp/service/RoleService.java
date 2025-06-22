package com.project.shopapp.service;

import com.project.shopapp.domain.entity.Role;
import com.project.shopapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> handleGetAllRole() {
        return this.roleRepository.findAll();
    }
}
