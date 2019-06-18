package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.exception.ResourceNotFoundException;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.commons.repository.UserRepository;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void checkEmailAvailabilityShowReturnFalseWhenRepositoryReturnsTrue() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        //when
        Boolean actual = userService.isEmailAvailable(anyString());

        //then
        assertFalse(actual);
        verify(userRepository, times(1)).existsByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserByIdShouldThrowExceptionWhenUserNotFound() {
        //given
        expectedException.expect(ResourceNotFoundException.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        userService.getUserById(anyLong());
    }

    @Test
    public void getUserByIdShouldReturnUserWhenHeFound() {
        //given
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        //when
        User actualUser = userService.getUserById(anyLong());

        //then
        assertEquals(user.getId(), actualUser.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void editUserProfileShouldThrowExceptionWhenUserNotFound() {
        //given
        expectedException.expect(ResourceNotFoundException.class);
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        userService.editUserProfile(user);
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void editUserProfileShouldReturnUpdatedUser() {
        //given
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setFirstName("Daniel");
        expectedUser.setLastName("Changer");
        expectedUser.setEmail("daniel_changer@blah.com");
        expectedUser.setPhoneNumber("+380124514261");

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setFirstName("Dan");
        userFromDB.setLastName("Chan");
        userFromDB.setEmail("dan_chan@blah.com");
        userFromDB.setPhoneNumber("+380000000000");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userFromDB));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        //when
        User actualUser = userService.editUserProfile(expectedUser);

        //then
        Assert.assertThat(actualUser, new SamePropertyValuesAs<>(expectedUser));

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(userFromDB);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void editUserProfileShouldReturnBadRequestExceptionWhenEmailIsNotAvailable() {
        //given
        expectedException.expect(BadRequestException.class);
        User user = new User();
        user.setId(1L);
        user.setEmail("daniel_changer@blah.com");

        User userFromDB = new User();
        userFromDB.setEmail("original_email@blah.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userFromDB));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //when
        userService.editUserProfile(user);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void changeUserPasswordShouldReturnUpdatedUser() {
        //given
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setFirstName("Artem");
        expectedUser.setLastName("Setko");
        expectedUser.setEmail("setkoartem@blah.com");
        expectedUser.setPhoneNumber("+380124514261");
        expectedUser.setPassword("password");


        User userFromDB = new User();
        userFromDB.setId(1L);

        userFromDB.setFirstName("Artem");
        userFromDB.setLastName("Setko");
        userFromDB.setEmail("setkoartem@blah.com");
        userFromDB.setPhoneNumber("+380124514261");
        userFromDB.setPassword("admin");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userFromDB));

        //when
        User actualUser = userService.changeUserPassword(expectedUser);

        //then
        Assert.assertThat(actualUser, new SamePropertyValuesAs<>(expectedUser));
        Assert.assertEquals(actualUser.getPassword(), expectedUser.getPassword());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(userFromDB);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void changeUserPasswordShouldThrowExceptionWhenUserNotFound() {
        //given
        expectedException.expect(ResourceNotFoundException.class);
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        userService.changeUserPassword(user);
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

}
