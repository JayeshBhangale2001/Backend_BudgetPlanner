package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.UserProfile;
import com.bezkoder.spring.login.services.UserProfileService;
import com.bezkoder.spring.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getLoggedInUserProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userProfileService.getUserProfileByUserId(userDetails.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateLoggedInUserProfile(@RequestBody UserProfile userProfile, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userProfileService.getUserProfileByUserId(userDetails.getId())
                .map(existingProfile -> {
                    userProfile.setId(existingProfile.getId());
                    userProfile.setUser(existingProfile.getUser()); // Keep the association with the User
                    return ResponseEntity.ok(userProfileService.saveUserProfile(userProfile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.saveUserProfile(userProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @RequestBody UserProfile userProfile) {
        return userProfileService.getUserProfileById(id)
                .map(existingProfile -> {
                    userProfile.setId(existingProfile.getId());
                    return ResponseEntity.ok(userProfileService.saveUserProfile(userProfile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserProfile(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id)
                .map(profile -> {
                    userProfileService.deleteUserProfile(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
