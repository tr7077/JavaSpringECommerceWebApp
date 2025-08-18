package com.teorerras.buynowdotcom.data;


import com.teorerras.buynowdotcom.model.Role;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.repository.RoleRepository;
import com.teorerras.buynowdotcom.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultRoles(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoles(Set<String> roles) {
        roles.stream()
                .filter(role -> Optional.ofNullable(roleRepository.findByName(role))
                        .isEmpty()).map(Role::new).forEach(roleRepository::save);
    }


    private void createDefaultAdminIfNotExists() {
        Role adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        for(int i=0; i<3; i++) {
            String defEmail = "admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defEmail)) continue;

            User newUser = new User();
            newUser.setFirstName("Admin");
            newUser.setLastName("Shop User" + i);
            newUser.setEmail(defEmail);
            newUser.setPassword(passwordEncoder.encode("123456"));
            newUser.setRoles(Set.of(adminRole));
            userRepository.save(newUser);
        }
    }
}
