package looseweight.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import looseweight.animations.Shake;
import looseweight.handler.DatabaseHandler;
import looseweight.handler.InputData;
import looseweight.handler.User;
import looseweight.handler.Window;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SingUpController {

    public static User user = new User();
    private final Window window = new Window();
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField singUpSurname;
    @FXML
    private PasswordField singUpPasswordField;
    @FXML
    private Button singUpFinishButton;
    @FXML
    private TextField singUpLoginField;
    @FXML
    private TextField singUpName;
    @FXML
    private Button singInButton;
    @FXML
    private RadioButton manCheckBox;
    @FXML
    private RadioButton womenCheckBox;
    @FXML
    private RadioButton noCheckBox;
    @FXML
    private ImageView imagePassword;
    @FXML
    private ImageView imageLogin;
    @FXML
    private ImageView imageName;
    @FXML
    private ImageView imageSurname;

    @FXML
    void initialize() {
        singUpFinishButton.setOnAction(actionEvent -> {
            if (singUpNewUser()) {
                Stage currentStage = (Stage) singUpFinishButton.getScene().getWindow();
                currentStage.close();
                window.goToScene("MainWindow.fxml");
            }
        });
        singInButton.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) singInButton.getScene().getWindow();
            currentStage.close();
            window.goToScene("AuthWindow.fxml");
        });

        ToggleGroup group = new ToggleGroup();
        manCheckBox.setToggleGroup(group);
        manCheckBox.setSelected(true);
        womenCheckBox.setToggleGroup(group);
        noCheckBox.setToggleGroup(group);


    }

    /**
     * Регистрирует нового пользователя (добавляет в БД)
     * - в случае, если проверка данных завалена воспроизводит анимацию
     *
     * @return bool
     */
    private boolean singUpNewUser() {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        if (checkInputData()) {
            try {
                databaseHandler.signUpUser(user);
                return true;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Shake nameAnim = new Shake(singUpName);
            Shake surnameAnim = new Shake(singUpSurname);
            Shake loginAnim = new Shake(singUpLoginField);
            Shake passwordAnim = new Shake(singUpPasswordField);
            Shake imageName = new Shake(this.imageName);
            Shake imageSurname = new Shake(this.imageSurname);
            Shake imageLogin = new Shake(this.imageLogin);
            Shake imagePassword = new Shake(this.imagePassword);
            imageName.PlayAnim();
            imageSurname.PlayAnim();
            imageLogin.PlayAnim();
            imagePassword.PlayAnim();
            nameAnim.PlayAnim();
            surnameAnim.PlayAnim();
            loginAnim.PlayAnim();
            passwordAnim.PlayAnim();
        }
        return false;
    }

    /**
     * Проверяет данные в полях регистрации
     * - логин - любое, кроме пустого
     * - пароль - любое, кроме пустого
     * - имя - только буквы
     * - фамилия - только буквы
     * - страна - только буквы
     * Заполняет статический объект класса User
     *
     * @return bool
     */
    private boolean checkInputData() {
        InputData inputCheck = new InputData();

        String inputName = singUpName.getText();
        String inputSurname = singUpSurname.getText();
        String inputLogin = singUpLoginField.getText();
        String inputPassword = singUpPasswordField.getText();
        String inputSex = "";

        if (manCheckBox.isSelected()) {
            inputSex = "Мужчина";
        } else if (womenCheckBox.isSelected()) {
            inputSex = "Женщина";
        } else {
            inputSex = "Не указано";
        }

        int error = 0;
        if (inputCheck.isText(inputName)) {
            error++;
        }
        if (inputCheck.isText(inputSurname)) {
            error++;
        }
        if (inputPassword.isEmpty()) {
            error++;
        }
        if (inputLogin.isEmpty()) {
            error++;
        }

        if (error > 0) {
            error = 0;
            return false;
        } else {
            user.setName(inputName);
            user.setSurname(inputSurname);
            user.setLogin(inputLogin);
            user.setPassword(inputPassword);
            user.setSex(inputSex);
            return true;
        }

    }
}
