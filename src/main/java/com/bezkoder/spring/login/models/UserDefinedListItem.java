package com.bezkoder.spring.login.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user_defined_list_items")
public class UserDefinedListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String listType;

    @NotBlank
    private String itemName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
