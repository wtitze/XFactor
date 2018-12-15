import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
    
public class Main {
    public static void main (String[] args) {
        String message = "";
        // stringa per connettersi al database
        String connURL = "jdbc:sqlserver://213.140.22.237\\SQLEXPRESS:1433;databaseName=XFactor;user=titze.walter;password=galvani@2018";
        // abbreviata
        // String connURL = "jdbc:sqlserver://213.140.22.237; databaseName=###;user=###;password=###";
        try { // Load SQL Server JDBC driver and establish connection.
            Connection connection = DriverManager.getConnection(connURL); 
            message += "connection OK\n";
            System.out.println("inserisci l'operazione che intendi effettuare");
            System.out.println("1: visualizzazione dei giudici");
            System.out.println("2: visualizzazione dei giudici nati in un certo luogo");   
            System.out.println("3: visualizzazione dei giudici nati in un certo luogo (con preparedStatement");   
            System.out.println("4: inserimento di un giudice"); 
            System.out.print("inserisci la scelta --> "); 
            Scanner scan = new Scanner(System.in);
            int scelta = scan.nextInt();
            switch (scelta) {
                case 1: 
                    // visualizzazione di tutti i giudici
                    // creazione di un oggetto per effettuare la richiesta al DB 
                    Statement stmt = connection.createStatement(); // istruzione SQL da eseguire
                    String sql = "SELECT * FROM Giudice";
                    // esecuzione dell’istruzione: il risultato viene inserito nell’oggetto rs
                    // per essere elaborato 
                    ResultSet rs = stmt.executeQuery(sql);
                    // ciclo per prendere tutte i record prelevati dalla tabella del database 
                    while(rs.next()){
                        String first = rs.getString("Nome");
                        String last = rs.getString("Cognome");
                        String alias = rs.getString("Soprannome");
                        message += first + " | " + last + " | " + alias + "\n";
                    }
                    rs.close();
                    System.out.println(message);
                break;
                case 2:
                    // visualizzazione dei giudici nati in una certa città
                    // utilizzando i prepared statement
                    System.out.print("Inserisci il luogo di nascita del giudice: ");
                    String luogo2 = scan.next();
                    // String sql2 = "SELECT * FROM Giudice where LuogoDiNascita = ?";
                    String sql2 = "SELECT * FROM Giudice where LuogoDiNascita = '" + luogo2 + "' ";
                    System.out.println(sql2);
                    Statement stmt2 = connection.createStatement(); 
                    // stmt2.setString(1, luogo);
                    ResultSet rs2 = stmt2.executeQuery(sql2);
                    while(rs2.next()){
                        String first = rs2.getString("Nome");
                        String last = rs2.getString("Cognome");
                        String alias = rs2.getString("Soprannome");
                        message += first + " | " + last + " | " + alias + "\n";
                    }
                    rs2.close();
                    System.out.println(message);
                break;
                case 3:
                    // visualizzazione con preparedStatement
                    // visualizzazione dei giudici nati in una certa città
                    // utilizzando i prepared statement
                    System.out.print("Inserisci il luogo di nascita del giudice: ");
                    String luogo3 = scan.next();
                    String sql3 = "SELECT * FROM Giudice where LuogoDiNascita = ?";
                    PreparedStatement stmt3 = connection.prepareStatement(sql3); 
                    stmt3.setString(1, luogo3);
                    ResultSet rs3 = stmt3.executeQuery();
                    while(rs3.next()){
                        String first = rs3.getString("Nome");
                        String last = rs3.getString("Cognome");
                        String alias = rs3.getString("Soprannome");
                        message += first + " | " + last + " | " + alias + "\n";
                    }
                    rs3.close();
                    System.out.println(message);
                break;
                case 4:
                    //inserimento di un nuovo giudice
                    System.out.print("Inserisci il nome del giudice: ");
                    String nome = scan.next();
                    System.out.print("Inserisci il cognome del giudice: ");
                    String cognome = scan.next();
                    // selezione dell'ultimo ID
                    Statement stmt4 = connection.createStatement();
                    String sqlId = "Select Max(ID) as MaxId from Giudice";
                    ResultSet rs4 = stmt4.executeQuery(sqlId);
                    rs4.next();
                    // calcolo nuovo ID
                    int newId = rs4.getInt("MaxId") + 1;
                    // inserimento del nuovo giudice
                    String sql4 = "INSERT INTO Giudice (ID, Nome, Cognome) VALUES (?, ?, ?)";
                    PreparedStatement prepStmt = connection.prepareStatement(sql4);
                    prepStmt.setInt(1, newId);
                    prepStmt.setString(2, nome);
                    prepStmt.setString(3, cognome);
                    prepStmt.executeUpdate();
                    System.out.println("giudice inserito");
                break;
                default:
                    System.out.println("opzione inesistente");
            }
            scan.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}