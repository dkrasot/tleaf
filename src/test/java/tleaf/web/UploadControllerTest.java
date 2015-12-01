package tleaf.web;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import tleaf.data.ProfileRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

public class UploadControllerTest {

    @Ignore //test works at home PC only.
    @Test
    public void shouldProcessUploading() throws Exception {

        ProfileRepository mockRepository = mock(ProfileRepository.class);
        ProfileController controller = new ProfileController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        File file = new File("D:\\Java\\ideaProjects\\tleaf\\src\\test\\resources\\testImage.jpg");
        //Logic creating and populating file

        InputStream inputstream = new FileInputStream(file);
        MockMultipartFile acceptFile = new MockMultipartFile("file", inputstream);

        mockMvc.perform(fileUpload("/upload")
                .file(acceptFile)
                .param("download", "download")
                .param("some other param", "value"))
                .andDo(print())//for debug
                .andExpect(status().isOk())
                .andExpect(model().attributeHasNoErrors("file"));

    }

    //TODO Test Multipart Request
}
