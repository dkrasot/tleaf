package tleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tleaf.UserProfile;
import tleaf.data.UserProfileRepository;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserProfileController {
    private UserProfileRepository repository;

    @Autowired
    public UserProfileController(UserProfileRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new UserProfile());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@Valid UserProfile userProfile,
                                      Errors errors, //BindingResult can be used instead of Errors
                                      RedirectAttributes redirectAttributes)
            throws IllegalStateException, IOException {
        if (errors.hasErrors()) {
            return "registerForm";
        }
        repository.save(userProfile);
        redirectAttributes.addAttribute("username", userProfile.getUsername());
        redirectAttributes.addFlashAttribute(userProfile);
        // TODO : maybe send image as Flash attr?

        return "redirect:/user/{username}";
// if we add another attr by addAttribute (userId) but don't write it to redirect' {} we will get redirect:/user/USERNAME?userId=25
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model) {
        // checking Model for FLASH attr userProfile - if absent -> select from REPO
        if (!model.containsAttribute("user")) {
            UserProfile userProfile = repository.findByUsername(username);
            model.addAttribute(userProfile);
        }
        return "profile";
    }
}

// ...Notes...
// if name of User in Model would be "profile": @Valid User profile -> Then we must annotating it with @ModelAttribute("profile"):
//   @Valid @ModelAttribute("profile") User profile, ... in HTML template:  th:object="${profile}"