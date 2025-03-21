package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.BO.BOFactory;
import org.example.BO.LoginBO;
import org.example.DAO.DAOFactory;
import org.example.DAO.Impl.UserDAO;
import org.example.DTO.LoginDTO;
import org.example.Entity.User;
import org.example.util.PasswordVerifier;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoginController {
    public Label LblLogin;
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DaoType.User);
    LoginBO loginBO = (LoginBO) BOFactory.getBoFactory().getBo(BOFactory.BoType.Login);

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    public void initialize() throws SQLException, ClassNotFoundException {
        genarateNextID();
    }

    private void genarateNextID() throws SQLException, ClassNotFoundException {
      String LoginID = loginBO.generateNextId();
      LblLogin.setText(LoginID);
    }

    @FXML
    void handleLoginAction(ActionEvent event) {
        String ID = LblLogin.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        Date date = Date.valueOf(LocalDate.now());

        try {
            checkCredential(ID,username, password,date, event);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred during login.").show();
        }
    }

    private void checkCredential(String ID,String username, String password,Date date ,ActionEvent event) throws Exception {
        User user = userDAO.searchByUsername(username);

        if (user != null) {
            if (PasswordVerifier.verifyPassword(password, user.getPassword())) {
                LoginDTO loginDTO = new LoginDTO(ID,user.getUser_id(),date);
                boolean isSave = loginBO.save(loginDTO);
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Login successful!").show();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoard.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.centerOnScreen();
                    stage.show();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Login failed!").show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid password. Please try again.").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "User not found. Please check your username.").show();
        }
    }
}
