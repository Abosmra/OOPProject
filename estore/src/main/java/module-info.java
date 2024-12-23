module project {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;

  exports project;
  exports project.controllers to javafx.fxml;

  opens project to javafx.fxml;
  opens project.controllers to javafx.fxml;
}
