package Classess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBwork {

    final static String url = "jdbc:oracle:thin:@localhost:1521:XE";
    final static String driver = "oracle.jdbc.OracleDriver";
    final static String username = "lib";
    final static String pass = "lib";

    PreparedStatement pst = null;//ana hatetha hena ahsan ashn a2dar astakhdemha f kza methods 
    ResultSet rs = null;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    //database connection 

    public static Connection conn() {
        try {
            //aftah el drivers
            Class.forName(driver);
            //aftah connection m3 el database
            Connection con = DriverManager.getConnection(url, username, pass);

            return con;
        } catch (ClassNotFoundException | SQLException e) {

            return null;
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //el function dih betkhaliny adakhal kaza primary key wara b3d b3d lama a3ml save t3bn ;
    public int getMaxPK(String columnname, String tablename) {
        String sql = "select Max(" + columnname + ") from " + tablename + " ";
        int max = 0;
        try {
            pst = conn().prepareStatement(sql);
            rs = pst.executeQuery();//3shn a3ml save lel result elly b3melaha insert fel database 
            if (rs.next()) {
                max = rs.getInt(1);
            }
            return max + 1;//max + 1 3shn el pK mayenfa3sh yetkarar
        } catch (Exception e) {
            return 0;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //3shn a3mel add lel values fel textbox
    public int add(String tablename, String[] values, byte[] personimage) {
        String insert = " insert into " + tablename + " values(";
        for (int i = 0; i < values.length; i++) {
            insert += "?,";
        }
        insert = insert.substring(0, insert.length() - 1) + " )";
        try {
            pst = conn().prepareStatement(insert);
            for (int i = 1; i < values.length; i++) {
                pst.setString(i, values[i - 1]);
            }
            pst.setBytes(values.length, personimage);
            pst.executeUpdate();
            return -1;
        } catch (Exception e) {
            return 0;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////    
    public ResultSet select(String tablename,String[] ChoosenFields,String[] whereFields,String[] values,String operator,String separator){
        //Select [ChoosenFields] From [tablename] Where [whereFields==values] el == dah el operator f mmkn yetghayar ;
        //choosenFields elly howa el informations beta3et el table [member]
        //zay masalan --> mem_id,mem_phone...etc
        //whereFields ely howa enta lama betcall fel database : Select mem_id From member[tablename] Where mem_id=123 ;
        //el where Fields hiya el queries elly fel akher ;
        String sql = "select ";
        for(int i=0;i<ChoosenFields.length;i++){
            sql+=ChoosenFields[i]+", ";
        }
        sql.substring(0, sql.length()-1);
        
        sql = " From "+tablename+" Where";
        
        String select = functionwhere(sql, whereFields, operator, separator);
        
        try{
            pst=conn().prepareStatement(select);
            for(int i =1;i<=values.length;i++){
                pst.setString(i, values[i-1]);
            }
            return pst.executeQuery();
        }catch(Exception e){
        return null;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    //functionwhere ashn law e7tagt a3mel delete aw update l7aga f ha7taga eni akteb masalan
    public String functionwhere(String sql ,String[] whereFields1,String operator1,String separator1){
        String sql1 = sql;
        //hakawenn gomlet el Where
        for(int i =0 ;i<whereFields1.length;i++){
            sql1 += " "+whereFields1[i]+" "+operator1+"? "+separator1;
        }
        sql1=sql1.substring(0, sql1.length()-separator1.length());
        return sql1;
        
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //function el delete 
//    public int delete(String tablename , Strin[] whereFields2 ,String[] wherevalues, String  sep){
//        
//    }
}
