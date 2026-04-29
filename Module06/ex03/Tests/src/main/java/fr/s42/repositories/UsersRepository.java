package fr.s42.repositories;

import fr.s42.models.User;

public interface UsersRepository {
    
    User findByLogin(String login);
    void update(User user);
    
}

