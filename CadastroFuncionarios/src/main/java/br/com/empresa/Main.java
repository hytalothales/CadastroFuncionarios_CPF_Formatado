package br.com.empresa;

import br.com.empresa.repository.FuncionarioRepository;
import br.com.empresa.service.FuncionarioService;
import br.com.empresa.view.FuncionarioView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Path arquivo = Path.of("dados", "funcionarios.csv");
        FuncionarioRepository repository = new FuncionarioRepository(arquivo);
        FuncionarioService service = new FuncionarioService(repository);
        FuncionarioView view = new FuncionarioView(service);

        Scene scene = new Scene(view, 1120, 760);
        var css = getClass().getResource("/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("Sistema de Cadastro de Funcionários");
        stage.setMinWidth(980);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
