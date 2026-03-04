package application.view;

import javafx.scene.layout.StackPane;

public abstract class View {

    protected StackPane root;

    public StackPane getRoot() { return root; }
    public void setRoot(StackPane root) { this.root = root; }
    public abstract void startup(); // Since clicking the back button does NOT re instantiate a new view This should be the one that is called every time you switch to the view.
    public abstract void cleanup(); // Unregister events, remove other stuff that might cause mem leak

    public View() {
        setRoot(new StackPane());
    }

}
