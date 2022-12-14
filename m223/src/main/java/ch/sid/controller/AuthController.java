package ch.sid.controller;

import ch.sid.model.Member;
import ch.sid.security.JwtServiceHMAC;
import ch.sid.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    JwtServiceHMAC jwtService;
    MemberService memberService;

    @Autowired
    public AuthController(JwtServiceHMAC jwtService, MemberService memberService) {
        this.jwtService = jwtService;
        this.memberService = memberService;
    }

    @Operation(
            summary = "Login",
            description = "Login  with existing username and password",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member user) {
        ArrayList<String> scopes = new ArrayList<String>();
        Optional<Member> member = memberService.getByEmailAndPassword(user.getEmail(), user.getPassword());
        if(member.isEmpty()){
            throw new IllegalArgumentException("Wrong username or Password");
        }

        if (member.get().getRole().equals("MEMBER")) {
            scopes.add("MEMBER");
        }else if(member.get().getRole().equals("ADMIN")){
            scopes.add("ADMIN");
        }

        String id = UUID.randomUUID().toString();
        var jwt = jwtService.createNewJWT(id, member.get().getId().toString(), member.get().getEmail(), scopes);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }

    @Operation(
            summary = "Register",
            description = "Register a new User with name, lastname, email & password",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Member member){
        return new ResponseEntity(memberService.create(member), HttpStatus.OK);
    }
}
