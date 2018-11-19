package ru.mifi.authentication.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.mifi.authentication.model.User;
import ru.mifi.constructor.model.Model;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT * from users")
    List<User> getAllUsers() throws SQLException;

    @Select("SELECT * FROM users u where u.id = #{id}")
    User getUser(String id);

    @Update("UPDATE users u SET u.name=#{name} where u.id=#{id}")
    void updateUser(User user);

    @Insert("INSERT INTO users(ID, NAME, EMAIL, PASSWORD)" +
            " values (#{id},#{name},#{email}, #{password}) ")
    void createUser(User user);

    @Delete("Delete from users u where u.id = #{id}")
    void deleteUser(String id);


    @Select("SELECT * FROM users u WHERE u.email = #{email}")
    User getByEmail(String email);
}
