package ru.mifi.authentication.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.mifi.authentication.model.Role;
import ru.mifi.authentication.model.User;
import ru.mifi.constructor.model.Model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT * from users")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", javaType = Set.class, column = "role",
                    many = @Many(select = "getRolesForUser"))
    })
    List<User> getAllUsers() throws SQLException;

    @Select("SELECT * FROM users u where u.id = #{id}")
    User getUser(String id);

    @Update("UPDATE users u SET u.firstName=#{firstName},u.lastName=#{lastName} where u.id=#{id}")
    void updateUser(User user);

    @Insert("INSERT INTO users(ID, FIRSTNAME,LASTNAME, EMAIL, PASSWORD)" +
            " values (#{id},#{firstName},#{lastName}, #{email}, #{password}) ")
    void createUser(User user);

    @Delete("Delete from users u where u.id = #{id}")
    void deleteUser(String id);

    @Select("SELECT * FROM users u WHERE u.email = #{email}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", javaType = Set.class, column = "id",
                    many = @Many(select = "getRolesForUser"))
    })
    User getByEmail(String email) throws SQLException;

    @Select("SELECT r.role from roles r where r.user_id=#{id}")
    Set<Role> getRolesForUser(String id);
}
