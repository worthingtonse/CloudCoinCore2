import java.sql.*;

/**
 * Writes data to an SQLite Database so the file system 
 * does not have to be used. This is an alternative for future use and is not used yet. 
 * 
 * @author Sean H. Worthington
 * @version 1/6/2017
 */
public class Database
{
    // instance variables - replace the example below with your own
    private static final String DATABASE_NAME = "CLOUDCOIN_DATABASE.db";
    private static final String CLOUDCOIN_TABLE = "CLOUDCOIN_TABLE";
    private static final int DATABASE_VERSION = 200;
    //private final Context mCtx;
 //   public static String TAG = Database.class.getSimpleName();

   // private DatabaseHelper mDbHelper;
  //  SQLiteDatabase mDb;

    public static final String KEY_ROWID = "sn";
    public static final String NN = "nn";
    public static final String AN0 = "an0";
    public static final String AN1 = "an1";
    public static final String AN2 = "an2";
    public static final String AN3 = "an3";
    public static final String AN4 = "an4";
    public static final String AN5 = "an5";
    public static final String AN6 = "an6";
    public static final String AN7 = "an7";
    public static final String AN8 = "an8";
    public static final String AN9 = "an9";
    public static final String AN10 = "an10";
    public static final String AN11 = "an11";
    public static final String AN12 = "an12";
    public static final String AN13 = "an13";
    public static final String AN14 = "an14";
    public static final String AN15 = "an15";
    public static final String AN16 = "an16";
    public static final String AN17 = "an17";
    public static final String AN18 = "an18";
    public static final String AN19 = "an19";
    public static final String AN20 = "an20";
    public static final String AN21 = "an21";
    public static final String AN22 = "an22";
    public static final String AN23 = "an23";
    public static final String AN24 = "an24";

    public static final String PAN0 = "pan0";
    public static final String PAN1 = "pan1";
    public static final String PAN2 = "pan2";
    public static final String PAN3 = "pan3";
    public static final String PAN4 = "pan4";
    public static final String PAN5 = "pan5";
    public static final String PAN6 = "pan6";
    public static final String PAN7 = "pan7";
    public static final String PAN8 = "pan8";
    public static final String PAN9 = "pan9";
    public static final String PAN10 = "pan10";
    public static final String PAN11 = "pan11";
    public static final String PAN12 = "pan12";
    public static final String PAN13 = "pan13";
    public static final String PAN14 = "pan14";
    public static final String PAN15 = "pan15";
    public static final String PAN16 = "pan16";
    public static final String PAN17 = "pan17";
    public static final String PAN18 = "pan18";
    public static final String PAN19 = "pan19";
    public static final String PAN20 = "pan20";
    public static final String PAN21 = "pan21";
    public static final String PAN22 = "pan22";
    public static final String PAN23 = "pan23";
    public static final String PAN24 = "pan24";

    public static final String S0 = "s0";//pass, fail, error or notdetected
    public static final String S1 = "s1";
    public static final String S2 = "s2";
    public static final String S3 = "s3";
    public static final String S4 = "s4";
    public static final String S5 = "s5";
    public static final String S6 = "s6";
    public static final String S7 = "s7";
    public static final String S8 = "s8";
    public static final String S9 = "s9";
    public static final String S10 = "s10";
    public static final String S12 = "s12";
    public static final String S13 = "s13";
    public static final String S14 = "s14";
    public static final String S15 = "s15";
    public static final String S16 = "s16";
    public static final String S17 = "s17";
    public static final String S18 = "s18";
    public static final String S19 = "s19";
    public static final String S20 = "s20";
    public static final String S21 = "s21";
    public static final String S22 = "s22";
    public static final String S23 = "s23";
    public static final String S24 = "s24";

    public static final String STATUS ="status"; //suspect,bank, counterfeit, lost, fracked
    public static final String INCOMMING_FILE = "incomming_file";
    public static final String NOTE = "note";
    public static final String HP = "hp";
    public static final String ED = "ed";
    public static final String ED_HEX = "ed_hex";

