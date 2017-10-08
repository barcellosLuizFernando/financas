/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luiz.barcellos
 */
public class CadFluxo extends javax.swing.JInternalFrame {

    /**
     * Creates new form cadPessoas
     */
    public CadFluxo() {
        initComponents();
    }

    int var_consulta;

    ConexaoMySQL cn = new ConexaoMySQL();
    String url;
    String driver = "com.mysql.jdbc.Driver";
    String usuario;
    String senha;

    public void setPosicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
    }

    public void DadosConexao() {
        try (FileReader arq = new FileReader("conexao.txt")) {
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            int line = 1;
            while (linha != null) {
                switch (line) {
                    case 1:
                        url = "jdbc:mysql://" + linha;
                        break;
                    case 2:
                        usuario = linha;
                        break;
                    case 3:
                        senha = linha;
                        break;
                    default:
                        break;
                }
                line++;
                linha = lerArq.readLine();
            }
            arq.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro na abertura do arquivo: " + e);

        }
    }

    public void HabilitarEdicao() {
        jBtnGravar.setEnabled(true);
        jBtnPesquisar.setEnabled(false);

        jRbEntrada.setEnabled(true);
        jRbSaida.setEnabled(true);
        jRbTransferencia.setEnabled(true);

        jTxtNome.setEnabled(true);

    }

    public void DesabilitarEdicao() {
        jBtnGravar.setEnabled(false);
        jBtnPesquisar.setEnabled(true);
        jBtnEditar.setEnabled(false);
        jBtnIncluir.setEnabled(true);

        jRbEntrada.setEnabled(false);
        jRbSaida.setEnabled(false);
        jRbTransferencia.setEnabled(false);

        jTxtNome.setEnabled(false);
        jTxtId.setEnabled(true);

        jBtnIncluir.setText("Incluir");
        jBtnEditar.setText("Editar");

    }

    public void LimpaCampos() {
        jTxtNome.setText("");
        jTxtId.setText("");

    }

    public void MontaLista() {
        DefaultTableModel lista = (DefaultTableModel) jTblConsulta_Multi.getModel();
        lista.setColumnCount(0); //LIMPA COLUNAS
        lista.setRowCount(0); // LIMPA LINHAS
        //jTxtPesquisa_Multi.setText("");

        switch (var_consulta) {
            case 1:
                //CONSULTA TIPOS FLUXO

                lista.addColumn("Id");
                lista.addColumn("Descrição");

                jTblConsulta_Multi.getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getColumnModel().getColumn(0).setMinWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMinWidth(50);

                String sql1; //INSTANCIA A VARIÁVEL. SERÁ INICIALIZADA NO IF

                if ("".equals(jTxtPesquisa_Multi.getText())) {
                    sql1 = "SELECT * FROM fluxo;";
                } else {
                    sql1 = "SELECT * FROM fluxo "
                            + "WHERE UPPER(descricao) "
                            + "LIKE '%" + jTxtPesquisa_Multi.getText().toUpperCase() + "%' "
                            + "ORDER BY descricao;";
                }

                DadosConexao();
                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql1);

                 {
                    try {
                        while (cn.rs.next()) {

                            lista.addRow(new String[]{
                                cn.rs.getString(1),
                                cn.rs.getString(2)});
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CadFluxo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                cn.desconecta();

                break;
        }
    }

    public void IncluiPesquisa() {

        int linha = jTblConsulta_Multi.getSelectedRow();

        switch (var_consulta) {
            case 1:
                //INCLUI ID BANCO
                jTxtId.setText(jTblConsulta_Multi.getValueAt(linha, 0).toString());

                var_consulta = 11;

                IncluiPesquisa();

                break;

            case 11:
                //INCLUI DADOS FINANCIAMENTO
                String id = jTxtId.getText();
                int natureza;

                String sql11 = "SELECT * FROM fluxo "
                        + "WHERE id = '" + id + "';";

                DadosConexao();
                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql11);

                try {
                    while (cn.rs.next()) {

                        jTxtNome.setText(cn.rs.getString(2));
                        natureza = cn.rs.getInt(3);
                        
                        switch (natureza){
                            case 1:
                                jRbEntrada.setSelected(true);
                                break;
                            case 2:
                                jRbSaida.setSelected(true);
                                break;
                            default:
                                jRbTransferencia.setSelected(true);
                                break;
                        }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CadFluxo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                

                if (!"".equals(jTxtNome.getText())) {

                    jBtnEditar.setEnabled(true);
                } else {
                    LimpaCampos();
                }

                break;

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPesquisar = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jTxtPesquisa_Multi = new javax.swing.JTextField();
        jBtnConfirmar = new javax.swing.JButton();
        jBtnCancelar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTblConsulta_Multi = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPnlPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtId = new javax.swing.JTextField();
        jTxtNome = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jBtnPesquisar = new javax.swing.JButton();
        jRbEntrada = new javax.swing.JRadioButton();
        jRbSaida = new javax.swing.JRadioButton();
        jRbTransferencia = new javax.swing.JRadioButton();
        jPnlComandos = new javax.swing.JPanel();
        jBtnIncluir = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnGravar = new javax.swing.JButton();

        jPesquisar.setTitle("Pesquisar");
        jPesquisar.setMinimumSize(new java.awt.Dimension(700, 400));

        jTxtPesquisa_Multi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtPesquisa_Multi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtPesquisa_MultiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtPesquisa_MultiKeyReleased(evt);
            }
        });

        jBtnConfirmar.setText("Adicionar");
        jBtnConfirmar.setEnabled(false);
        jBtnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnConfirmarActionPerformed(evt);
            }
        });

        jBtnCancelar.setText("Cancelar");
        jBtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarActionPerformed(evt);
            }
        });

        jTblConsulta_Multi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTblConsulta_Multi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTblConsulta_Multi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblConsulta_MultiMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTblConsulta_Multi);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 453, Short.MAX_VALUE)
                        .addComponent(jBtnCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnConfirmar))
                    .addComponent(jTxtPesquisa_Multi)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTxtPesquisa_Multi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnConfirmar)
                    .addComponent(jBtnCancelar))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPesquisarLayout = new javax.swing.GroupLayout(jPesquisar.getContentPane());
        jPesquisar.getContentPane().setLayout(jPesquisarLayout);
        jPesquisarLayout.setHorizontalGroup(
            jPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPesquisarLayout.setVerticalGroup(
            jPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setClosable(true);
        setTitle("Fluxo de Transações");

        jLabel1.setText("Código");

        jTxtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtIdKeyReleased(evt);
            }
        });

        jTxtNome.setEnabled(false);
        jTxtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtNomeActionPerformed(evt);
            }
        });

        jLabel8.setText("Descrição");

        jBtnPesquisar.setText("...");
        jBtnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPesquisarActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRbEntrada);
        jRbEntrada.setText("Entrada");
        jRbEntrada.setEnabled(false);

        buttonGroup1.add(jRbSaida);
        jRbSaida.setText("Saída");
        jRbSaida.setEnabled(false);

        buttonGroup1.add(jRbTransferencia);
        jRbTransferencia.setText("Transferência");
        jRbTransferencia.setEnabled(false);

        javax.swing.GroupLayout jPnlPrincipalLayout = new javax.swing.GroupLayout(jPnlPrincipal);
        jPnlPrincipal.setLayout(jPnlPrincipalLayout);
        jPnlPrincipalLayout.setHorizontalGroup(
            jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addComponent(jRbEntrada)
                        .addGap(18, 18, 18)
                        .addComponent(jRbSaida)
                        .addGap(18, 18, 18)
                        .addComponent(jRbTransferencia))
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel8))
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnPesquisar))
                            .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPnlPrincipalLayout.setVerticalGroup(
            jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRbEntrada)
                    .addComponent(jRbSaida)
                    .addComponent(jRbTransferencia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBtnIncluir.setMnemonic('I');
        jBtnIncluir.setText("Incluir");
        jBtnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnIncluirActionPerformed(evt);
            }
        });

        jBtnEditar.setText("Editar");
        jBtnEditar.setEnabled(false);
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });

        jBtnGravar.setText("Gravar");
        jBtnGravar.setEnabled(false);
        jBtnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGravarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPnlComandosLayout = new javax.swing.GroupLayout(jPnlComandos);
        jPnlComandos.setLayout(jPnlComandosLayout);
        jPnlComandosLayout.setHorizontalGroup(
            jPnlComandosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlComandosLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPnlComandosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnGravar, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(jBtnIncluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPnlComandosLayout.setVerticalGroup(
            jPnlComandosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlComandosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBtnIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnGravar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPnlComandos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPnlComandos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
            .addComponent(jPnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTxtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtNomeActionPerformed

    private void jBtnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnIncluirActionPerformed
        if ("Incluir".equals(jBtnIncluir.getText())) {

            HabilitarEdicao();

            jBtnIncluir.setText("Cancelar");
            jBtnIncluir.setMnemonic('C');
            jBtnEditar.setEnabled(false);

            jTxtId.setEnabled(false);
            jTxtId.setText("NOVO");

        } else {

            jBtnIncluir.setMnemonic('I');
            jTxtId.setText("");

            DesabilitarEdicao();
        }
    }//GEN-LAST:event_jBtnIncluirActionPerformed

    private void jBtnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPesquisarActionPerformed
        var_consulta = 1;

        jPesquisar.setVisible(true);

        MontaLista();
    }//GEN-LAST:event_jBtnPesquisarActionPerformed

    private void jTxtPesquisa_MultiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtPesquisa_MultiKeyReleased
        MontaLista();
        
        jTblConsulta_Multi.setRowSelectionInterval(0, 0);
    }//GEN-LAST:event_jTxtPesquisa_MultiKeyReleased

    private void jBtnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConfirmarActionPerformed
        IncluiPesquisa();
        jPesquisar.dispose();// TODO add your handling code here:
        jTxtPesquisa_Multi.setText("");
    }//GEN-LAST:event_jBtnConfirmarActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        jPesquisar.setVisible(false);        // TODO add your handling code here:
        jPesquisar.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jTblConsulta_MultiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblConsulta_MultiMouseClicked
        jBtnConfirmar.setEnabled(true);    // TODO add your handling code here:
    }//GEN-LAST:event_jTblConsulta_MultiMouseClicked

    private void jBtnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGravarActionPerformed
        String id = jTxtId.getText();
        String nome = jTxtNome.getText();
        String fluxo;

        if (jRbEntrada.isSelected()) {
            fluxo = "1";
        } else if (jRbSaida.isSelected()) {
            fluxo = "2";
        } else {
            fluxo = "3";
        }

        String sql;

        if (!"".equals(id)
                && !"".equals(nome)) {
            if ("Cancelar".equals(jBtnIncluir.getText())) {
                sql = "INSERT INTO fluxo (descricao,natureza) "
                        + "VALUES ('" + nome + "','" + fluxo + "');";
            } else {
                sql = "UPDATE fluxo SET descricao = '" + nome + "',"
                        + "natureza = '" + fluxo + "' "
                        + "WHERE id = '" + id + "';";
            }

            DadosConexao();
            cn.conecta(url, driver, usuario, senha);
            cn.executeAtualizacao(sql);
            cn.desconecta();

            DesabilitarEdicao();
            LimpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Algum campo não está preenchido!");
        }


    }//GEN-LAST:event_jBtnGravarActionPerformed

    private void jBtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditarActionPerformed
        if ("Editar".equals(jBtnEditar.getText())) {

            HabilitarEdicao();

            jBtnEditar.setText("Cancelar");
            jBtnEditar.setMnemonic('C');
            jBtnIncluir.setEnabled(false);

            jTxtId.setEnabled(false);

        } else {

            jBtnEditar.setMnemonic('E');

            DesabilitarEdicao();
        }
    }//GEN-LAST:event_jBtnEditarActionPerformed

    private void jTxtIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtIdKeyReleased
        if (!"".equals(jTxtId.getText())) {
            String id = jTxtId.getText();
            LimpaCampos();
            jTxtId.setText(id);            
            
            var_consulta = 11;

            IncluiPesquisa();

        } else {
            jBtnEditar.setEnabled(false);
            LimpaCampos();
        }
    }//GEN-LAST:event_jTxtIdKeyReleased

    private void jTxtPesquisa_MultiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtPesquisa_MultiKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            IncluiPesquisa();
            jPesquisar.dispose();
        }
    }//GEN-LAST:event_jTxtPesquisa_MultiKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JButton jBtnConfirmar;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnGravar;
    private javax.swing.JButton jBtnIncluir;
    private javax.swing.JButton jBtnPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JFrame jPesquisar;
    private javax.swing.JPanel jPnlComandos;
    private javax.swing.JPanel jPnlPrincipal;
    private javax.swing.JRadioButton jRbEntrada;
    private javax.swing.JRadioButton jRbSaida;
    private javax.swing.JRadioButton jRbTransferencia;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTblConsulta_Multi;
    private javax.swing.JTextField jTxtId;
    private javax.swing.JTextField jTxtNome;
    private javax.swing.JTextField jTxtPesquisa_Multi;
    // End of variables declaration//GEN-END:variables
}
