package com.ce.hakanarayici.recipes.api.recipe;

import com.ce.hakanarayici.recipes.config.JWTTokenUtil;
import com.ce.hakanarayici.recipes.config.JwtRequest;
import com.ce.hakanarayici.recipes.config.JwtResponse;
import com.ce.hakanarayici.recipes.config.JwtUserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SampleApi {



	@Operation(summary = "auth service", description = "auth service")
	@ApiResponse(description = "auth service")
	@RequestMapping(value = "/blyat", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {


		return ResponseEntity.ok(new RecipeApiResponse(true,"hahan"));
	}

}