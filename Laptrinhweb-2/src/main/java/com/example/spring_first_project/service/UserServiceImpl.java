package com.example.spring_first_project.service;

import com.example.spring_first_project.dto.*;
import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.repository.CompanyRepository;
import com.example.spring_first_project.repository.RoleRepository;
import com.example.spring_first_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtService jwtService;

    @Override
    public LoginResponse login(UserLoginDto userLoginDto, String token) {
        UserDemo userFindByEmail = userRepository.findByEmail(userLoginDto.getEmail());
        if (userFindByEmail == null) {
            throw new UsernameNotFoundException(userLoginDto.getEmail());
        }
        boolean isUserMatched = bCryptPasswordEncoder.matches(userLoginDto.getPassword(), userFindByEmail.getPassword());
        if (isUserMatched) {
            UserDemo user = userRepository.findByEmail(userLoginDto.getEmail());
            user.setToken(token);
            userRepository.save(user);
            System.out.println(user.getAuthorities());
            for (Role role : user.getAuthorities()){
                System.out.println(role);
                System.out.println(role.getAuthority());
                return new LoginResponse(userLoginDto, token, role.getAuthority(), user.getEmail());
            }
        }
        throw new UsernameNotFoundException(userLoginDto.getEmail());
    }

    @Override
    public List<UserDemo> findAll() {
        List<UserDemo> users = userRepository.findAll();
        return users;
    }

    @Override
    public void save(UserRegistrationDto userRegistrationDto) {
        Company company = new Company("Company 1", new ArrayList<UserDemo>());
        if (company != null) {

        }
        companyRepository.save(company);

        UserDemo user = new UserDemo();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));

        user.setCompany(company);

        Role role = new Role("ROLE_USER");
        user.setAuthorities(List.of(role));
        roleRepository.save(role);

        company.getUsers().add(user);

        // Lưu người dùng
        userRepository.save(user);
    }

    @Override
    public UserDemo saveUserWithApi(UserRegistrationApiDto userRegistrationApiDto) {
        System.out.println("userRegistrationApiDto: " + userRegistrationApiDto.getAuthorities());

        if (existedEmailChecking(userRegistrationApiDto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        UserDemo user = new UserDemo();
        user.setFirstName(userRegistrationApiDto.getFirstName());
        user.setLastName(userRegistrationApiDto.getLastName());
        user.setEmail(userRegistrationApiDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationApiDto.getPassword()));

        Integer companyId = userRegistrationApiDto.getCompany();
        Company company = null;

        if (companyId != null) {
            company = companyRepository.findById(companyId).orElse(null);
            if (company == null) {
                System.out.println("Company with ID " + companyId + " not found. User will be created without a company.");
                user.setCompany(null);
            } else {
                user.setCompany(company);
            }
        } else {
            System.out.println("Company ID is null. User will be created without a company.");
            user.setCompany(null);
        }

        // Add role
        List<Role> roles = new ArrayList<>();
        for (String roleName : userRegistrationApiDto.getAuthorities()) {
            Role roleExisted = roleService.getRoleByName(roleName);
            if (roleExisted == null) {
                System.out.println(roleName);
                Role role = new Role(roleName);
                roleRepository.save(role);
                roles.add(role);
            }else {
                System.out.println(roleExisted);
                roles.add(roleExisted);
            }
        }

        user.setAuthorities(roles);
        UserDemo newUser = userRepository.save(user);

        // Update user in company
        if (company != null) {
            company.getUsers().add(newUser);
            companyRepository.save(company);
            System.out.println("company:" + company);
        }

        return newUser;
    }

    @Override
    public UserDemo updateUserWithApi(UserUpdateApiDto userUpdateApiDto, int id) {
        UserDemo user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Company company = companyRepository.findById(userUpdateApiDto.getCompany()).orElseThrow(
                () -> new UsernameNotFoundException("Company not found")
        );

        user.setFirstName(userUpdateApiDto.getFirstName());
        user.setLastName(userUpdateApiDto.getLastName());
        user.setEmail(userUpdateApiDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userUpdateApiDto.getPassword()));
        user.setCompany(company);

        // Add role
        List<Role> roles = new ArrayList<>();
        for (String roleName : userUpdateApiDto.getAuthorities()) {
            Role roleExisted = roleService.getRoleByName(roleName);
            if (roleExisted == null) {
                System.out.println(roleName);
                Role role = new Role(roleName);
                roleRepository.save(role);
                roles.add(role);
            }else {
                System.out.println(roleExisted);
                roles.add(roleExisted);
            }
        }

        user.setAuthorities(roles);
        UserDemo updateUser = userRepository.save(user);

        // Update user in company
        company.getUsers().add(updateUser);
        companyRepository.save(company);
        System.out.println("company:" + company);

        return updateUser;
    }

    @Override
    public UserDemo decentralizationWithApi(DecentralizationDto decentralizationDto, int id) {
        UserDemo user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        // Add role
        List<Role> roles = new ArrayList<>();
        for (String roleName : decentralizationDto.getAuthorities()) {
            Role roleExisted = roleService.getRoleByName(roleName);
            if (roleExisted == null) {
                System.out.println(roleName);
                Role role = new Role(roleName);
                roleRepository.save(role);
                roles.add(role);
            }else {
                System.out.println(roleExisted);
                roles.add(roleExisted);
            }
        }
        user.setAuthorities(roles);
        UserDemo decentralizationUser = userRepository.save(user);
        return decentralizationUser;
    }

    @Override
    public void deleteUserWithApi(int id) {

        UserDemo user =  userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Company company = companyRepository.findById(id).orElse(null);

        if (user != null && user.getCompany() != null) {
            user.setCompany(null);
        }
        userRepository.delete(user);
    }

    @Override
    public UserDemo findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDemo getUserById(int id) {

        UserDemo user;
        user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        return user;
    }

    @Override
    public Boolean existedEmailChecking(String email) {
        UserDemo user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public Collection<Role> getUserRoles(int id) {
        UserDemo user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return user.getAuthorities();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDemo user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }
}
