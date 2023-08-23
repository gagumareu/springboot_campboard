package coke.controller.camp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
class CampApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }

    @Test
    public void setDataSource(){

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select now()");
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            resultSet.next();

            System.out.println(resultSet.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
