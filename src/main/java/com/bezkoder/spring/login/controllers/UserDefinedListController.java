package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.UserDefinedListItem;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.services.UserDetailsImpl;
import com.bezkoder.spring.login.services.UserDefinedListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/user-defined-lists")
public class UserDefinedListController {

    @Autowired
    private UserDefinedListService listService;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/{listType}")
    public List<UserDefinedListItem> getItemsByListType(@PathVariable String listType, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return listService.getItemsByListType(userDetails.getId(), listType);
    }

    @PostMapping("/{items}")
    public UserDefinedListItem createItem(@RequestBody UserDefinedListItem item, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        item.setUser(user);
        return listService.saveItem(item);
    }


    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        listService.deleteItem(id);
    }
    
    @GetMapping("/list-types")
    public List<String> getListTypes(Authentication authentication) {
        // Assuming you have a service method to fetch list types for a user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return listService.getListTypesForUser(userId);
    }

}
