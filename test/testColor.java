
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.Property;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.parameters.base.Brand;
import org.xersys.parameters.base.Color;
import org.xersys.parameters.base.Labor;

public class testColor {
    public static void main(String [] args){
        final String PRODUCTID = "Daedalus";
        
        //get database property
        Property loConfig = new Property("db-config.properties", PRODUCTID);
        if (!loConfig.loadConfig()){
            System.err.println(loConfig.getMessage());
            System.exit(1);
        } else System.out.println("Database configuration was successfully loaded.");
        
        //connect to database
        SQLConnection loConn = new SQLConnection();
        loConn.setProperty(loConfig);
        if (loConn.getConnection() == null){
            System.err.println(loConn.getMessage());
            System.exit(1);
        } else
            System.out.println("Connection was successfully initialized.");        
        
        //load application driver
        Nautilus loNautilus = new Nautilus();
        
        loNautilus.setConnection(loConn);
        loNautilus.setEncryption(CryptFactory.make(CryptFactory.CrypType.AESCrypt));
        
        loNautilus.setUserID("000100210001");
        if (!loNautilus.load(PRODUCTID)){
            System.err.println(loNautilus.getMessage());
            System.exit(1);
        } else
            System.out.println("Application driver successfully initialized.");
        
        LRecordMas _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                System.out.println(fsFieldNm + "-->" + foValue);
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                System.out.println(fnIndex + "-->" + foValue);
            }
        };
        
        Color instance = new Color(loNautilus, (String) loNautilus.getBranchConfig("sBranchCd"), false);
        instance.setListener(_listener);
        
        if (instance.NewRecord()) {
            instance.setMaster("sColorNme", "Black and Yello");
            instance.setMaster("sColorCde", "MAC");
            
            if (instance.SaveRecord()){
                System.out.println("Record saved successfully.");
            } else {
                System.err.println(instance.getMessage());
            }
        }
    }
}
