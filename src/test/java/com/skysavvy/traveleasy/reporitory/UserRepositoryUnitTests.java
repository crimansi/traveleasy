package com.skysavvy.traveleasy.reporitory;

import com.skysavvy.traveleasy.TraveleasyApplication;
import com.skysavvy.traveleasy.database.repository.UserRepository;
import com.skysavvy.traveleasy.model.user.Role;
import com.skysavvy.traveleasy.model.user.User;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveleasyApplication.class)
public class UserRepositoryUnitTests {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach //prima di ogni test creo l'utente fittizio da testare
    public void setUp() {
        MockitoAnnotations.openMocks(this); //abilita i mock
        testUser = new User();
        testUser.setId(7L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setEnabled(true);
    }

    @Test
    public void testFindByUsername() {

        //quando vado a richiamare il metodo devo trovare l'utente fittizio e lo salvo nella variabile result
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByUsername("testUser");

        //verifico che result lo abbia effettivamente trovato e non è quindi null, e che l'username corrisponda a quello dell'utente fittizio e veriico il metodo findByUsername
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    public void testExistsByUsername() {

        //quando vado a richiamare il metodo deve richiamarmi true
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        boolean result = userRepository.existsByUsername("testUser");

        assertTrue(result);
        verify(userRepository).existsByUsername("testUser");
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = userRepository.existsByEmail("test@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    public void testFindByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(7L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findById(7L);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        verify(userRepository).findById(7L);
    }
}

