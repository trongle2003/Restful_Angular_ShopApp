package com.project.shopapp.controller;

import com.project.shopapp.domain.entity.Role;
import com.project.shopapp.service.RoleService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    @ApiMessage("Get All Role")
    public ResponseEntity<?> getRole() {
        List<Role> roles = this.roleService.handleGetAllRole();
        return ResponseEntity.ok().body(roles);
    }
}
