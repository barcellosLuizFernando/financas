
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import view.TelaInicial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ferna
 */
public class Main extends javax.swing.JFrame {

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException exc) {
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace();
        } catch (InstantiationException exc) {
            exc.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        }
        // restante do método main aqui...

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaInicial().setVisible(true);

            }
        });
    }
}

//    private static void ExMySql() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//        };

