//package com.softserveinc.itacademy.bikechampionship.server.service;
//
//import com.softserveinc.itacademy.bikechampionship.commons.model.User;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//public class ImageServiceImplTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private ImageServiceImpl imageService;
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(imageService).build();
//    }
//
//    @Test
//    public void saveUserAvatar() {
//
//        when(userService.getUserById(anyLong())).thenReturn(any(User.class));
//
//
//    }
//
//
//    @Test
//    public void getUserAvatar() {
//    }
//}