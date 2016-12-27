import com.datastax.driver.core.{Row, Cluster}
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.DataTypeCqlNameParser;
import com.datastax.driver.core
import com.datastax.driver.extras.codecs.jdk8.InstantCodec
;

/**
 * Created by marutsingh on 12/26/16.
 */


object CassandraSample extends App {
  var cluster: Cluster = null;

  val createTableKey = "create table IF NOT EXISTS godam.item_status (\n  serial_id text," +
    "\n  insertion_time timestamp, status_map map<timestamp, text>," +
    "\n  event blob,\n  PRIMARY KEY (serial_id, insertion_time)\n)" +
    "\nWITH CLUSTERING ORDER BY (insertion_time DESC);"

  try {
    cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
    cluster.getConfiguration().getCodecRegistry()
      .register(InstantCodec.instance);
    val session: com.datastax.driver.core.Session = cluster.connect();

    session.execute(createTableKey);
    val time = java.time.Instant.now().formatted("YYYY-MM-DD HH:mm:ss")
    session.execute("INSERT INTO godam.item_status (serial_id,insertion_time,status_map) VALUES ('1','2014-10-2 12:00'," +
      "{'2014-10-2 12:00' : 'throw ring into mount doom'})")
    val rs = session.execute("SELECT * FROM godam.item_status LIMIT 10")
    val row: Row  = rs.one();
    val map = row.getMap("status_map", Class.forName("java.util.Date"), Class.forName("java.lang.String"));

  System.out.println(row.getString("release_version"));                          // (4)
  }catch{
    case e: Exception =>
      e.printStackTrace()
  }
   finally{
    if (cluster != null) cluster.close();                                          // (5)
  }
}