    public static final String[] CLOUDCOIN_FIELDS = new String[] {
            KEY_ROWID,
            NN,
            AN0,
            AN1,
            AN2,
            AN3,
            AN4,
            AN5,
            AN6,
            AN7,
            AN8,
            AN9,
            AN10,
            AN11,
            AN12,
            AN13,
            AN14,
            AN15,
            AN16,
            AN17,
            AN18,
            AN19,
            AN20,
            AN21,
            AN22,
            AN23,
            AN24,
            PAN0,
            PAN1,
            PAN2,
            PAN3,
            PAN4,
            PAN5,
            PAN6,
            PAN7,
            PAN8,
            PAN9,
            PAN10,
            PAN11,
            PAN12,
            PAN13,
            PAN14,
            PAN15,
            PAN16,
            PAN17,
            PAN18,
            PAN19,
            PAN20,
            PAN21,
            PAN22,
            PAN23,
            PAN24,
            S0,
            S1,
            S2,
            S3,
            S4,
            S5,
            S6,
            S7,
            S8,
            S9,
            S10,
            S11,
            S12,
            S13,
            S14,
            S15,
            S16,
            S17,
            S18,
            S19,
            S20,
            S21,
            S22,
            S23,
            S24,
            INCOMMING_FILE,
            NOTE,
            HP,
            ED,
            ED_HEX

        };

    private static final String CREATE_TABLE_CLOUDCOIN =
        "create table " + CLOUDCOIN_TABLE + "("
        + _ID + " INTEGER PRIMARY KEY "
        + NN + " INTEGER,"
        + AN0 + " text,"
        + AN1+ " text,"
        + AN2+ " text,"
        + AN3+ " text,"
        + AN4+ " text,"
        + AN5+ " text,"
        + AN6+ " text,"
        + AN7 + " text,"
        + AN8 + " text,"
        + AN9 + " text,"
        + AN10 + " text,"
        + AN11 + " text,"
        + AN12 + " text,"
        + AN13 + " text,"
        + AN14 + " text,"
        + AN15 + " text,"
        + AN16 + " text,"
        + AN17 + " text,"
        + AN18 + " text,"
        + AN19 + " text,"
        + AN20 + " text,"
        + AN21 + " text,"
        + AN22 + " text,"
        + AN23 + " text,"
        + AN24 + " text,"
        + PAN0 + " text,"
        + PAN1 + " text,"
        + PAN2 + " text,"
        + PAN3 + " text,"
        + PAN4 + " text,"
        + PAN5 + " text,"
        + PAN6 + " text,"
        + PAN7 + " text,"
        + PAN8 + " text,"
        + PAN9 + " text,"
        + PAN10 + " text,"
        + PAN11 + " text,"
        + PAN12 + " text,"
        + PAN13 + " text,"
        + PAN14 + " text,"
        + PAN15 + " text,"
        + PAN16 + " text,"
        + PAN17 + " text,"
        + PAN18 + " text,"
        + PAN19 + " text,"
        + PAN20 + " text,"
        + PAN21 + " text,"
        + PAN22 + " text,"
        + PAN23 + " text,"
        + PAN24 + " text,"
        + S0 + " text,"
        + S1 + " text,"
        + S2 + " text,"
        + S3 + " text,"
        + S4 + " text,"
        + S5 + " text,"
        + S6 + " text,"
        + S7 + " text,"
        + S8 + " text,"
        + S9 + " text,"
        + S10 + " text,"
        + S11 + " text,"
        + S12 + " text,"
        + S13 + " text,"
        + S14 + " text,"
        + S15 + " text,"
        + S16 + " text,"
        + S17 + " text,"
        + S18 + " text,"
        + S19 + " text,"
        + S20 + " text,"
        + S21 + " text,"
        + S22 + " text,"
        + S23 + " text,"
        + S24 + " text,"
        + INCOMMING_FILE + " text,"
        + NOTE + " text,"
        + HP + " INTEGER,"
        + ED + " text,"
        + ED_HEX + " text"
        +");";
/*
    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }//end constructor

        @Override
        public void onCreate(SQLiteDatabase db){

            db.execSQL(CREATE_TABLE_CLOUDCOIN);
        }//end onCreate
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            //do nothinga

        }//end on upgrade

    }//end inner class DatabaseHelper
    */
    
   
   
    /**
     * Constructor for objects of class CloudCoinDbAdapter
     */
    public Database( )
    {
        // initialise instance variables
        //this.mCtx = ctx;
        
    }

    public void open() throws SQLException {
 Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
  
    }//end open

