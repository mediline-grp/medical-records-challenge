package com.mediline.medicalrecords.service;

import com.mediline.medicalrecords.entity.User;
import com.mediline.medicalrecords.exception.ResourceNotFoundException;
import com.mediline.medicalrecords.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 2.2 Implement getAllUsers() method that returns all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2.3 Implement getUserById(Long id) method that returns Optional<User> or throws ResourceNotFoundException
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // 2.4 Implement createUser(User user) method that saves and returns the new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // 2.5 Implement updateUser(Long id, User userDetails) method that updates existing user and returns updated entity
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);
        
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setBirthDate(userDetails.getBirthDate());
        existingUser.setActive(userDetails.getActive());
        
        return userRepository.save(existingUser);
    }

    // 2.6 Implement deleteUser(Long id) method that deletes user by ID
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
