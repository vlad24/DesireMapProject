import java.sql.*;
public class Test {
	
        public static void jExec() {
                try{
                        System.out.println("---------Test program on");
                        testDataBase();
                        testBaseTableCreation();
                        testBaseAdding();
                        testBaseSelecting();
                        System.out.println("---------Tests are passed\n");
                }
                catch(Exception fail){
                        System.out.println(fail.getMessage());
                }
        }

        public static void testDataBase() throws Exception{
                DataBaseSQLite base = new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
                try {
                        base.connectToBase();
                        if (!base.turnedOn){
                                throw new Exception("DB is not turned on!\n");
                        }
                }
                catch(Exception error){
                        throw new Exception("Problems with db connection\n");
                }
                finally{
                        base.disconnect();
                }
        }

        public static void testBaseTableCreation() throws Exception{
                DataBaseSQLite base = new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
                base.connectToBase();
                Statement creator = base.getConnection().createStatement();
                creator.execute("DROP TABLE IF EXISTS PRICES");
                creator.execute("CREATE TABLE IF NOT EXISTS PRICES(COMPANY TEXT, MODEL TEXT PRIMARY KEY, COST TEXT)");
                creator.close();
                base.disconnect();
        }

        public static void testBaseAdding() throws Exception{
                DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
                base.connectToBase();
                String company = "opel";
                String model = "astra";
                String price = "500000";
                String company2 = "renault";
                String model2 = "logan";
                String price2 = "300000";
                Statement adder = base.getConnection().createStatement();
                adder.execute("INSERT INTO PRICES VALUES('" + company + "', '" + model + "', '" + price + "')");
                adder.execute("INSERT INTO PRICES VALUES('" + company2 + "', '" + model2 + "', '" + price2 + "')");
                base.disconnect();
        }

        public static void testBaseSelecting() throws Exception{
                DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
                base.connectToBase();
                Statement selector  = base.getConnection().createStatement();
                ResultSet result = selector.executeQuery("SELECT * FROM PRICES");
                int count = 0;
                while(result.next()){
                        count++;
                        System.out.print(result.getString("company") + " ");
                        System.out.print(result.getString("model") + " ");
                        System.out.print(result.getString("cost") + "\n");
                }
                System.out.println(count);
        }
        
        public static void testCoordinatesWorking() throws Exception{
            DataBaseSQLite base= new DataBaseSQLite("D://JavaProgramming/DesireMap/DMServer/testCARS.db");
            base.connectToBase();
            Statement selector  = base.getConnection().createStatement();
            ResultSet result = selector.executeQuery("SELECT * FROM PRICES");
            int count = 0;
            while(result.next()){
                    count++;
                    System.out.print(result.getString("company") + " ");
                    System.out.print(result.getString("model") + " ");
                    System.out.print(result.getString("cost") + "\n");
            }
            System.out.println(count);
        }
}