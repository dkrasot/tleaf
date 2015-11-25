package tleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//////////////////////////////@Controller
public class SecurityController {

    @RequestMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/login?error")
    public String showLoginPageWithError(Model model) {
        model.addAttribute("loginError",true);
        return "login";
    }

    //TODO later - make Login-, Error- ... Controllers


}
