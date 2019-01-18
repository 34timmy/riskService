package ru.mifi.authentication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.authentication.utils.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mifi.authentication.model.Role;
import ru.mifi.authentication.model.User;
import ru.mifi.authentication.repository.UserMapper;
import ru.mifi.authentication.utils.EmailExistException;
import ru.mifi.constructor.utils.exception.NotFoundException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by stephan on 20.03.16.
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userMapper.getByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return JwtUserFactory.create(user);
        }
    }

    @Transactional
    public void save(User user) throws SQLException {
        userMapper.updateUser(user);
    }

    @Transactional
    public void delete(String id) throws SQLException {

        userMapper.deleteUser(id);
    }

    public User get(String id) throws NotFoundException, SQLException {
        return userMapper.getUser(id);
    }

    public User getByEmail(String email) throws NotFoundException, SQLException {
        return userMapper.getByEmail(email);
    }

    public Collection<User> getAll() throws SQLException {
        return userMapper.getAllUsers();
    }

    @Transactional
    public void create(User user) throws SQLException, EmailExistException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistException("There is an account with that email adress: "
                    + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        TODO role on flag
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userMapper.createUser(user);
        userMapper.createRole(Role.ROLE_USER.toString(), user.getId());
    }

    public void evictCache() {
    }

//    @CacheEvict(value = "users", allEntries = true)
//    @Transactional
//    public void enable(int id, boolean enabled) {
//        User user = get(id);
//        user.setEnabled(enabled);
//        userMapper.save(user);
//    }

    private boolean emailExist(String email) throws SQLException {
        User user = userMapper.getByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
