package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UpLoadService;
import vn.hoidanit.laptopshop.service.UserService;


@Controller
public class UserController {
    
    public final UserService userService;
    private final UpLoadService upLoadService;
    private final PasswordEncoder passwordEncoder;

    
    public UserController(UserService userService, UpLoadService upLoadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.upLoadService = upLoadService;
        this.passwordEncoder = passwordEncoder;
    }

    // @RequestMapping("/")
    // public String getHomePage(Model model){
    //     List<User> users = userService.getAllUserByEmail("1@gmail.com");
    //     System.out.println(users);
    //     model.addAttribute("huy", "test");
    //     return "hello";
    // }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model){
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id){
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model){
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }
    
    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model,
        @ModelAttribute("newUser") @Valid User huyquoc, BindingResult newUserBindingResult,
        @RequestParam("hoidanitFile") MultipartFile file) {

        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println(">>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        // validate
        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        

        String avatar = this.upLoadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(huyquoc.getPassword());

        huyquoc.setAvatar(avatar);
        huyquoc.setPassword(hashPassword);

        huyquoc.setRole(this.userService.getRoleByName(huyquoc.getRole().getName()));

        this.userService.handleSaveUsers(huyquoc);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id){
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User huyquoc){
        User currentUser = this.userService.getUserById(huyquoc.getId());
        if(currentUser != null){
            currentUser.setAddress(huyquoc.getAddress());
            currentUser.setFullName(huyquoc.getFullName());
            currentUser.setPhone(huyquoc.getPhone());

            this.userService.handleSaveUsers(currentUser);
        }
        return "redirect:/admin/user";
    }


    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id){
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }


    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User huyquoc){
        this.userService.deleteUser(huyquoc.getId());
        return "redirect:/admin/user";
    }
}