    public void close(){
        if(mDbHelper !=null){
            mDbHelper.close();
        }//end if not null

    }//end close

    public long insertCloudCoin(CloudCoin cc){
       // return mDb.insertWithOnConflict (CLOUDCOIN_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);

    }//end inser cloudcoin

    public boolean updateCloudCoinStatus(int id, String  newStatus){
       // String[] selectionArgs = {String.valueOf(id)};
       // return mDb.update(CLOUDCOIN_TABLE, newValues, KEY_ROWID + "=?", selectionArgs)> 0;
    }//end update

    public boolean deleteCloudCoin(int id){
       // String[] selectionArgs = {String.valueOf(id)};
       // return mDb.update( CLOUDCOIN_TABLE, newValues, KEY_ROWID + "=?", selectionArgs)> 0;
    }//end update

    public Cursor getCloudCoins( boolean all, String status){
        if( all ){
            //return mDb.query(CLOUDCOIN_TABLE, CLOUDCOIN_FIELDS, null, null, null, null, null);
        }else{
            //return mDb.query(CLOUDCOIN_TABLE, CLOUDCOIN_FIELDS, STATUS + "=" + status, null, null, null, null);
        }//end if all

    }//end get CloudCOIN 
    public static CloudCoin getCloudCoinFromCursor( Cursor cursor){
        CloudCoin cc = new CloudCoin();
        cc.sn = cursor.getInt( cursor.getColumnIndex(KEY_ROWID));
        cc.nn = cursor.getInt( cursor.getColumnIndex(NN));
        cc.ans[0] =cursor.getString( cursor.getColumnIndex(AN0));
        cc.ans[1] =cursor.getString( cursor.getColumnIndex(AN1));
        cc.ans[2] =cursor.getString( cursor.getColumnIndex( AN2));
        cc.ans[3] =cursor.getString( cursor.getColumnIndex(AN3));
        cc.ans[4] =cursor.getString( cursor.getColumnIndex( AN4));
        cc.ans[5] =cursor.getString( cursor.getColumnIndex( AN5));
        cc.ans[6] =cursor.getString( cursor.getColumnIndex( AN6));
        cc.ans[7] =cursor.getString( cursor.getColumnIndex(AN7));
        cc.ans[8] =cursor.getString( cursor.getColumnIndex(AN8));
        cc.ans[9] =cursor.getString( cursor.getColumnIndex(AN9));
        cc.ans[10] =cursor.getString( cursor.getColumnIndex( AN10));
        cc.ans[11] =cursor.getString( cursor.getColumnIndex( AN11));
        cc.ans[12] =cursor.getString( cursor.getColumnIndex( AN12));
        cc.ans[13] =cursor.getString( cursor.getColumnIndex( AN13));
        cc.ans[14] =cursor.getString( cursor.getColumnIndex( AN14));
        cc.ans[15] =cursor.getString( cursor.getColumnIndex( AN15));
        cc.ans[16] =cursor.getString( cursor.getColumnIndex( AN16));
        cc.ans[17] =cursor.getString( cursor.getColumnIndex( AN17));
        cc.ans[18] =cursor.getString( cursor.getColumnIndex( AN18));
        cc.ans[19] =cursor.getString( cursor.getColumnIndex( AN19));
        cc.ans[20] =cursor.getString( cursor.getColumnIndex( AN20));
        cc.ans[21] =cursor.getString( cursor.getColumnIndex( AN21));
        cc.ans[22] =cursor.getString( cursor.getColumnIndex( AN22));
        cc.ans[23] =cursor.getString( cursor.getColumnIndex( AN23));
        cc.ans[24] =cursor.getString( cursor.getColumnIndex( AN24));

        cc.pans[0] =cursor.getString( cursor.getColumnIndex( PAN0));
        cc.pans[1] =cursor.getString( cursor.getColumnIndex( PAN1));
        cc.pans[2] =cursor.getString( cursor.getColumnIndex( PAN2));
        cc.pans[3] =cursor.getString( cursor.getColumnIndex( PAN3));
        cc.pans[4] =cursor.getString( cursor.getColumnIndex( PAN4));
        cc.pans[5] =cursor.getString( cursor.getColumnIndex( PAN5));
        cc.pans[6] =cursor.getString( cursor.getColumnIndex( PAN6));
        cc.pans[7] =cursor.getString( cursor.getColumnIndex( PAN7));
        cc.pans[8] =cursor.getString( cursor.getColumnIndex( PAN8));
        cc.pans[9] =cursor.getString( cursor.getColumnIndex( PAN9));
        cc.pans[10] =cursor.getString( cursor.getColumnIndex( PAN10));
        cc.pans[11] =cursor.getString( cursor.getColumnIndex( PAN11));
        cc.pans[12] =cursor.getString( cursor.getColumnIndex( PAN12));
        cc.pans[13] =cursor.getString( cursor.getColumnIndex( PAN13));
        cc.pans[14] =cursor.getString( cursor.getColumnIndex( PAN14));
        cc.pans[15] =cursor.getString( cursor.getColumnIndex( PAN15));
        cc.pans[16] =cursor.getString( cursor.getColumnIndex( PAN16));
        cc.pans[17] =cursor.getString( cursor.getColumnIndex( PAN17));
        cc.pans[18] =cursor.getString( cursor.getColumnIndex( PAN18));
        cc.pans[19] =cursor.getString( cursor.getColumnIndex( PAN19));
        cc.pans[20] =cursor.getString( cursor.getColumnIndex( PAN20));
        cc.pans[21] =cursor.getString( cursor.getColumnIndex( PAN21));
        cc.pans[22] =cursor.getString( cursor.getColumnIndex( PAN22));
        cc.pans[23] =cursor.getString( cursor.getColumnIndex( PAN23));
        cc.pans[24] =cursor.getString( cursor.getColumnIndex( PAN24));

        cc.pastStatus[0] =cursor.getString( cursor.getColumnIndex( S0));
        cc.pastStatus[1] =cursor.getString( cursor.getColumnIndex( S1));
        cc.pastStatus[2] =cursor.getString( cursor.getColumnIndex( S2));
        cc.pastStatus[3] =cursor.getString( cursor.getColumnIndex( S3));
        cc.pastStatus[4] =cursor.getString( cursor.getColumnIndex( S4));
        cc.pastStatus[5] =cursor.getString( cursor.getColumnIndex( S5));
        cc.pastStatus[6] =cursor.getString( cursor.getColumnIndex( S6));
        cc.pastStatus[7] =cursor.getString( cursor.getColumnIndex( S7));
        cc.pastStatus[8] =cursor.getString( cursor.getColumnIndex( S8));
        cc.pastStatus[9] =cursor.getString( cursor.getColumnIndex( S9));
        cc.pastStatus[10] =cursor.getString( cursor.getColumnIndex( S10));
        cc.pastStatus[11] =cursor.getString( cursor.getColumnIndex( S11));
        cc.pastStatus[12] =cursor.getString( cursor.getColumnIndex( S12));
        cc.pastStatus[13] =cursor.getString( cursor.getColumnIndex( S13));
        cc.pastStatus[14] =cursor.getString( cursor.getColumnIndex( S14));
        cc.pastStatus[15] =cursor.getString( cursor.getColumnIndex( S15));
        cc.pastStatus[16] =cursor.getString( cursor.getColumnIndex( S16));
        cc.pastStatus[17] =cursor.getString( cursor.getColumnIndex( S17));
        cc.pastStatus[18] =cursor.getString( cursor.getColumnIndex( S18));
        cc.pastStatus[19] =cursor.getString( cursor.getColumnIndex( S19));
        cc.pastStatus[20] =cursor.getString( cursor.getColumnIndex( S20));
        cc.pastStatus[21] =cursor.getString( cursor.getColumnIndex( S21));
        cc.pastStatus[22] =cursor.getString( cursor.getColumnIndex( S22));
        cc.pastStatus[23] =cursor.getString( cursor.getColumnIndex( S23));
        cc.pastStatus[24] =cursor.getString( cursor.getColumnIndex( S24));

        cc.fileName =cursor.getString( cursor.getColumnIndex( INCOMMING_FILE));
        cc.aoid =cursor.getString( cursor.getColumnIndex( NOTE));
        cc.hp =cursor.getInt( cursor.getColumnIndex( HP));
        cc.ed =cursor.getString( cursor.getColumnIndex( ED));
        cc.ed_hex =cursor.getString( cursor.getColumnIndex( ED_HEX));
        return(cc);

    }//end get cc from cursor
}
