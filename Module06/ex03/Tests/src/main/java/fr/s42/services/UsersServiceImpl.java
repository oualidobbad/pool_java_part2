package fr.s42.services;

import fr.s42.exceptions.AlreadyAuthenticatedException;
import fr.s42.models.User;
import fr.s42.repositories.UsersRepository;

public class UsersServiceImpl {
    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean authenticate(String login, String password){
        User user = usersRepository.findByLogin(login);

        if(user.isAuthenticated())
            throw new AlreadyAuthenticatedException(user.getLogin() + " already Authenticated");
        if (user.getPassword().equals(password))
        {
            user.setAuthenticated(true);
            usersRepository.update(user);
            return true;
        }
        else
            return false;

    }
}
