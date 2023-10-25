package  com.cst438.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cst438.domain.User;
import com.cst438.domain.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User currentUser = repository.findByAlias(username);

	    if (currentUser == null) {
	        throw new UsernameNotFoundException("User not found.");
	    }

	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + currentUser.getRole()));

	    return new org.springframework.security.core.userdetails.User(
	            currentUser.getAlias(),
	            currentUser.getPassword(),
	            authorities
	    );
	}
}
