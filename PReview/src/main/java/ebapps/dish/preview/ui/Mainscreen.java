package ebapps.dish.preview.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Reviewdata;
import ebapps.dish.preview.configuration.Values;
import ebapps.dish.preview.database.DbFunctions;

@SuppressWarnings("serial")
public class Mainscreen extends JFrame {

	private JPanel contentPane;
    private DbFunctions dbFunc = null;
    private JButton btnCreateGroup;
    private JButton btnCreateUsers;
    private JTable jtabReviews;
    private JComboBox<String> jcbFilter;
    private JTextField txtFilter;
    private Reviewdata rData = null;
	private JLabel lblSearchBy;

    
	/**
	 * Create the frame.
	 */
	public Mainscreen(DbFunctions _dbFunc) {
		setTitle("Review Analysis Tool - Main Screen");
		
		dbFunc = _dbFunc;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 759, 534);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmReports = new JMenuItem("Reports");
		mntmReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Reportscreen reportScreen = new Reportscreen(dbFunc);
				reportScreen.setVisible(true);
			}
		});
		mnOptions.add(mntmReports);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About aboutScreen = new About();
				aboutScreen.setVisible(true);
			}
		});
		mnOptions.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblReviewAnalysisTool = new JLabel("Review Analysis Tool");
		lblReviewAnalysisTool.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JButton btnSubmitReview = new JButton("Submit Review");
		btnSubmitReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SubmitFrame submitFrame = new SubmitFrame(dbFunc);
				submitFrame.setVisible(true);
			}
		});
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        dbFunc.disconnectFromDb();		
				System.exit(0);
			}
		});		
		
		btnCreateGroup = new JButton("Groupscreen");		
		btnCreateGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Groupscreen groupScreen = new Groupscreen(dbFunc);
				groupScreen.setVisible(true);
			}
		});
		
		btnCreateUsers = new JButton("Users");
		btnCreateUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Userscreen userFrame = new Userscreen(dbFunc);
				userFrame.setVisible(true);
			}
		});
		
		lblSearchBy = new JLabel("Search by");
		
		jcbFilter = new JComboBox<String>();
		jcbFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFilter.setText("");
				txtFilter.setEnabled(true);
				if (jcbFilter.getSelectedItem().toString().equals(Constants.S_QUERY_BY_ASG_GROUP))
				{
					txtFilter.setEnabled(false);
				}
			}
		});
		jcbFilter.setModel(new DefaultComboBoxModel<String>(new String[] {"Select", "Review ID", "Group Name", "Assigned Groups"}));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JButton btnSubmit = new JButton("Submit Review");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SubmitFrame submitFrame = new SubmitFrame(dbFunc);
				submitFrame.setVisible(true);
			}
		});
		
		JButton btnProcessReview = new JButton("Process Review");
		btnProcessReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null==rData)
				{
					JOptionPane.showMessageDialog(null,Constants.S_NO_REVIEW_SELECTED);
				}
				else
				{
					ProcessFrame procFrame = new ProcessFrame(dbFunc,rData);
					procFrame.setVisible(true);
				}
			}
		});
		
		JLabel label_1 = new JLabel("Search by");

		txtFilter = new JTextField();
		txtFilter.setColumns(10);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbFilter.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
				{
					JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + "filter type");				
				}
				else if ((null==txtFilter.getText() || txtFilter.getText().toString().length() < 1) && (!jcbFilter.getSelectedItem().toString().equals(Constants.S_QUERY_BY_ASG_GROUP)))
				{
					JOptionPane.showMessageDialog(null, Constants.S_INVALID_FILTER_VALUE);
				}
				else
				{
					ResultSet rsData = dbFunc.getReviews(jcbFilter.getSelectedItem().toString(), txtFilter.getText().toString());
						loadTable(rsData);
				}
			}
		});
		
	
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(32)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnProcessReview, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jcbFilter, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnCreateUsers, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCreateGroup, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)))
					.addGap(29))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(143)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(268, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(619, Short.MAX_VALUE)
					.addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addGap(32))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(54)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSubmit)
						.addComponent(btnProcessReview)
						.addComponent(btnCreateUsers)
						.addComponent(btnCreateGroup))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(jcbFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch))
					.addGap(29)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
					.addComponent(btnExit)
					.addGap(46))
		);
		
		jtabReviews = new JTable();
		jtabReviews.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
		      if (me.getClickCount()==2)
		      {
		    	  ProcessFrame procFrame = new ProcessFrame(dbFunc,rData);
					procFrame.setVisible(true);
		      }
			}
		});
		
		
		jtabReviews.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtabReviews.setRowSelectionAllowed(true);

		jtabReviews.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Artifact Type", "Artifact ID", "Status", "Review Condition", "Assigned Group", "# iterations", "Creator"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		jtabReviews.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		scrollPane_1.setViewportView(jtabReviews);

		
		contentPane.setLayout(gl_contentPane);

		ListSelectionModel rowSM = jtabReviews.getSelectionModel();

	    //Listener for client row change;
	    rowSM.addListSelectionListener(new ListSelectionListener() {

	        /// Fill the form values when a row is selected in the JTable
			public void valueChanged(ListSelectionEvent e) {
			      ListSelectionModel lsmData = (ListSelectionModel)e.getSource();
	               if (!lsmData.isSelectionEmpty())
	               {
	                   int iRow = lsmData.getMinSelectionIndex();
	                   {
	                	   rData = new Reviewdata();
	                	   rData.setiId(Integer.parseInt(jtabReviews.getValueAt(iRow, 0).toString()));
	                	   rData.setsArtifactType(jtabReviews.getValueAt(iRow, 1).toString());
	                	   rData.setsArtifactId(jtabReviews.getValueAt(iRow, 2).toString());
	                	   rData.setsStatus(jtabReviews.getValueAt(iRow, 3).toString());
	                	   rData.setsCondition(jtabReviews.getValueAt(iRow, 4).toString());
	                	   rData.setsGroup(jtabReviews.getValueAt(iRow, 5).toString());
	                	   rData.setiNumIterations(Integer.parseInt(jtabReviews.getValueAt(iRow, 6).toString()));
	                	   rData.setsCreator(jtabReviews.getValueAt(iRow, 7).toString());
	                   }
	                 }
	               else
	               {
	            	   rData = null;
	               }
	            }
	    });

		
	if (!Values.getsPrivs().equals("Admin"))
		{
			btnCreateUsers.setVisible(false);
			btnCreateGroup.setVisible(false);
		}
	}
	
	private void loadTable(ResultSet _rsData)
	{
		DefaultTableModel jtabModel = null;
		
		jtabReviews.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"ID", "Artifact Type", "Artifact ID","Status","Review Condition","Assigned Group","# iterations","Creator"
				}) {
				public boolean isCellEditable(int row, int column) {
					return false; 
				}}
			);	
		
        jtabModel = (DefaultTableModel) jtabReviews.getModel();

		try {
			if (null==_rsData)
			{
				JOptionPane.showMessageDialog(null, Constants.S_NO_DATA_RETURNED);
			}
			else
			{
			while (_rsData.next())
			{
			  jtabModel.addRow(new Object[]{_rsData.getInt(1),_rsData.getString(2),_rsData.getInt(3),_rsData.getString(4),_rsData.getString(5),_rsData.getString(6),_rsData.getInt(7), _rsData.getString(8)});
			}

			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadTable, SQLException");
			e.printStackTrace();
		}
	}
}
