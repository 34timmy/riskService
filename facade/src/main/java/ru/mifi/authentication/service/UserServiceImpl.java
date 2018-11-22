//package ru.mifi.authentication.service;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import ru.mifi.authentication.model.Role;
//import ru.mifi.authentication.utils.EmailExistException;
//import ru.mifi.authentication.model.LoggedUser;
//import ru.mifi.authentication.model.User;
//import ru.mifi.authentication.repository.UserMapper;
//import ru.mifi.constructor.utils.exception.NotFoundException;
//
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.Collections;
//
//@Service("userService")
//public class UserServiceImpl
////        implements UserDetailsService
//{
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @CacheEvict(value = "users", allEntries = true)
//    public void save(User user) throws SQLException {
//        userMapper.updateUser(user);
//    }
//
//    @CacheEvict(value = "users", allEntries = true)
//    public void delete(String id) throws SQLException {
//
//        userMapper.deleteUser(id);
//    }
//
//    public User get(String id) throws NotFoundException, SQLException {
//        return userMapper.getUser(id);
//    }
//
//    public User getByEmail(String email) throws NotFoundException, SQLException {
//        return userMapper.getByEmail(email);
//    }
//
//    @Cacheable("users")
//    public Collection<User> getAll() throws SQLException {
//        return userMapper.getAllUsers();
//    }
//
//    @CacheEvict(value = "users", allEntries = true)
//    public void create(User user) throws SQLException, EmailExistException {
//        if (emailExist(user.getEmail())) {
//            throw new EmailExistException("There is an account with that email adress: "
//                    + user.getEmail());
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
////        TODO role on flag
//        user.setRoles(Collections.singleton(Role.ROLE_USER));
//        userMapper.createUser(user);
//    }
//
//    @CacheEvict(value = "users", allEntries = true)
//    public void evictCache() {
//    }
//
////    @CacheEvict(value = "users", allEntries = true)
////    @Transactional
////    public void enable(int id, boolean enabled) {
////        User user = get(id);
////        user.setEnabled(enabled);
////        userMapper.save(user);
////    }
//
//
////    @Override
////    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
////
////        User u = null;
////        try {
////            u = userMapper.getByEmail(email.toLowerCase());
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        if (u == null) {
////            throw new UsernameNotFoundException("User " + email + " is not found");
////        }
////        return new LoggedUser(u);
////    }
//
//    private boolean emailExist(String email) throws SQLException {
//        User user = userMapper.getByEmail(email);
//        if (user != null) {
//            return true;
//        }
//        return false;
//    }
//}
