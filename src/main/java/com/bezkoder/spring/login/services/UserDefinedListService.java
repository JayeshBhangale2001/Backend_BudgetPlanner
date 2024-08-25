package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.models.UserDefinedListItem;
import com.bezkoder.spring.login.repository.UserDefinedListItemRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDefinedListService {
    @Autowired
    private UserDefinedListItemRepository listItemRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserDefinedListItem> getItemsByListType(Long userId, String listType) {
        return listItemRepository.findByUser_IdAndListType(userId, listType);
    }

    public UserDefinedListItem saveItem(UserDefinedListItem item) {
        return listItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        listItemRepository.deleteById(id);
    }
    
    public List<String> getListTypesForUser(Long userId) {
    	return listItemRepository.findDistinctListTypesByUserId(userId);
    }

}
