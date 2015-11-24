package tleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tleaf.Profile;
import tleaf.data.ProfileRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private ProfileRepository repository;

    @Autowired
    public ProfileController(ProfileRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new Profile());
        return "signUp";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String processRegistrationWithFile(@RequestPart(value = "profilePicture") MultipartFile profilePicture,
                                       @Valid Profile profile,
                                       Errors errors, //BindingResult can be used instead of Errors
                                       RedirectAttributes redirectAttributes)
            throws IllegalStateException, IOException {
        if (errors.hasErrors()) {
            return "signUp";
        }
        repository.save(profile);
        profilePicture.transferTo(new File(profilePicture.getOriginalFilename()));
        //profilePicture.transferTo(new File("/tmp/demo-leaf/"+ user.getUsername() + ".jpg"));
        //TODO Later - resolve problem with multipart-requests on GlassFish server - maybe try CommonsMultipartResolver ...
        // WebSphere is fine with StandardSMPResolver

        //GlassFish - file creating at this directory: glassfish4\glassfish\domains\domain1\generated\jsp\Tleaf-1.0-SNAPSHOT
        //TODO send Image path for viewing in profile.html ?
        redirectAttributes.addAttribute("username", profile.getUsername());
        redirectAttributes.addFlashAttribute(profile);
        return "redirect:/profile/{username}";
// if we add another attr by addAttribute (userId) but don't write it to redirect' {} we will get redirect:/user/USERNAME?userId=25
    }

    //@RequestPart(value = "profilePicture", required=false) Part fileBytes
//1. Part - alternative to MultipartFile for Servlet 3.0+ containers
// we don't need configuring of StandardServletMultipartResolver bean if using Part instead of MultipartFile

    //@RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String processRegistration(@Valid ProfileForm profileForm,
                                      Errors errors, //BindingResult can be used instead of Errors
                                      RedirectAttributes redirectAttributes)
            throws IllegalStateException, IOException {
        if (errors.hasErrors()) {
            return "signUp";
        }
        Profile profile = profileForm.toUser();
        repository.save(profile);
        MultipartFile profilePicture = profileForm.getProfilePicture();


        profilePicture.transferTo(new File(profilePicture.getOriginalFilename()));
        //profilePicture.transferTo(new File("/tmp/demo-leaf/"+ user.getUsername() + ".jpg"));
        //TODO Later - resolve problem with multipart-requests on GlassFish server - maybe try CommonsMultipartResolver ...
        // WebSphere is fine with StandardSMPResolver

        //GlassFish - file creating at this directory: glassfish4\glassfish\domains\domain1\generated\jsp\Tleaf-1.0-SNAPSHOT
        //TODO send Image path for viewing in profile.html?
        redirectAttributes.addAttribute("username", profile.getUsername());
        redirectAttributes.addFlashAttribute(profile);
        return "redirect:/profile/{username}";
// if we add another attr by addAttribute (userId) but don't write it to redirect' {} we will get redirect:/user/USERNAME?userId=25
    }




    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model) {
        // checking Model for FLASH attr userProfile - if absent -> select from REPO
        if (!model.containsAttribute("user")) {
            Profile profile = repository.findByUsername(username);
            model.addAttribute(profile);
        }
        return "profile";
    }
}

// ...Notes...
// if name of User in Model would be "profile": @Valid User profile -> Then we must annotating it with @ModelAttribute("profile"):
//   @Valid @ModelAttribute("profile") User profile, ... in HTML template:  th:object="${profile}"