import javax.swing.*;
import java.sql.SQLException;

public class HelpPage extends WindowMenu   {
    public HelpPage(User user){
            super("ДОПОМОГА", user);
            return;
        }

        @Override
        public void show() throws SQLException {
            super.show();
            int height = 300;
            int width = frame.getWidth()-80;
            int x = 70;
            int y = 200;
            JTextArea fn = new JTextArea("ТУТ МАЄ БУТИ МАКСИМАЛЬНО КОРИСНИЙ ТЕКСТ, ПРО ТЕ, ЩО РОБИТИ ЗІ СВОЇМ ЖИТТЯМ І ЦІЄЮ ПРОГРАМОЮ. \n" +
                    "АЛЕ ПОКИ ЙОГО НЕМА, І ЯКЩО ВИ ХОЧЕТЕ ПОСПІЛКУВАТИСЯ, ПИШИТЬ oleksandraradzievska@gmail.com");
            fn.setSize(width, height);
            fn.setBackground(null);
            fn.setLocation(x, y);
            frame.add(fn);
        }
}
