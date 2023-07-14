public class Main {

    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
        new Thread(window).start();
    }
}
