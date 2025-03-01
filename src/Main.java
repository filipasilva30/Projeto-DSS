import ui.GestTurnosUI;

public class Main {
    public static void main(String[] args) {
        try {
            new GestTurnosUI().run();
        } catch (Exception e) {
            System.out.println("Não foi possível arrancar o sistema: " + e.getMessage());
        }
    }
}
