package tleaf.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tleaf.Profile;
import tleaf.data.ProfileRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

public class ProfileControllerTest {

    // TODO TEST THIS? /profile/signup for signing up + /profile/{username} where username=signup >>> ambiguity
    // make controller of /signup apart?

// 2nd scheme of mocking
//    @Mock
//    private ProfileRepository mockRepository; OR some service
//    @InjectMocks
//    public ProfileController controller = new ProfileController( repo ? );
//    @Before
//    public void setUp() {
//        this.mockMvc = standaloneSetup(this.controller).build();
//    }

    @Test
    public void shouldShowSignUpForm() throws Exception {
        ProfileRepository mockRepository = mock(ProfileRepository.class);
        ProfileController controller = new ProfileController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/profile/signup"))
                .andExpect(view().name("signUp"));
    }

    @Test
    // correct without file uploading
    public void shouldProcessSigningUp() throws Exception {
        ProfileRepository mockRepository = mock(ProfileRepository.class);
        Profile unsaved = new Profile("jbauer", "24hours", "jbauer@ctu.gov");
        Profile saved = new Profile(24L, "jbauer", "24hours", "jbauer@ctu.gov");
        when(mockRepository.save(unsaved)).thenReturn(saved);

        ProfileController controller = new ProfileController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/profile/signup")
                .param("username", "jbauer")
                .param("password", "24hours")
                .param("email", "jbauer@ctu.gov"))
                .andExpect(model().attribute("username", unsaved.getUsername()))
                .andExpect(flash().attribute("profile", unsaved))
                .andExpect(redirectedUrl("/profile/jbauer"));
        verify(mockRepository, atLeastOnce()).save(unsaved);

//        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);
//        mockRedirectAttributes.addAttribute("username",unsaved.getUsername());
//        mockRedirectAttributes.addFlashAttribute("profile",unsaved);
//        verify(mockRedirectAttributes, times(1)).addAttribute(eq("username"),anyString());
//        verify(mockRedirectAttributes, times(1)).addFlashAttribute("profile", unsaved);
    }


    @Test
    public void shouldFailValidationWithNoData() throws Exception{
        ProfileRepository mockRepository = mock(ProfileRepository.class);
        ProfileController controller = new ProfileController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/profile/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signUp"))
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrors("profile","username", "password", "email"));//Profile and fields list:
    }

// TODO check MULTIPART request - file upload ... MAKE IT in other Test class
//    @Test
//    public void when_Http_Post_Correct_File() throws Exception {
//
//        ProfileRepository mockRepository = mock(ProfileRepository.class);
//        ProfileController controller = new ProfileController(mockRepository);
//        MockMvc mockMvc = standaloneSetup(controller).build();
//
//        File report = new File("report.dbf");
//        //Logic creating and populating dbf
//
//        InputStream inputstream = new FileInputStream(report);
//        MockMultipartFile acceptFile = new MockMultipartFile("importFile", inputstream);
//
//        mockMvc.perform(fileUpload("/reports")
//                .file(acceptFile)
//                .param("download", "download")
//                .param("some other param", "value"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(model().attributeHasNoErrors("importFile"));
//
//    }
}
