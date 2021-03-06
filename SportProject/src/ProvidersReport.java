import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;

public class ProvidersReport extends Report {
	private DataBase dataBase;
	private String query;
	private ResultSet rs;
	private JButton OK, OK1, OK2;
	private JButton report1, report2, report3, report4;
	private JComboBox cb;
	private JTextField sum, town;
	private JScrollPane mainScroll;
	private JPanel mainPanel = new JPanel();
	private JTable table;
	private JScrollPane scrollPane, scrollPane1, scrollPane2, scrollPane3;
	private JTable table1, table2, table3, table4;

	public ProvidersReport(String nameT, User user) {
		super(nameT, user);
	}

	public void show() throws SQLException {
		super.show();
		DataBase db = new DataBase();
		this.query = "select * from providers;";
		this.rs = db.select(query);
		JLabel repOf = new JLabel("Звіти по Постачальникам:", SwingConstants.CENTER);
		repOf.setFont(new Font("", Font.ITALIC, 30));
		repOf.setSize(400, 30);
		repOf.setLocation(380, 170);
		frame.add(repOf);

		int y = mainPanel.getY();
		int x = mainPanel.getX();
		int plus = 20;
		int between = 50;

		// перший звіт
		JLabel rep1 = new JLabel("Переглянути всіх постачальників, в яких купувався певний товар:");
		rep1.setFont(new Font("", Font.PLAIN, 15));
		rep1.setSize(500, 30);
		rep1.setLocation(x + plus, y + plus);

		cb = AddItemPage.createComboBox("name_of_goods", "nomenofdel", 200, 30, rep1.getWidth(), rep1.getY());
		// перший виклик метода не показує таблицю,з'являється лише після
		// натискання на інший айтем окрім першого
		scrollPane = new JScrollPane();
		// providersByGood(rep1.getX(), rep1.getHeight() + 20, query, q);
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				try {
					mainPanel.remove(scrollPane);
					String q = "" + cb.getSelectedItem();
					providersByGood(rep1.getX(), rep1.getHeight() + 20, query, q);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		report1 = new JButton("ЕКСПОРТ В Excel");
		report1.setSize(150, 30);
		report1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String q = "" + cb.getSelectedItem();
				String q1 = "Пост " + cb.getSelectedItem();
				try {
					Export exp1 = new Export(table1, "Постачальники " + q, q1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// другий звіт
		JLabel rep2 = new JLabel("Переглянути всіх постачальників, в яких купувався товар в певний проміжок часу:");
		rep2.setFont(new Font("", Font.PLAIN, 15));
		rep2.setSize(700, 30);
		rep2.setLocation(x + plus, rep1.getY() + 350 + between);

		JDateChooser calendar_from = new JDateChooser();
		calendar_from.setCalendar(Calendar.getInstance());
		calendar_from.setSize(100, 30);
		calendar_from.setLocation(rep2.getX() + 580, rep2.getY());
		calendar_from.setDateFormatString("yyyy-MM-dd");

		JDateChooser calendar_to = new JDateChooser();
		calendar_to.setCalendar(Calendar.getInstance());
		calendar_to.setSize(100, 30);
		calendar_to.setLocation(calendar_from.getX() + 130, rep2.getY());
		calendar_to.setDateFormatString("yyyy-MM-dd");

		String fromStr = calendar_from.getDate().toString();
		String toStr = calendar_to.getDate().toString();

		OK = new JButton("ok");
		OK.setSize(50, 30);
		OK.setLocation(calendar_to.getX() + 120, rep2.getY());
		String query1 = "SELECT distinct contracts.provider_id,providers.name_of_provider,date_from,"
				+ "date_to,name_of_goods FROM providers inner join contracts on providers.provider_id="
				+ "contracts.provider_id WHERE contracts.date_to >= '" + toStr + "' and contracts.date_from <= '"
				+ fromStr + "';";
		scrollPane1 = new JScrollPane();
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainPanel.remove(scrollPane1);
					SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
					String fromStr1 = format2.format(calendar_from.getDate());
					String toStr1 = format2.format(calendar_to.getDate());
					System.out.println(fromStr1);
					System.out.println(toStr1);
					providersByTime(x + plus, rep2.getY() + 30, query1, fromStr1, toStr1);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		report2 = new JButton("ЕКСПОРТ В Excel");
		report2.setSize(150, 30);
		report2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String q1 = "Пост " + "За Датою";
				try {
					Export exp1 = new Export(table2, "Постачальники", q1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// третій звіт
		JLabel rep3 = new JLabel("Переглянути всіх постачальників, в яких купувалися товари на певну суму : >= ");
		rep3.setFont(new Font("", Font.PLAIN, 15));
		rep3.setSize(700, 30);
		rep3.setLocation(x + plus, rep2.getY() + 350 + between);

		sum = new JTextField("");
		sum.setSize(70, 30);
		sum.setLocation(rep3.getX() + 550, rep3.getY());

		OK1 = new JButton("ok");
		OK1.setSize(50, 30);
		OK1.setLocation(rep3.getX() + 630, rep3.getY());

		String summ = sum.getText();
		String query2 = "SELECT distinct bill.provider_id,providers.name_of_provider,bill.sum_of_bill,bill.date_of_bill from bill inner join providers on bill.provider_id=providers.provider_id where sum_of_bill >= "
				+ summ + ";";
		scrollPane2 = new JScrollPane();

		OK1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainPanel.remove(scrollPane2);
					String summ = sum.getText();
					providersBySum(x + plus, rep3.getY() + 30, query2, summ);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		report3 = new JButton("ЕКСПОРТ В Excel");
		report3.setSize(150, 30);
		report3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String q1 = "Пост " + "За Сумою";

				try {
					Export exp1 = new Export(table3, "Постачальники З Сумою", q1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// четвертий звіт
		JLabel rep4 = new JLabel("Переглянути всіх постачальників з певного міста : ");
		rep4.setFont(new Font("", Font.PLAIN, 15));
		rep4.setSize(700, 30);
		rep4.setLocation(x + plus, rep3.getY() + 350 + between);

		town = new JTextField("");
		town.setSize(70, 30);
		town.setLocation(rep4.getX() + 350, rep4.getY());

		OK2 = new JButton("ok");
		OK2.setSize(50, 30);
		OK2.setLocation(rep4.getX() + 430, rep4.getY());

		String townn = town.getText();
		String query3 = "SELECT distinct * from providers where city = '" + townn + "';";
		scrollPane3 = new JScrollPane();

		OK2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainPanel.remove(scrollPane3);
					String townn = town.getText();
					providersByTown(x + plus, rep4.getY() + 30, query3, townn);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		report4 = new JButton("ЕКСПОРТ В Excel");
		report4.setSize(150, 30);
		report4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String q1 = "Пост " + "З Міста";
				try {
					Export exp1 = new Export(table4, "Постачальники З Міста", q1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// -----------------------------------------------------------------------
		// ДОДАВАННЯ ВСІХ ЕЛЕМЕНТІВ НА ПАНЕЛЬ І НА СКРОЛПЕЙН | ДОДАВАННЯ НА
		// ФРЕЙМ
		// -----------------------------------------------------------------------
		mainPanel.setLayout(null);
		mainPanel.setVisible(true);
		mainPanel.setPreferredSize(new Dimension(950, 1600));

		mainPanel.add(rep1);
		mainPanel.add(cb);
		mainPanel.add(rep2);
		mainPanel.add(report1);
		mainPanel.add(calendar_from);
		mainPanel.add(calendar_to);
		mainPanel.add(OK);
		mainPanel.add(report2);
		mainPanel.add(rep3);
		mainPanel.add(sum);
		mainPanel.add(OK1);
		mainPanel.add(report3);
		mainPanel.add(rep4);
		mainPanel.add(town);
		mainPanel.add(OK2);
		mainPanel.add(report4);

		mainScroll = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainScroll.setLocation(frame.getX() + 10, frame.getHeight() / 3 + 5);
		mainScroll.setSize(frame.getWidth() - 20, 450);

		report1.setLocation(mainScroll.getWidth() - 200, rep2.getY() - 40);
		report2.setLocation(mainScroll.getWidth() - 200, rep3.getY() - 40);
		report3.setLocation(mainScroll.getWidth() - 200, rep4.getY() - 40);
		report4.setLocation(mainScroll.getWidth() - 200, rep4.getY() + 340);

		frame.add(mainScroll);
	}

	// -----------------------------------------------------------------------
	// МЕТОДИ ЗАПИТІВ
	// -----------------------------------------------------------------------

	// метод для 1 запиту
	public void providersByGood(int x, int y, String query, String helpquery) throws SQLException {
		DataBase db = new DataBase();
		query = "select distinct contracts.provider_id,providers.name_of_provider,date_from,date_to,name_of_goods from providers inner join contracts on providers.provider_id=contracts.provider_id where contracts.name_of_goods='"
				+ helpquery + "';";
		paintTheTable(query, x, y, db, 1);
	}

	// метод для 2 запиту
	public void providersByTime(int x, int y, String query1, String from, String to) throws SQLException {
		DataBase db = new DataBase();
		query1 = "SELECT distinct contracts.provider_id,providers.name_of_provider,date_from,date_to,name_of_goods FROM providers inner join contracts on providers.provider_id=contracts.provider_id WHERE contracts.date_to >= '"
				+ to + "' and contracts.date_from <= '" + from + "';";
		System.out.println(from);
		System.out.println(to);
		paintTheTable(query1, x, y, db, 2);
	}

	// метод для 3 запиту
	public void providersBySum(int x, int y, String query2, String sum) throws SQLException {
		DataBase db = new DataBase();
		query2 = "SELECT distinct bill.provider_id,providers.name_of_provider,bill.sum_of_bill,bill.date_of_bill from bill inner join providers on bill.provider_id=providers.provider_id where sum_of_bill >= "
				+ sum + ";";
		paintTheTable(query2, x, y, db, 3);
	}

	// метод для 4 запиту
	public void providersByTown(int x, int y, String query3, String town) throws SQLException {
		DataBase db = new DataBase();
		query3 = "SELECT distinct * from providers where city = '" + town + "';";
		paintTheTable(query3, x, y, db, 4);
	}

	// -----------------------------------------------------------------------
	// ЗАПОВНЕННЯ ТАБЛИЦІ
	// -----------------------------------------------------------------------
	private void paintTheTable(String query, int x, int y, DataBase db, int check) {

		int rowHeight = 50;
		try {
			ResultSet rs = db.select(query);
			int colCount = rs.getMetaData().getColumnCount();
			rs.last();
			int lineCount = rs.getRow();
			rs.beforeFirst();

			Vector menu = new Vector();
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rs.getMetaData().getColumnLabel(i));
				menu.add(rs.getMetaData().getColumnLabel(i));
			}
			// відображення інфи з бд
			Vector res = new Vector();
			Vector row = new Vector();
			Vector[] help = new Vector[lineCount];
			for (int i = 0; i < lineCount; i++) {
				rs.next();
				for (int j = 0; j < colCount; j++) {

					row.add(rs.getString(j + 1));
				}
				help[i] = (Vector) row.clone();
				row.clear();
				res.add(help[i]);
			}
			switch (check) {
			case 1:
				table = new JTable(res, menu);
				table1 = table;
				table1.setEnabled(false);
				scrollPane = new JScrollPane(table);
				scrollPane.setLocation(x, y + 10);
				table.setRowHeight(rowHeight);
				if (lineCount <= 7)
					scrollPane.setSize(mainPanel.getWidth() - 30, 300);
				else
					scrollPane.setSize(mainPanel.getWidth() - 30, 300);

				mainPanel.add(scrollPane);
				break;
			case 2:
				table = new JTable(res, menu);
				table2 = table;
				table2.setEnabled(false);
				scrollPane1 = new JScrollPane(table);
				scrollPane1.setLocation(x, y + 10);
				table.setRowHeight(rowHeight);
				if (lineCount <= 7)
					scrollPane1.setSize(mainPanel.getWidth() - 30, 300);
				else
					scrollPane1.setSize(mainPanel.getWidth() - 30, 300);

				mainPanel.add(scrollPane1);
				break;
			case 3:
				table = new JTable(res, menu);
				table3 = table;
				table3.setEnabled(false);
				scrollPane2 = new JScrollPane(table);
				scrollPane2.setLocation(x, y + 10);
				table.setRowHeight(rowHeight);
				if (lineCount <= 7)
					scrollPane2.setSize(mainPanel.getWidth() - 30, 300);
				else
					scrollPane2.setSize(mainPanel.getWidth() - 30, 300);

				mainPanel.add(scrollPane2);
				break;
			case 4:
				table = new JTable(res, menu);
				table4 = table;
				table4.setEnabled(false);
				scrollPane3 = new JScrollPane(table);
				scrollPane3.setLocation(x, y + 10);
				table.setRowHeight(rowHeight);
				if (lineCount <= 7)
					scrollPane3.setSize(mainPanel.getWidth() - 30, 300);
				else
					scrollPane3.setSize(mainPanel.getWidth() - 30, 300);

				mainPanel.add(scrollPane3);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}