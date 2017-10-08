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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luiz.barcellos
 */
public class CadPessoas extends javax.swing.JInternalFrame {

    /**
     * Creates new form cadPessoas
     */
    public CadPessoas() {
        initComponents();
        
    }

    int var_consulta;

    ConexaoMySQL cn = new ConexaoMySQL();
    String url;
    String driver = "com.mysql.jdbc.Driver";
    String usuario;
    String senha;

    DateFormat dateOut = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat dateIn = new SimpleDateFormat("dd/MM/yyyy");

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
        jBtnPesquisarCidade.setEnabled(true);
        jBtnPesquisar.setEnabled(false);

        jTxtNome.setEnabled(true);
        jTxtCPF.setEnabled(true);
        jTxtRG.setEnabled(true);
        jTxtRGEmissor.setEnabled(true);
        jSpRGData.setEnabled(true);
        jTxtEndereco.setEnabled(true);

    }

    public void DesabilitarEdicao() {
        jBtnGravar.setEnabled(false);
        jBtnPesquisarCidade.setEnabled(false);
        jBtnPesquisar.setEnabled(true);
        jBtnEditar.setEnabled(false);
        jBtnIncluir.setEnabled(true);

        jTxtNome.setEnabled(false);
        jTxtCPF.setEnabled(false);
        jTxtRG.setEnabled(false);
        jTxtRGEmissor.setEnabled(false);
        jSpRGData.setEnabled(false);
        jTxtEndereco.setEnabled(false);
        jTxtId.setEnabled(true);

        jBtnIncluir.setText("Incluir");
        jBtnEditar.setText("Editar");

    }

    public void LimpaCampos() {
        jTxtNome.setText("");
        jTxtCPF.setText("");
        jTxtRG.setText("");
        jTxtRGEmissor.setText("");
        //jSpRGData.setText("");
        jTxtEndereco.setText("");
        jTxtId.setText("");
        jTxtIdCidade.setText("");
        jTxtNomeCidade.setText("");
    }

    public void MontaLista() throws SQLException {
        DefaultTableModel lista = (DefaultTableModel) jTblConsulta_Multi.getModel();
        lista.setColumnCount(0); //LIMPA COLUNAS
        lista.setRowCount(0); // LIMPA LINHAS
        //jTxtPesquisa_Multi.setText("");

        switch (var_consulta) {
            case 1:
                //CONSULTA PESSOAS

                lista.addColumn("Id");
                lista.addColumn("Nome");
                lista.addColumn("CPF");

                jTblConsulta_Multi.getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getColumnModel().getColumn(0).setMinWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMinWidth(50);

                String sql1; //INSTANCIA A VARIÁVEL. SERÁ INICIALIZADA NO IF

                if ("".equals(jTxtPesquisa_Multi.getText())) {
                    sql1 = "SELECT * FROM pessoas";
                } else {
                    sql1 = "SELECT * FROM pessoas "
                            + "WHERE UPPER(nome) "
                            + "LIKE '%" + jTxtPesquisa_Multi.getText().toUpperCase() + "%' "
                            + "ORDER BY nome;";
                }

                DadosConexao();
                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql1);

                while (cn.rs.next()) {
                    
                    lista.addRow(new String[]{
                        cn.rs.getString(1),
                        cn.rs.getString(2),
                        cn.rs.getString(3)});
                }
                cn.desconecta();

                break;

            case 2:
                //CONSULTA MUNICÍPIOS

                lista.addColumn("Id");
                lista.addColumn("Cidade");
                lista.addColumn("UF");
                jTblConsulta_Multi.getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getColumnModel().getColumn(0).setMinWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(0).setMinWidth(50);
                jTblConsulta_Multi.getColumnModel().getColumn(2).setMaxWidth(50);
                jTblConsulta_Multi.getColumnModel().getColumn(2).setMinWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(50);
                jTblConsulta_Multi.getTableHeader().getColumnModel().getColumn(2).setMinWidth(50);

                String sql2; //INSTANCIA A VARIÁVEL. SERÁ INICIALIZADA NO IF

                if ("".equals(jTxtPesquisa_Multi.getText())) {
                    sql2 = "SELECT * FROM cad_munic";
                } else {
                    sql2 = "SELECT * FROM cad_munic "
                            + "WHERE UPPER(nome) "
                            + "LIKE '%" + jTxtPesquisa_Multi.getText().toUpperCase() + "%' "
                            + "ORDER BY nome;";
                }

                DadosConexao();
                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql2);

                while (cn.rs.next()) {
                    
                    lista.addRow(new String[]{
                        cn.rs.getString(1),
                        cn.rs.getString(4),
                        cn.rs.getString(2)});
                }
                cn.desconecta();

                break;
        }

    }

    public void IncluiPesquisa() throws SQLException {

        int linha = jTblConsulta_Multi.getSelectedRow();

        switch (var_consulta) {
            case 1:
                //INCLUI ID PESSOA
                jTxtId.setText(jTblConsulta_Multi.getValueAt(linha, 0).toString());

                var_consulta = 11;

                IncluiPesquisa();
                break;

            case 11:
                //INCLUI DADOS PESSOA
                String id_pessoa = jTxtId.getText();

                String sql11 = "SELECT * FROM pessoas "
                        + "WHERE id = '" + id_pessoa + "';";

                DadosConexao();
                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql11);

                try {
                    while (cn.rs.next()) {

                        String dti = dateIn.format(cn.rs.getObject(6));

                        jTxtNome.setText(cn.rs.getString(2));
                        jTxtCPF.setText(cn.rs.getString(3));
                        jTxtRG.setText(cn.rs.getString(4));
                        jTxtRGEmissor.setText(cn.rs.getString(5));
                        jSpRGData.setValue(dateIn.parse(dti));
                        jTxtEndereco.setText(cn.rs.getString(7));
                        jTxtIdCidade.setText(cn.rs.getString(8));
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }

                var_consulta = 22;

                IncluiPesquisa();

                if (!"".equals(jTxtNome.getText())) {

                    jBtnEditar.setEnabled(true);
                } else {
                    LimpaCampos();
                }

                break;

            case 2:

                jTxtIdCidade.setText(jTblConsulta_Multi.getValueAt(linha, 0).toString());

                var_consulta = 22;

                IncluiPesquisa();

                break;

            case 22:
                String id_cidade = jTxtIdCidade.getText();
                String sql22 = "SELECT concat(nome,' / ',uf) as nome FROM cad_munic "
                        + "WHERE id = '" + id_cidade + "';";

                cn.conecta(url, driver, usuario, senha);
                cn.executeConsulta(sql22);

                String nome_cidade;
                while (cn.rs.next()) {
                    
                    nome_cidade = cn.rs.getString(1);
                    
                    jTxtNomeCidade.setText(nome_cidade);
                }
                cn.desconecta();
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
        jPnlPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTxtId = new javax.swing.JTextField();
        jTxtNome = new javax.swing.JTextField();
        jTxtCPF = new javax.swing.JTextField();
        jTxtRG = new javax.swing.JTextField();
        jTxtRGEmissor = new javax.swing.JTextField();
        jTxtEndereco = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTxtIdCidade = new javax.swing.JTextField();
        jTxtNomeCidade = new javax.swing.JTextField();
        jSpRGData = new javax.swing.JSpinner();
        jBtnPesquisarCidade = new javax.swing.JButton();
        jBtnPesquisar = new javax.swing.JButton();
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
        setTitle("Cadastro de Pessoas");

        jLabel1.setText("Código");

        jLabel2.setText("CPF");

        jLabel3.setText("Identidade");

        jLabel4.setText("Órgão Emissor");

        jLabel5.setText("Data Emissão");

        jLabel6.setText("Endereço");

        jLabel7.setText("Cidade");

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

        jTxtCPF.setEnabled(false);
        jTxtCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtCPFActionPerformed(evt);
            }
        });

        jTxtRG.setEnabled(false);
        jTxtRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtRGActionPerformed(evt);
            }
        });

        jTxtRGEmissor.setEnabled(false);
        jTxtRGEmissor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtRGEmissorActionPerformed(evt);
            }
        });

        jTxtEndereco.setEnabled(false);

        jLabel8.setText("Nome");

        jTxtIdCidade.setEnabled(false);

        jTxtNomeCidade.setEnabled(false);

        jSpRGData.setModel(new javax.swing.SpinnerDateModel());
        jSpRGData.setEditor(new javax.swing.JSpinner.DateEditor(jSpRGData, "dd/MM/yyyy"));
        jSpRGData.setEnabled(false);

        jBtnPesquisarCidade.setText("...");
        jBtnPesquisarCidade.setEnabled(false);
        jBtnPesquisarCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPesquisarCidadeActionPerformed(evt);
            }
        });

        jBtnPesquisar.setText("...");
        jBtnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPesquisarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPnlPrincipalLayout = new javax.swing.GroupLayout(jPnlPrincipal);
        jPnlPrincipal.setLayout(jPnlPrincipalLayout);
        jPnlPrincipalLayout.setHorizontalGroup(
            jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlPrincipalLayout.createSequentialGroup()
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(28, 28, 28)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSpRGData, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jTxtRG))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtRGEmissor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel8)
                            .addComponent(jLabel2))
                        .addGap(59, 59, 59)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnPesquisar))))
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(47, 47, 47)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTxtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(jTxtIdCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTxtNomeCidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnPesquisarCidade))))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTxtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTxtRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTxtRGEmissor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpRGData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTxtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTxtIdCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtNomeCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnPesquisarCidade))
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
                    .addComponent(jBtnGravar, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
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
                .addComponent(jPnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPnlComandos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPnlComandos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTxtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtNomeActionPerformed

    private void jTxtCPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtCPFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtCPFActionPerformed

    private void jTxtRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtRGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtRGActionPerformed

    private void jTxtRGEmissorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtRGEmissorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtRGEmissorActionPerformed

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
        
        try {
            MontaLista();
        } catch (SQLException ex) {
            Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnPesquisarActionPerformed

    private void jBtnPesquisarCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPesquisarCidadeActionPerformed
        var_consulta = 2;

        jPesquisar.setVisible(true);

        try {
            MontaLista();
        } catch (SQLException ex) {
            Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnPesquisarCidadeActionPerformed

    private void jTxtPesquisa_MultiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtPesquisa_MultiKeyReleased
        try {
            MontaLista();
            
            jTblConsulta_Multi.setRowSelectionInterval(0, 0);
            
        } catch (SQLException ex) {
            Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTxtPesquisa_MultiKeyReleased

    private void jBtnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConfirmarActionPerformed
        try {
            IncluiPesquisa();
        } catch (SQLException ex) {
            Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String cpf = jTxtCPF.getText();
        String rg = jTxtRG.getText();
        String rgemis = jTxtRGEmissor.getText();
        String rgdata = dateOut.format(jSpRGData.getValue());
        String endereco = jTxtEndereco.getText();
        String cidade = jTxtIdCidade.getText();

        String sql;

        if (!"".equals(id)
                && !"".equals(nome)
                && !"".equals(cpf)
                && !"".equals(rg)
                && !"".equals(rgemis)
                && !"".equals(rgdata)
                && !"".equals(endereco)
                && !"".equals(cidade)) {
            if ("Cancelar".equals(jBtnIncluir.getText())) {
                sql = "INSERT INTO pessoas (nome,cpf,identidade,orgao_emissor,"
                        + "data_emissao,endereco,cidade) VALUES ('" + nome + "',"
                        + "'" + cpf + "','" + rg + "','" + rgemis + "','" + rgdata + "',"
                        + "'" + endereco + "','" + cidade + "');";
            } else {
                sql = "UPDATE pessoas SET nome = '" + nome + "', cpf = '" + cpf + "',"
                        + "identidade = '" + rg + "',orgao_emissor = '" + rgemis + "',"
                        + "data_emissao = '" + rgdata + "', endereco = '" + endereco + "',"
                        + "cidade = '" + cidade + "' "
                        + "where id = '" + id + "';";
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
            var_consulta = 11;

            try {
                IncluiPesquisa();
            } catch (SQLException ex) {
                Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            jBtnEditar.setEnabled(false);
            LimpaCampos();
        }


    }//GEN-LAST:event_jTxtIdKeyReleased

    private void jTxtPesquisa_MultiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtPesquisa_MultiKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            try {
                IncluiPesquisa();
            } catch (SQLException ex) {
                Logger.getLogger(CadPessoas.class.getName()).log(Level.SEVERE, null, ex);
            }
            jPesquisar.dispose();
        }
    }//GEN-LAST:event_jTxtPesquisa_MultiKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JButton jBtnConfirmar;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnGravar;
    private javax.swing.JButton jBtnIncluir;
    private javax.swing.JButton jBtnPesquisar;
    private javax.swing.JButton jBtnPesquisarCidade;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JFrame jPesquisar;
    private javax.swing.JPanel jPnlComandos;
    private javax.swing.JPanel jPnlPrincipal;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpRGData;
    private javax.swing.JTable jTblConsulta_Multi;
    private javax.swing.JTextField jTxtCPF;
    private javax.swing.JTextField jTxtEndereco;
    private javax.swing.JTextField jTxtId;
    private javax.swing.JTextField jTxtIdCidade;
    private javax.swing.JTextField jTxtNome;
    private javax.swing.JTextField jTxtNomeCidade;
    private javax.swing.JTextField jTxtPesquisa_Multi;
    private javax.swing.JTextField jTxtRG;
    private javax.swing.JTextField jTxtRGEmissor;
    // End of variables declaration//GEN-END:variables
}
