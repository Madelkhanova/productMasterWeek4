package hard;

public class Main {

    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>(123);
        intBox.showType();

        Box<String> strBox = new Box<>("Aruzhan");
        strBox.showType();
    }

}
