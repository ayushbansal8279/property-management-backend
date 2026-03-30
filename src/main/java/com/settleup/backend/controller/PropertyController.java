package com.settleup.backend.controller;

import com.settleup.backend.dto.PropertyRequestDto;
import com.settleup.backend.dto.PropertyResponseDto;
import com.settleup.backend.service.PropertyService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // ➕ Create property
    @PostMapping("/{ledgerId}")
    public PropertyResponseDto create(
            @PathVariable UUID ledgerId,
            @RequestBody PropertyRequestDto request) {

        return propertyService.createProperty(ledgerId, request);
    }

    // 📋 Get all properties
    @GetMapping("/{ledgerId}")
    public List<PropertyResponseDto> getAll(@PathVariable UUID ledgerId) {
        return propertyService.getProperties(ledgerId);
    }
}