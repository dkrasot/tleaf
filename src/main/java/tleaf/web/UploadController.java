package tleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class UploadController {
//temporary controller for MULTIPART FILE UPLOAD

    @RequestMapping(method = RequestMethod.GET)
    public String tmp_showUploadForm() {
        return "tmp_uploadForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String tmp_processUpload(@RequestPart("file") MultipartFile file) {
        System.out.println("---->  Filename - " + file.getName() + "; filesize - " + file.getSize());
        return "redirect:/";
    }

}
