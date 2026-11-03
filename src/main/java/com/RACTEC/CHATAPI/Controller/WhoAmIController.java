package com.RACTEC.CHATAPI.Controller;
import org.springframework.security.core.Authentication ;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WhoAmIController {
    @GetMapping("/api/whoami")
    public String whoAmI(Authentication authentication){
        if (authentication == null) {
            return "caller = anonymousUser";
        }
        return "caller = " + authentication.getPrincipal() ;
    }
}
