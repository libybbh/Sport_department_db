import java.sql.*;

public class DataBase {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/sport_department?useUnicode=true&characterEncoding=utf-8";
    private static final String user = "root";
    private static final String password = "root";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs=null;
    //private static String query;

    public DataBase() {
        try {
            this.con = DriverManager.getConnection(url, user, password);
            //stmt.executeUpdate(this.query);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }
    public void addTables(){
        String query = "";
        try {
            query = "CREATE TABLE IF NOT EXISTS bank ("+
                    "MFI_bank VARCHAR(6) NOT NULL,"+
                    "name_of_bank VARCHAR(150) NOT NULL,"+
                    "PRIMARY KEY (MFI_bank));";
            stmt.executeUpdate(query);


            query = "CREATE TABLE IF NOT EXISTS nomenOfDel ("+
                    "name_of_goods CHAR(70) NOT NULL,"+
                    "accountancy_account INT NOT NULL,"+
                    "order_number INT NOT NULL,"+
                    "unit CHAR(30) NOT NULL,"+
                    "PRIMARY KEY (name_of_goods));";
            stmt.executeUpdate(query);


            query = "CREATE TABLE IF NOT EXISTS providers ("+
                    "provider_id VARCHAR(12) NOT NULL ,"+
                    "name_of_provider VARCHAR(255) NOT NULL,"+
                    "city VARCHAR(255) NOT NULL,"+
                    "street VARCHAR(255) NOT NULL,"+
                    "built VARCHAR(255) NOT NULL,"+
                    "flat INT,"+
                     "phone VARCHAR(20) NOT NULL,"+
                    "PRIMARY KEY (provider_id));";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS phoneNum ("+
                    "phone_num VARCHAR(25) NOT NULL ,"+
                    "provider_id VARCHAR(12) NOT NULL,"+
                    "FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE CASCADE,"+
                    "PRIMARY KEY (phone_num));";
            stmt.executeUpdate(query);


            query = "CREATE TABLE IF NOT EXISTS regOfStor ("+
                    "id_of_storage INT NOT NULL AUTO_INCREMENT,"+
                    "responsible_person VARCHAR(50) NOT NULL,"+
                    "address VARCHAR(50) NOT NULL,"+
                    "PRIMARY KEY (id_of_storage),"+
                    "UNIQUE(address));";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS accounts ("+
                    "account_id VARCHAR(16) NOT NULL,"+
                    "MFI_bank VARCHAR(6) NOT NULL,"+
                    "PRIMARY KEY (account_id),"+
                    "CONSTRAINT FK_MFIBank FOREIGN KEY (MFI_bank) REFERENCES bank(MFI_bank) "+
                    "ON DELETE CASCADE ON UPDATE CASCADE );";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS bill ("+
                    "bill_id INT NOT NULL AUTO_INCREMENT,"+
                    "date_of_bill DATE NOT NULL,"+
                    "number_from_provider VARCHAR(12) NOT NULL,"+
                    "sum_of_bill real NOT NULL,"+
                    "provider_id VARCHAR(12) NOT NULL,"+
                    "account_id VARCHAR(16) NOT NULL,"+
                    "PRIMARY KEY (bill_id),"+
                    "FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE NO ACTION ON UPDATE CASCADE,"+
                    "FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE NO ACTION ON UPDATE CASCADE);";
            stmt.executeUpdate(query);


            query = "CREATE TABLE IF NOT EXISTS goodsOnStor(\n" +
                    "                    goods_on_storages_id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                    name_of_goods CHAR(50) NOT NULL,\n" +
                    "                    amount CHAR(50) NOT NULL,\n" +
                    "                    summ REAL NOT NULL,\n" +
                    "                    bill_id INT NOT NULL,\n" +
                    "                    id_of_storage INT NOT NULL,\n" +
                    "                    PRIMARY KEY (goods_on_storages_id),\n" +
                    "                    FOREIGN KEY (bill_id) REFERENCES bill(bill_id) ON DELETE NO ACTION ON UPDATE CASCADE,\n" +
                    "                    FOREIGN KEY (id_of_storage) REFERENCES regOfStor(id_of_storage) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                    "            );";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS billDet(\n" +
                    "  bill_det_id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  name_of_goods CHAR(70) NOT NULL,\n" +
                    "  price_per_unit REAL NOT NULL,\n" +
                    "  amount INT NOT NULL,\n" +
                    "  summ REAL NOT NULL,\n" +
                    "  VAT REAL,\n" +
                    "  sum_VAT REAL,\n" +
                    "  state CHAR(30),\n" +
                    "  bill_id INT NOT NULL,\n" +
                    "  PRIMARY KEY (bill_det_id),\n" +
                    "  FOREIGN KEY (name_of_goods) REFERENCES nomenOfDel(name_of_goods) ON DELETE NO ACTION ON UPDATE CASCADE,\n" +
                    "  FOREIGN KEY (bill_id) REFERENCES bill(bill_id) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                    ");";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS billDetGoods(\n" +
                    "  id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  bill_det_id INT NOT NULL,\n" +
                    "  goods_on_storages_id INT NOT NULL,\n" +
                    "  PRIMARY KEY (id),\n" +
                    "  FOREIGN KEY (bill_det_id) REFERENCES billDet(bill_det_id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "  FOREIGN KEY (goods_on_storages_id) REFERENCES goodsOnStor(goods_on_storages_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                    ");";
            stmt.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS contracts(\n" +
                    "  contracts_id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  provider_id VARCHAR(12) NOT NULL,\n" +
                    "  name_of_goods CHAR(70) NOT NULL,\n" +
                    "  date_from DATE NOT NULL,\n" +
                    "  date_to DATE NOT NULL,\n" +
                    "  PRIMARY KEY (contracts_id),\n" +
                    "  FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE NO ACTION ON UPDATE CASCADE,\n" +
                    "  FOREIGN KEY (name_of_goods) REFERENCES nomenOfDel(name_of_goods) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                    ");";
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String what, String from, String where) throws SQLException {//select from... query
        String query = "select "+what +" from "+ from+" where " +where +";";
        rs = stmt.executeQuery(query);
        return rs;
    }
    public ResultSet select(String what, String from) throws SQLException {//select from... query
        String query = "select "+what +" from "+ from+";";
        rs = stmt.executeQuery(query);
        return rs;
    }

    public ResultSet select(String query) throws SQLException {//select from... query
        rs = stmt.executeQuery(query);
        return rs;
    }

    public void add(String query) throws SQLException {
        //con.createStatement().executeQuery("set character set utf8_koi8");
        stmt.executeUpdate(query);
    }
    //вставка. Поки ніяких ідей для того, щоб загально прописати назви таблиць при вставці немає
    //лише світч кайс

    //видалення
    public void delete(String query) throws SQLException {
        stmt.executeUpdate(query);
    }

    public void close() throws SQLException { //not forget to close throws after using db
        try { con.close(); } catch(SQLException se) { /*can't do anything */ }
        try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        if(rs!=null)try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
    }

}
