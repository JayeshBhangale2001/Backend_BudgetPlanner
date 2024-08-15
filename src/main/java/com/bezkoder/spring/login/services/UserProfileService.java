package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.models.UserProfile;
import com.bezkoder.spring.login.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public Optional<UserProfile> getUserProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}
