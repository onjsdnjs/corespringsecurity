package io.security.corespringsecurity.controller.admin;


import org.aspectj.bridge.Message;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    @GetMapping(value="/admin")
    public String home(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = null;
        if(principal != null && principal instanceof User){
            username = ((User) principal).getUsername();
        }

        model("userId", username);


        return "admin/home";
    }

    @PreAuthorize("isAuthenticated() and (( #user.name == principal.name ) or hasRole('ROLE_ADMIN'))")
    @RequestMapping( value = "", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateUser(User user ){
        messageService.updateMessage( user );
        return new ResponseEntity<Message>( new Message("",null, true), HttpStatus.OK );
    }

}
