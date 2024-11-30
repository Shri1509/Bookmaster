package Book_master;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class BookMaster {
    // JDBC Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/l6\",\"root\",\"admin@1509";

    public static void main(String[] args) {
        // Initialize the database
        initializeDatabase();

        // Create main frame
        JFrame frame = new JFrame("BookMaster - Library Management");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Header
        JLabel header = new JLabel("BookMaster Library Management System", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(34, 139, 34)); // Green color
        frame.add(header, BorderLayout.NORTH);

        // Table Panel
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Year"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180)); // Steel blue
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load existing books
        loadBooks(tableModel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Book");
        JButton deleteButton = new JButton("Delete Book");
        addButton.setBackground(new Color(60, 179, 113)); // Green
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(255, 165, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add Book
        addButton.addActionListener(e -> showBookDialog(frame, tableModel, null, "Add Book"));

        // Edit Book
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            showBookDialog(frame, tableModel, id, "Edit Book");
        });

        // Delete Book
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteBook(id);
                tableModel.removeRow(selectedRow);
            }
        });

        // Display frame
        frame.setVisible(true);
    }

    private static void deleteBook(int id) {
		// TODO Auto-generated method stub
		
	}

	private static void loadBooks(DefaultTableModel tableModel) {
		// TODO Auto-generated method stub
		
	}

	private static void initializeDatabase() {
		// TODO Auto-generated method stub
		
	}

	private static void showBookDialog(JFrame frame, DefaultTableModel tableModel, Integer id, String title) {
        // Dialog for adding/editing books
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel yearLabel = new JLabel("Year:");

        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();

        if (id != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE id = ?")) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idField.setText(String.valueOf(rs.getInt("id")));
                    titleField.setText(rs.getString("title"));
                    authorField.setText(rs.getString("author"));
                    yearField.setText(String.valueOf(rs.getInt("year")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(titleLabel);
        dialog.add(titleField);
        dialog.add(authorLabel);
        dialog.add(authorField);
        dialog.add(yearLabel);
        dialog.add(yearField);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 179, 113));
        saveButton.setForeground(Color.WHITE);

        dialog.add(new JLabel()); // Empty space
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(idField.getText());
                String bookTitle = titleField.getText();
                String bookAuthor = authorField.getText();
                int bookYear = Integer.parseInt(yearField.getText());

                if (id == null) {
                    saveBook(bookId, bookTitle, bookAuthor, bookYear);
                    tableModel.addRow(new Object[]{bookId, bookTitle, bookAuthor, bookYear});
                } else {
                    updateBook(bookId, bookTitle, bookAuthor, bookYear);
                    int selectedRow = tableModel.getRowCount() - 1;
                    tableModel.setValueAt(bookTitle, selectedRow, 1);
                    tableModel.setValueAt(bookAuthor, selectedRow, 2);
                    tableModel.setValueAt(bookYear, selectedRow, 3);
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

	private static void saveBook(int bookId, String bookTitle, String bookAuthor, int bookYear) {
		// TODO Auto-generated method stub
		
	}

	private static void updateBook(int bookId, String bookTitle, String bookAuthor, int bookYear) {
		// TODO Auto-generated method stub
		
	}

    // Database methods: initializeDatabase(), loadBooks(), saveBook(), updateBook(), deleteBook()
    // Remain the same as in the previous example.
}
