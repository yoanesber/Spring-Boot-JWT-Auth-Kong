package com.yoanesber.jwt_auth_with_kong.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yoanesber.jwt_auth_with_kong.entity.CustomHttpResponse;
import com.yoanesber.jwt_auth_with_kong.service.DepartmentService;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    // The services needed for the controller.
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllDepartments() {
        try {
            return ResponseEntity.ok(new CustomHttpResponse(HttpStatus.OK.value(), 
                "Departments retrieved successfully", 
                departmentService.getAllDepartments()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    e.getMessage(), 
                    null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomHttpResponse> getDepartmentById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(new CustomHttpResponse(HttpStatus.OK.value(), 
                    "Department with ID " + id + " retrieved successfully",
                    departmentService.getDepartmentById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    e.getMessage(), 
                    null));
        }
    }
}
