import dao.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;

public class MainForm extends JFrame {
    // TODO: generate ids for tables

    private static StudyDepartment department;

    private JTable studentsTable;
    private JTable groupsTable;

    private JTextPane resultTextPane;

    private JTextField studentCodeField1;
    private JTextField studentNameField;
    private JTextField groupCodeField1;
    private JTextField groupCodeField2;
    private JTextField groupNameField;
    private JTextField studentCodeField2;
    private JTextField groupCodeField3;

    private MainForm() {
        setLayout(null);

        initResultPane();

        initTables();

        initFields();
        initButtons();

        setSize(500, 620);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //--------------------------------------------------------------------------------------------

    private void addStudentRow(Student s) {
        DefaultTableModel studentsModel = (DefaultTableModel) studentsTable.getModel();
        Object[] rowData = new Object[3];

        rowData[0] = s.code;
        rowData[1] = s.name;
        rowData[2] = s.groupId;

        studentsModel.addRow(rowData);
        studentsTable.updateUI();
        resultTextPane.setText("New student was added!");
    }

    private void addGroupRow(Group g) {
        DefaultTableModel groupsModel = (DefaultTableModel) groupsTable.getModel();
        Object[] rowData = new Object[2];
        rowData[0] = g.code;
        rowData[1] = g.name;
        groupsModel.addRow(rowData);
        groupsTable.updateUI();

        resultTextPane.setText("New group was added!");
    }

    private void initTables() {
        initStudentsTable();
        initGroupsTable();

        JScrollPane studentsScrollPane = new JScrollPane(studentsTable);
        JScrollPane groupsScrollPane = new JScrollPane(groupsTable);

        JTabbedPane pane = new JTabbedPane();
        pane.setBounds(0, 0, 500, 300);
        pane.add("Students", studentsScrollPane);
        pane.add("Groups", groupsScrollPane);

        add(pane);
    }

    //---------------------------------------------
    private void initStudentsTable() {
        String[] studentsColumns = {"code", "name", "group_code"};
        Object[] rowData = new Object[4];
        DefaultTableModel studentsModel = new DefaultTableModel();
        studentsModel.setColumnIdentifiers(studentsColumns);

        List<Student> students = null;
        try {
            students = department.getAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Student student : students) {
            rowData[0] = student.code;
            rowData[1] = student.name;
            rowData[2] = student.groupId;
            studentsModel.addRow(rowData);
        }
        studentsTable = new JTable(studentsModel);
        studentsTable.setBackground(new Color(246, 217, 221));

        studentsTable.setFillsViewportHeight(true);
        studentsTable.setColumnSelectionAllowed(false);
        studentsTable.setRowSelectionAllowed(true);

        addStudentsTableListener();
    }

    private void addStudentsTableListener() {
        studentsTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                studentsTable.getModel().removeTableModelListener(this);

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = studentsTable.getColumnName(column);

                    if (columnName.equals("name")) {
                        updateStudentName(row, column);
                    }

                    if (columnName.equals("group_code")) {
                        updateStudentGroup(row, column);
                    }
                }
                studentsTable.getModel().addTableModelListener(this);
            }
        });
    }

    private void updateStudentName(int row, int column) {
        int studentId = (Integer) studentsTable.getValueAt(row, 0);
        Student student = null;
        try {
            student = department.getStudent(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String oldName = student.name;
        String newName = (String) studentsTable.getValueAt(row, column);
        student.name = newName;

        if (!oldName.equals(newName)) {
            try{
                department.updateStudent(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultTextPane.setText("Student's name has been changed: " + oldName + " -> " + newName);
        }
    }

    private void updateStudentGroup(int row, int column) {
        int studentId = (Integer) studentsTable.getValueAt(row, 0);
        Student student = null;
        try {
            student = department.getStudent(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int oldGroupId = student.groupId;
        int newGroupId = (Integer) studentsTable.getValueAt(row, column);
        student.groupId = newGroupId;

        if (oldGroupId != newGroupId) {
            try {
                department.updateStudent(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultTextPane.setText("Student's group has been changed: " + oldGroupId + " -> " + newGroupId);
        }
    }

    //---------------------------------------------

    private void initGroupsTable() {
        String[] groupsColumns = {"code", "name"};
        Object[] rowData = new Object[2];
        DefaultTableModel groupsModel = new DefaultTableModel();
        groupsModel.setColumnIdentifiers(groupsColumns);

        List<Group> groups = null;
        try {
            groups = department.getAllGroups();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Group group : groups) {
            rowData[0] = group.code;
            rowData[1] = group.name;
            groupsModel.addRow(rowData);
        }
        groupsTable = new JTable(groupsModel);
        groupsTable.setBackground(new Color(254, 221, 109));

        groupsTable.setFillsViewportHeight(true);
        groupsTable.setColumnSelectionAllowed(false);
        groupsTable.setRowSelectionAllowed(true);

        addGroupsTableListener();
    }

    private void addGroupsTableListener() {

        groupsTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                groupsTable.getModel().removeTableModelListener(this);

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = groupsTable.getColumnName(column);

                    if (columnName.equals("name")) {
                        updateGroupName(row, column);
                    }

                }
                groupsTable.getModel().addTableModelListener(this);
            }
        });
    }

    private void updateGroupName(int row, int column) {
        int groupId = (Integer) groupsTable.getValueAt(row, 0);
        Group group = null;
        try {
            group = department.getGroup(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String oldName = group.name;
        String newName = (String) groupsTable.getValueAt(row, column);
        group.name = newName;

        if (!oldName.equals(newName)) {
            try {
                department.updateGroup(group);
            }catch (Exception e) {
                e.printStackTrace();
            }
            resultTextPane.setText("Group's name has been changed: " + oldName + " -> " + newName);
        }
    }

    //--------------------------------------------------------------------------------------------

    private void initResultPane() {
        resultTextPane = new JTextPane();
        resultTextPane.setText("Here you will see results. " +
                "\n\n Select row to delete it. " +
                "\n Double click on the cell to edit it.");
        resultTextPane.setBounds(5, 450, 490, 100);
        resultTextPane.setEditable(false);
        add(resultTextPane);
    }

    //---------------------------------------------

    private void initButtons() {

        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setBounds(400, 300, 90, 20);
        addStudentButton.addActionListener((e) -> addStudentButtonListener());

        add(addStudentButton);

        JButton addGroupButton = new JButton("Add Group");
        addGroupButton.setBounds(400, 330, 90, 20);
        addGroupButton.addActionListener((e) -> addGroupButtonListener());

        add(addGroupButton);

        // -------------------------------------------------------------

        JButton findStudentButton = new JButton("Find Student");
        findStudentButton.setBounds(110, 370, 90, 20);
        findStudentButton.addActionListener((e) -> findStudentButtonListener());

        add(findStudentButton);

        JButton findGroupButton = new JButton("Find Group");
        findGroupButton.setBounds(400, 370, 90, 20);
        findGroupButton.addActionListener((e) -> {
            try {
                findGroupButtonListener();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(findGroupButton);

        JButton findStudentsInGroupButton = new JButton("Students in Group");
        findStudentsInGroupButton.setBounds(400, 400, 90, 20);
        findStudentsInGroupButton.addActionListener((e) -> {
            try {
                findStudentsInGroupButtonListener();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(findStudentsInGroupButton);

        // -------------------------------------------------------------

        JButton deleteRowButton = new JButton("Delete Row");
        deleteRowButton.setBounds(5, 560, 90, 20);
        deleteRowButton.addActionListener((e) -> deleteRowButtonListener());

        add(deleteRowButton);

        JButton saveToDBButton = new JButton("Save to DB");
        saveToDBButton.setBounds(100, 560, 90, 20);
        saveToDBButton.addActionListener((e) -> {
            try {
                department.saveToDB();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });

        add(saveToDBButton);

        JButton saveToFileButton = new JButton("REFRESH");
        saveToFileButton.setBounds(400, 560, 90, 20);
        saveToFileButton.addActionListener((e) ->
        {
            //TODO: make it save DB to XML file
            updateTables();
        });

        add(saveToFileButton);

        JButton loadFileButton = new JButton("Load File");
        loadFileButton.setBounds(305, 560, 90, 20);
        loadFileButton.addActionListener((e) -> {
            XMLLoader.loadFromFile();
            try {
                department.update();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            updateTables();

        });

        add(loadFileButton);
    }

    private void addStudentButtonListener() {
        if (studentCodeField1.getText().equals("")) {
            resultTextPane.setText("New student should have code!");
            return;
        }
        int code = Integer.parseInt(studentCodeField1.getText());
        Student student = null;
        try {
            department.getStudent(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (student != null) {
            resultTextPane.setText("Student with this code already exists.");
            return;
        }

        String name = studentNameField.getText();

        int groupCode = Integer.parseInt(groupCodeField1.getText());
        Group group = null;
        try {
            group = department.getGroup(groupCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (group == null) {
            resultTextPane.setText("This group doesn't exist!");
            return;
        }

        try {
            department.addStudent(new Student(code, name, groupCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        addStudentRow(new Student(code, name, groupCode));
    }

    private void addGroupButtonListener() {
        if (groupCodeField2.getText().equals("")) {
            resultTextPane.setText("New group should have code!");
            return;
        }
        int code = Integer.parseInt(groupCodeField2.getText());
        String name = groupNameField.getText();

//        Group group = groupDao.getGroup(code);
        Group group = null;
        try {
            group = department.getGroup(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (group != null) {
            resultTextPane.setText("Group with this code already exists.");
            return;
        }
        try {
            department.addGroup(new Group(code, name));
        } catch (Exception e) {
            e.printStackTrace();
        }


        addGroupRow(new Group(code, name));
    }

    private void findStudentButtonListener() {
        if (studentCodeField2.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a student!");
            return;
        }
        int code = Integer.parseInt(studentCodeField2.getText());
        Student student = null;
        try {
            student = department.getStudent(code);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (student == null) {
            resultTextPane.setText("Student with this code doesn't exist!");
            return;
        }
        Group group = null;
        try {
            group = department.getGroup(student.groupId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        resultTextPane.setText(" " + student.name + " " + group.name);
    }

    private void findGroupButtonListener() throws RemoteException {
        if (groupCodeField3.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a group!");
            return;
        }
        int code = Integer.parseInt(groupCodeField3.getText());
        Group group = department.getGroup(code);

        if (group == null) {
            resultTextPane.setText("Group with this code doesn't exist!");
            return;
        }

        resultTextPane.setText(group.name);
    }

    private void findStudentsInGroupButtonListener() throws RemoteException {
        if (groupCodeField3.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a group!");
            return;
        }
        int code = Integer.parseInt(groupCodeField3.getText());
        Group group = department.getGroup(code);

        if (group == null) {
            resultTextPane.setText("Group with this code doesn't exist!");
            return;
        }

        List<Student> students = department.getAllStudents();
        resultTextPane.setText("");
        for (Student student : students) {
            if (student.groupId == code) {
                resultTextPane.setText(resultTextPane.getText() + "\n" + student.name);
            }
        }
    }

    private void deleteRowButtonListener() {
        DefaultTableModel studentModel = (DefaultTableModel) studentsTable.getModel();
        int selRow = studentsTable.getSelectedRow();
        if (selRow != -1) {
            try {
                int studentId = (Integer) studentsTable.getValueAt(selRow, 0);
                department.deleteStudent(studentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            studentModel.removeRow(selRow);
            studentsTable.updateUI();
            resultTextPane.setText("Student was deleted.");
        }

        DefaultTableModel groupModel = (DefaultTableModel) groupsTable.getModel();
        selRow = groupsTable.getSelectedRow();
        int groupId = -1;
        if (selRow != -1) {
            try {
                groupId = (Integer) groupsTable.getValueAt(selRow, 0);
                department.deleteGroup(groupId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            groupModel.removeRow(selRow);
            groupsTable.updateUI();
            resultTextPane.setText("Group was deleted.");

            try {
                List<Student> students = department.getAllStudents();
                for (Student student : students) {
                    if (student.groupId == groupId) {
                        department.deleteStudent(student.code);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            updateTables();
        }
    }

    void updateTables() {
        ((DefaultTableModel) studentsTable.getModel()).setRowCount(0);
        ((DefaultTableModel) groupsTable.getModel()).setRowCount(0);
        try {
            List<Student> students = department.getAllStudents();
            for (Student student : students) {
                addStudentRow(student);
            }

            List<Group> groups = department.getAllGroups();
            for (Group group : groups) {
                addGroupRow(group);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }



    }

    //---------------------------------------------

    private void initFields() {
        studentCodeField1 = new JTextField();
        studentCodeField1.setBounds(5, 300, 90, 20);
        studentCodeField1.setToolTipText("student code");
        add(studentCodeField1);

        studentNameField = new JTextField();
        studentNameField.setBounds(95, 300, 120, 20);
        studentNameField.setToolTipText("student name");
        add(studentNameField);


        groupCodeField1 = new JTextField();
        groupCodeField1.setBounds(305, 300, 90, 20);
        groupCodeField1.setToolTipText("group code");
        add(groupCodeField1);

        //----------------------------------------------

        groupCodeField2 = new JTextField();
        groupCodeField2.setBounds(215, 330, 90, 20);
        groupCodeField2.setToolTipText("group code");
        add(groupCodeField2);

        groupNameField = new JTextField();
        groupNameField.setBounds(305, 330, 90, 20);
        groupNameField.setToolTipText("group name");
        add(groupNameField);

        //----------------------------------------------

        studentCodeField2 = new JTextField();
        studentCodeField2.setBounds(5, 370, 90, 20);
        studentCodeField2.setToolTipText("student code");
        add(studentCodeField2);

        groupCodeField3 = new JTextField();
        groupCodeField3.setBounds(305, 370, 90, 20);
        groupCodeField3.setToolTipText("group code");
        add(groupCodeField3);
    }

    //--------------------------------------------------------------------------------------------

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException{
        String url = "//localhost:1099/StudyDepartment";
        department = (StudyDepartment) Naming.lookup(url);
        System.out.println("🌿 RMI object found!");
        new MainForm();
    }
}

//TODO: refreshing tables for several clients