package view;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexaoMySQL {

    private Connection conexao;
    private Statement st;
    public ResultSet rs;

    public String url;
    String driver;
    String usuario;
    String senha;

    public static int resultadoUpd = 99;

    public void conecta(String url, String driver, String usuario, String senha) {

        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
            st = conexao.createStatement();

            resultadoUpd = 0;

            st.executeUpdate("begin;");

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar o "
                    + "driver do banco de dados." + ex);
        } catch (SQLException sqlEx) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados. " + sqlEx);
        }
    }

    public void desconecta() {

        try {

            switch (resultadoUpd) {
                case 0:
                    st = conexao.createStatement();
                    st.executeUpdate("commit;");
                    break;
                default:
                    st = conexao.createStatement();
                    st.executeUpdate("rollback;");
            }

            conexao.close();

        } catch (SQLException sqlEx) {

            JOptionPane.showMessageDialog(null, "Não foi possível desconectar o banco " + sqlEx);

        }

    }

    public void executeAtualizacao(String sql) {

        try {
            st = conexao.createStatement();
            st.executeUpdate(sql);

        } catch (SQLException sqlEx) {
            resultadoUpd = 1;
            JOptionPane.showMessageDialog(null, "Não foi possível executar o comando sql" + sql + ".Erro " + sqlEx + " upd " + resultadoUpd);

        }

    }

    public void executeConsulta(String sql) {

        try {
            st = conexao.createStatement();

            rs = st.executeQuery(sql);

        } catch (SQLException sqlEx) {

            JOptionPane.showMessageDialog(null, "Não foi possível executar o comando sql" + sql + "Erro " + sqlEx);

        }

    }
}
