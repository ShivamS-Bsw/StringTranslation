package com.bsw.ui;

// Java program to implement
// a Simple Registration Form
// using Java Swing

import com.bsw.GoogleSheetsService;
import com.bsw.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame implements ActionListener {

    // Components of the Form
    private Container c;
    private JLabel sheetIdLabel;
//    private JTextField sheetId;
//    private JLabel sheetNameLable;
//    private JTextField sheetName;
    private JLabel pathLable;
    private JTextField path;
    private JButton submit;
    private JButton select;
//    private JButton save;
    private JFileChooser fileChooser ;
    private JLabel errroLable;
    private JCheckBox term;
    private JTextArea output;
    private JComboBox project;

    // constructor, to initialize the components
    // with default values.
    public MyFrame() {
        setTitle("Copy Strings");
        setBounds(100, 90, 680, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        sheetIdLabel = new JLabel("Select Project");
        sheetIdLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        sheetIdLabel.setSize(100, 15);
        sheetIdLabel.setLocation(20, 50);
        c.add(sheetIdLabel);
//
//        sheetId = new JTextField();
//        sheetId.setFont(new Font("Arial", Font.PLAIN, 15));
//        sheetId.setSize(150, 20);
//        sheetId.setLocation(120, 50);
//        c.add(sheetId);
//
//        sheetNameLable = new JLabel("Sheet Name");
//        sheetNameLable.setFont(new Font("Arial", Font.PLAIN, 15));
//        sheetNameLable.setSize(100, 15);
//        sheetNameLable.setLocation(20, 90);
//        c.add(sheetNameLable);
//
//        sheetName = new JTextField();
//        sheetName.setFont(new Font("Arial", Font.PLAIN, 15));
//        sheetName.setSize(150, 20);
//        sheetName.setLocation(120, 90);
//        c.add(sheetName);

        project = new JComboBox(MyConstants.getProjectName());
        project.setFont(new Font("Arial", Font.PLAIN, 15));
        project.setSize(200, 20);
        project.setLocation(120, 48);
        c.add(project);

        pathLable = new JLabel("Local Path");
        pathLable.setFont(new Font("Arial", Font.PLAIN, 15));
        pathLable.setSize(100, 15);
        pathLable.setLocation(20, 130);
        c.add(pathLable);

        path = new JTextField();
        path.setFont(new Font("Arial", Font.PLAIN, 15));
        path.setSize(200, 20);
        path.setLocation(120, 130);
        c.add(path);

        select = new JButton("Select");
        select.setFont(new Font("Arial", Font.PLAIN, 12));
        select.setSize(50, 20);
        select.setLocation(330, 130);
        select.addActionListener(this);
        c.add(select);

        term = new JCheckBox("Sure you want to go ahead?");
        term.setFont(new Font("Arial", Font.PLAIN, 13));
        term.setSize(250, 20);
        term.setLocation(120, 160);
        c.add(term);

//        save = new JButton("Save");
//        save.setFont(new Font("Arial", Font.PLAIN, 15));
//        save.setSize(70, 20);
//        save.setLocation(120, 190);
//        save.addActionListener(this);
//        c.add(save);

        submit = new JButton("Submit");
        submit.setFont(new Font("Arial", Font.PLAIN, 15));
        submit.setSize(70, 20);
        submit.setLocation(150, 190);
        submit.addActionListener(this);
        c.add(submit);

        errroLable = new JLabel("");
        errroLable.setFont(new Font("Arial", Font.PLAIN, 12));
        errroLable.setSize(250, 15);
        errroLable.setLocation(150, 220);
        c.add(errroLable);

        output = new JTextArea();
        output.setFont(new Font("Arial", Font.PLAIN, 13));
        output.setSize(250, 250);
        output.setLocation(400, 15);
        output.setAutoscrolls(true);
        output.setLineWrap(true);
        output.setEditable(false);
        c.add(output);


        JPanel groupBoxEncryption = new JPanel();

        JScrollPane scrollPanePlain = new JScrollPane(output);
        groupBoxEncryption.add(scrollPanePlain);
        scrollPanePlain.setBounds(400, 15, 250, 250);
        scrollPanePlain.setVisible(true);
        c.add(scrollPanePlain);
        setVisible(true);
    }

    public void writeOutput(String logs){
        output.append(logs + "\n");
        invalidate();
    }



    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == select){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Path to res folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fileChooser.showDialog(this,"Select") == JFileChooser.APPROVE_OPTION)
            {
                String fileID = fileChooser.getSelectedFile().getPath();
                path.setText(fileID);
            }
        }

        if (e.getSource() == submit){

//            if (sheetId.getText().trim().isEmpty()){
//                errroLable.setText("* Sheet Id cannot be empty");
//                return;
//            }else if (sheetName.getText().trim().isEmpty()){
//                errroLable.setText("* Sheet Name cannot be empty");
//                return;
//            }else

            if (project.getSelectedIndex() == -1 || project.getSelectedItem() == null || project.getSelectedItem().toString().trim().isEmpty()){
                errroLable.setText("* Select project");
                return;
            } else if (path.getText().trim().isEmpty()){
                errroLable.setText("* Select a local path to copy strings");
                return;
            }else if (!term.isSelected()){
                term.grabFocus();
                errroLable.setText("");
                return;
            }else{
                errroLable.setText("");
            }

            MyConstants.MASTER_SHEET = MyConstants.projectName.get((String)project.getSelectedItem());
            MyConstants.SHEET_ID = MyConstants.projectSheetIdMapName.get(MyConstants.MASTER_SHEET);
            MyConstants.setFilePath(path.getText().trim());
            MyConstants.currentVersion = 0;
            clearData();
            GoogleSheetsService.setup();
        }
    }
    private void clearData(){
        output.setText("");
//        sheetId.setText("");
//        sheetName.setText("");
        path.setText("");
        term.setSelected(false);
        invalidate();
    }
}

