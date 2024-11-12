package cvsu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class checklist extends JFrame{
	
	private JPanel design;
	BufferedImage cvsuLogo, cvsuLogo2, id, searchIcon, studentIcon, studentNum, course, info, gmailPict;
    private DefaultTableModel tableModel;
    public static JTable table;
    private JTextField searchBar;
    private JTextField searchField;
    private JLabel x, clear;
    private String logoPath = "C://Users/Admin/programming/midterm/src/images/cvsuLogo.png";
    
    
	checklist(){
		
		try {			
			cvsuLogo = null;
			cvsuLogo2 = null;
			id = null;
			searchIcon = null;
			studentIcon = null;
			studentNum = null;
			course = null;
			info = null;
			gmailPict = null;
						cvsuLogo = ImageIO.read(getClass().getResource("/images/cvsuLogo.png"));
						cvsuLogo2 = ImageIO.read(getClass().getResource("/images/cvsuLogo.png"));
						id = ImageIO.read(getClass().getResource("/images/id.png"));
						searchIcon = ImageIO.read(getClass().getResource("/images/searchIcon.png"));
						studentIcon = ImageIO.read(getClass().getResource("/images/student.png"));
						studentNum = ImageIO.read(getClass().getResource("/images/studentNum.png"));
						course = ImageIO.read(getClass().getResource("/images/course.png"));
						info = ImageIO.read(getClass().getResource("/images/info.png"));
						gmailPict = ImageIO.read(getClass().getResource("/images/gmailPict.png"));
						
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		setSize(1100, 630);
		setUndecorated(true);	
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(logoPath));
		
		 	connectAndPopulateTable();
		 
		 	JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBorder(BorderFactory.createEmptyBorder());
	        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setPreferredSize(new Dimension(1100, 850));
	        scrollPane.setBounds(0, 75, 1100, 800);
 	
	        add(scrollPane);
	        
	        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
	        verticalScrollBar.setUnitIncrement(25);
	        verticalScrollBar.setBlockIncrement(50);
	        
	        design = new design();
	        design.setPreferredSize(new Dimension(1100, 1200));	       
	        scrollPane.setViewportView(design);
	    
	}	
	
	private void connectAndPopulateTable() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String url = "jdbc:mysql://127.0.0.1:3306/mychecklist";
        String user = "root";
        String password = "StephenCurry30";

        try {
            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Execute query
            String query = "SELECT * FROM student " +
                    "LEFT JOIN subject ON student.StudentId = subject.CourseId " +
                    "LEFT JOIN sem_year ON subject.CourseId = sem_year.sem_yearId " +
                    "LEFT JOIN grade ON sem_year.sem_yearId = grade.GradeId " +
                    "LEFT JOIN professor ON grade.GradeId = professor.ProfessorId ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Get ResultSet metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create table model with column names
            tableModel = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
     
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            // Populate table model with data
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
               }
                tableModel.addRow(rowData);
            }
            
           table = new JTable();
           table.setDefaultRenderer(Object.class, new AlternateRowColorRenderer());
           table.setShowVerticalLines(false);
           table.setShowHorizontalLines(false);        
           table.setModel(tableModel);
           
	        JTableHeader header = table.getTableHeader();
	        header.setBackground(Color.decode("#88DC3D"));
	        header.setForeground(Color.decode("#26590F")); 
	        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 30));
	        header.setFont(new Font("Arial Black", Font.BOLD, 12));
             
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	  public class AlternateRowColorRenderer extends DefaultTableCellRenderer {
		  
	        @Override
	       
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	                                                       boolean hasFocus, int row, int column) {
	            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            
	            String searchText = searchField.getText().toLowerCase();
	           
	            if (row % 2 == 0) {
	                component.setBackground(Color.decode("#D4D4D4"));
	               
	            } else {
	                component.setBackground(Color.WHITE);
	            }

	            if (searchText.length() > 0 && value != null) {
	                String cellText = value.toString().toLowerCase();
	                if (cellText.contains(searchText)) {
	                    component.setBackground(Color.decode("#88DC3D"));
	                }
	            }
	            
	            return component;     
	        }	       
	        }

	  class design extends JPanel {
	
		design () {
			
			try {
	            searchIcon = ImageIO.read(getClass().getResource("/images/searchIcon.png"));
	            studentIcon = ImageIO.read(getClass().getResource("/images/student.png"));
				studentNum = ImageIO.read(getClass().getResource("/images/studentNum.png"));
				course = ImageIO.read(getClass().getResource("/images/course.png"));
				info = ImageIO.read(getClass().getResource("/images/info.png"));
				gmailPict = ImageIO.read(getClass().getResource("/images/gmailPict.png"));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        setLayout(null); // Use absolute positioning
	        
	        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        table.setBorder(null);
	        table.setShowGrid(false);
	        table.setFont(new Font("Micosoft JhengHei", Font.BOLD, 12));         
	        
	        table.getColumnModel().getColumn(1).setPreferredWidth(120);
	        table.getColumnModel().getColumn(2).setPreferredWidth(120);
	        table.getColumnModel().getColumn(3).setPreferredWidth(250);
	        table.getColumnModel().getColumn(4).setPreferredWidth(120);
	        table.getColumnModel().getColumn(6).setPreferredWidth(120);
	        table.getColumnModel().getColumn(7).setPreferredWidth(270);
	        table.getColumnModel().getColumn(8).setPreferredWidth(120);
	        table.getColumnModel().getColumn(9).setPreferredWidth(120);
	        table.getColumnModel().getColumn(10).setPreferredWidth(130);
	        table.getColumnModel().getColumn(11).setPreferredWidth(130);
	        table.getColumnModel().getColumn(12).setPreferredWidth(120);
	        table.getColumnModel().getColumn(13).setPreferredWidth(120);
	        table.getColumnModel().getColumn(14).setPreferredWidth(120);
	        table.getColumnModel().getColumn(16).setPreferredWidth(120);
	        table.getColumnModel().getColumn(17).setPreferredWidth(120);
	        table.getColumnModel().getColumn(18).setPreferredWidth(120);	       
	        
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setBounds(50, 390, 1000, 753); // Adjust position and size as needed
	        scrollPane.setBorder(null);
	        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
	        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
	        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	        add(scrollPane);
	        
	        setRowHeight();

	        searchField = new JTextField();
	        searchField.setPreferredSize(new Dimension(200, 30));
	        searchField.setBounds(395, 116, 320, 31);
	        searchField.setFocusable(true);
	        // Add a document listener to the search field to filter the table as the user types
	        searchField.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void insertUpdate(DocumentEvent e) {
	                filterTable();
	            }

	            @Override
	            public void removeUpdate(DocumentEvent e) {
	                filterTable();
	            }

	            @Override
	            public void changedUpdate(DocumentEvent e) {
	                filterTable();
	            }
	        });
	        
	        add(searchField);
	        
	        x = new JLabel("x");
	        x.setBounds(1050, 15, 50, 25);
	        x.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 30));
	        x.setForeground(Color.WHITE);
	        x.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent e) {
	                x.setForeground(Color.decode("#88DC3D"));
	            }
	            public void mouseClicked(MouseEvent e) {
	            	
	            	dispose();
	            }

	            public void mouseExited(MouseEvent e) {
	                x.setForeground(Color.WHITE);
	            }
	        });
	        
	        add(x);
	        
	        clear = new JLabel("X");
	        clear.setBounds(724, 120, 25, 25);
	        clear.setFont(new Font("Arial", Font.PLAIN, 18));
	        clear.setForeground(Color.decode("#26580F"));
	        clear.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent e) {
	            	clear.setForeground(Color.decode("#88DC3D"));
	            }
	            public void mouseClicked(MouseEvent e) {
	            	
	            	searchField.setText("");
	            }

	            public void mouseExited(MouseEvent e) {
	            	clear.setForeground(Color.decode("#26580F"));
	            }
	        });
	        
	        add(clear);

		}
		

	    protected void paintComponent(Graphics graphics) {
	        super.paintComponent(graphics);
	        
	   
	        Graphics2D graphics2d = (Graphics2D) graphics.create();
	        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

	        GradientPaint gradient = new GradientPaint(50, 126, Color.decode("#88DC3D"), 1050, 240, Color.decode("#26580F"));
	        
	        graphics2d.setColor(Color.decode("#26580F"));
	        graphics2d.fillRect(0, 0, 1100, 630);
	        graphics2d.setColor(Color.decode("#FAFAFA"));
	        graphics2d.fillRect(0, 60, 1100, 1200);
	        
	        graphics2d.fillOval(17, 19, 15, 15);
	        graphics2d.fillOval( 40, 19, 15, 15);
	        graphics2d.fillOval(63, 19, 15, 15);
	        
	        graphics2d.setColor(Color.decode("#FAFAFA"));
	        graphics2d.fillRoundRect(30, 90, 1040, 520, 30, 30);
	        graphics2d.setPaint(gradient);
	        graphics2d.fillRoundRect(50, 102, 1000, 138, 30, 30);
	    
	        graphics2d.setColor(Color.WHITE);
	        graphics2d.setFont(new Font("Impact", Font.BOLD, 15));
	        graphics2d.drawString("CHECKLIST OF COURSES", 478, 34);
	       
	        graphics2d.setColor(Color.decode("#26590F"));
	        graphics2d.setFont(new Font("Gill Sans MT", Font.BOLD, 16));
	        
	        graphics2d.setColor(Color.decode("#FFFFFF"));
	        graphics2d.setFont(new Font("Microsoft JhengHei", Font.BOLD, 9));
	        graphics2d.drawString("1906", 107, 205);
	        graphics2d.setFont(new Font("STENCIL", Font.BOLD, 14));
	        graphics2d.drawString("PHILIPPINES", 72, 220);
	        graphics2d.fillRoundRect(350, 117, 400, 30, 20, 20);
	        
	        graphics2d.setColor(Color.decode("#FFFFFF"));
	        graphics2d.setFont(new Font("ENGRAVERS MT", Font.PLAIN, 10));
	        graphics2d.drawString("Republic of the Philippines", 443, 174);
	        
	        graphics2d.setFont(new Font("ENGRAVERS MT", Font.PLAIN, 25));
	        graphics2d.drawString("CAVITE STATE UNIVERSITY", 314, 195);
	        
	        graphics2d.setFont(new Font("ENGRAVERS MT", Font.PLAIN, 19));
	        graphics2d.drawString("BACOOR CITY CAMPUS", 400, 213);
	        
	        graphics2d.setPaint(gradient);
	        graphics2d.fillRoundRect(50, 250, 320, 118, 30, 30);
	        graphics2d.fillRoundRect(390, 250, 320, 118, 30, 30);
	        graphics2d.fillRoundRect(730, 250, 320, 118, 30, 30);
	      
	        graphics2d.drawImage(searchIcon, 360, 120, 23, 23, null);
	        graphics2d.drawImage(studentIcon, 70, 280, 60, 60, null);
	        graphics2d.drawImage(studentNum, 410, 280, 60, 60, null);
	        graphics2d.drawImage(course, 750, 280, 60, 60, null);	        
	        graphics2d.drawImage(info, 338, 255, 25, 25, null);
	        graphics2d.drawImage(info, 678, 255, 25, 25, null);
	        graphics2d.drawImage(info, 1018, 255, 25, 25, null);
	       // graphics2d.drawImage(gmailPict, 830, -25, 90, 110, null);
	        graphics2d.drawImage(cvsuLogo2, 72, 125, 90, 70, null);
	       
	        graphics2d.setColor(Color.WHITE);
	        graphics2d.setFont(new Font("Microsoft JhengHei", Font.BOLD, 11));
	        graphics2d.drawString("STUDENT NAME", 165, 294);
	        graphics2d.drawString("STUDENT NUMBER", 500, 294);
	        graphics2d.drawString("COURSE / SECTION", 835, 294);
	        
	        graphics2d.setFont(new Font("Arial Black", Font.BOLD, 24));
	        graphics2d.drawString("Linus Aurin", 165, 320);
	        graphics2d.drawString("202211669", 500, 320);
	        graphics2d.drawString("BSCS 2-6", 835, 320);

	        //graphics2d.setFont(new Font("Arial", Font.PLAIN, 10));
	        //graphics2d.drawString("aurinlinus26@gmail.com", 910, 30);
	        
	    }
	}
	
	 private void setRowHeight() {
	        // Set row height to 50 pixels
	        int rowHeight = 50;
	        for (int i = 0; i < table.getRowCount(); i++) {
	            table.setRowHeight(i, rowHeight);
	        }
	    }
		
	 private void filterTable() {
		    String searchText = searchField.getText().toLowerCase();
		    DefaultTableModel model = (DefaultTableModel) table.getModel();
		    TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
		    table.setRowSorter(rowSorter);
		    if (searchText.trim().length() == 0) {
		        rowSorter.setRowFilter(null);
		    } else {
		        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
		        if (rowSorter.getViewRowCount() == 0) {
		            JOptionPane.showMessageDialog(this, "Data cannot be found", "Error", JOptionPane.ERROR_MESSAGE);
		            rowSorter.setRowFilter(null); // Reset the filter
		        }
		    }
		    setRowHeight();
		}


	 static class CustomScrollBarUI extends BasicScrollBarUI {

	        @Override
	        protected void configureScrollBarColors() {
	            thumbColor = Color.decode("#D4D4D4"); // Set thumb color
	            thumbDarkShadowColor = thumbColor.darker(); // Set thumb shadow color
	            thumbHighlightColor = thumbColor.brighter(); // Set thumb highlight color
	            thumbLightShadowColor = thumbColor; // Set thumb light shadow color
	            trackColor = Color.decode("#ffffff"); // Set track color
	            trackHighlightColor = Color.decode("#ffffff"); // Set track highlight color
	        }
	 }
	
	public static void main(String[]args) {
				
		checklist frame = new checklist();
		frame.setVisible(true);
		
		}
}
