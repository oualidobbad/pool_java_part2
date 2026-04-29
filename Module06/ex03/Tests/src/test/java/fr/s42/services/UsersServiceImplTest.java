package fr.s42.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.s42.models.User;
import fr.s42.repositories.UsersRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {
    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersServiceImpl usersServiceImpl;

    @Test
    void testAuthenticateCorrectLoginAndPassword(){
    
        User user = new User(1L, "oobbad", "hello123", false);
        when(usersRepository.findByLogin("oobbad")).thenReturn(user);
        boolean result = usersServiceImpl.authenticate("oobbad", "hello123");
        assertTrue(result);
        assertTrue(user.isAuthenticated());
        verify(usersRepository).update(user);
    }



}
