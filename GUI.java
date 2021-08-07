    import java.awt.*;
    import java.awt.event.*;
    import javax.swing.*;
    import javax.swing.table.*;
    import java.util.*;
    import java.util.regex.*; 
    import javafx.util.Pair;
    
    /**
     * This is the GUI of the car park application from project 1. 
     *
     * @author Quinn Chan 103053395
     * @version JDK 14.0.2 - 22/10/2020
     */
    public class GUI
    {
        //Swing Components
        private JFrame frame;
        private DefaultTableModel model;
        private CarPark carPark;
        private JTable table;
        // Regex and prompts for checking inputs
        private static final String regNumRegex = "^[A-Z][0-9]{5}$";
        private static final String regNumPrompt = "Registration number requires one uppercase letter followed by five digits";
        private static final String slotIDRegex = "^[A-Z][0-9]{3}$";
        private static final String slotIDPrompt = "Slot ID requires one uppercase letter followed by three digits";    
        public GUI()
        {
            makeFrame();
        }
    
        /**
         * Create the Swing frame and its content.
         */
        private void makeFrame()
        {
            frame = new JFrame("Car Park Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            frame.setTitle("Car Park Management System");            
            frame.setLayout(new FlowLayout());
            
            makeMenuBar();                       
            frame.getContentPane().setLayout(new GridLayout(1, 1));
     
            //add tabs and panels
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);      
            tabbedPane.addTab("List all slots",listPanel()); 
            tabbedPane.addTab("Park a car", pcarPanel());
            tabbedPane.addTab("Find a car", fcarPanel());
            tabbedPane.addTab("Remove a car", rcarPanel());
            tabbedPane.addTab("Add a slot", aslotPanel());
            tabbedPane.addTab("Remove a slot", rslotPanel());
     
            frame.getContentPane().add(tabbedPane);
            frame.setResizable(false);
            frame.pack();
            frame.setPreferredSize(new Dimension(600, 600));
            frame.pack();
            frame.setVisible(true);
            
            // Pop up window to ask for input
            createSlots(); 
        }
        
        /**
         * Show dialog to ask for user input to create slots.
         */
        private void createSlots()
        {
            JTextField sSlotText = new JTextField();
            JTextField vSlotText = new JTextField();
            String intRegex = "^[0-9]{1,3}$";
            String intPrompt = "Please enter integers from 1 to 999 for both";
            Object[] message = {
                "Enter number (1-999) of staff slots:", sSlotText,
                "Enter number (1-999) of visitor slots:", vSlotText
            };
            
            int option = JOptionPane.showConfirmDialog(frame, message, "Create slots", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String sSlot=sSlotText.getText();
                String vSlot=vSlotText.getText();
                if (checkFormat(sSlot, intRegex, intPrompt) && checkFormat(vSlot, intRegex, intPrompt)) {
                    carPark = new CarPark (Integer. parseInt(sSlot), Integer. parseInt(vSlot));
                    model= makeModel(carPark);
                    table.setModel(model);
                    model.fireTableDataChanged();
                    JOptionPane.showMessageDialog(frame, "Car park created with "+carPark.getStaffSlotsSize()+" staff slots and " +carPark.getVisitorSlotsSize()+" visitor slots.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Car park is not created");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Create car park to display slots");
            }
        }
    
        /**
         * Create List all panel
         * 
         * @return    JPanel
         */  
        private JPanel listPanel() 
        {
            JPanel p = new JPanel();
            carPark = new CarPark (0, 0);
            model = makeModel(carPark);
            table = new JTable(model);             
            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);
            p.add(scrollPane);
            return p;
        }
        
        /**
         * Create DefaultTable Model which links to JTable
         * 
         * @param  CarPark c
         * @return    DefaultTableModel
         */
        private DefaultTableModel makeModel(CarPark c)
        {
            String[] columns = new String[] {"SlotID", "Type", "Availability", "Car & owner"};
            Object[][]data = c.toArray(); 
            DefaultTableModel m = new DefaultTableModel(data, columns); 
            return m;
        }
        
        /**
         * Create Park car panel
         * 
         * * @return    JPanel
         */
        private JPanel pcarPanel() 
        {
            JPanel p = new JPanel(new BorderLayout()); 
            JPanel panel = new JPanel(new GridBagLayout());
            
            //GridBagLayout for inner panel
            GridBagConstraints constr = new GridBagConstraints();
            constr.insets = new Insets(5, 5, 5, 5);     
            constr.anchor = GridBagConstraints.WEST;     
            
            //Label components
            JLabel carLabel  = new JLabel("Enter car registration number:   ");
            JLabel ownerLabel = new JLabel("Enter the car owner's name:   ");
            JLabel slotLabel = new JLabel("Enter the slot ID: ");
            JLabel staffLabel = new JLabel("Staff or visitor: ");
            
            //Textfield components
            JTextField carText  = new JTextField( 10 );
            JTextField ownerText  = new JTextField( 10 );
            JTextField slotText = new JTextField( 10 );
            
            //RadioButtons components
            JRadioButton staffButton = new JRadioButton("Staff");
            JRadioButton visitorButton = new JRadioButton("Visitor");
            ButtonGroup group = new ButtonGroup();
            group.add(staffButton);
            group.add(visitorButton);
            staffButton.setActionCommand("Y");
            visitorButton.setActionCommand("N");
            staffButton.setSelected(true);                                 
            JPanel staffPanel = new JPanel();
            staffPanel.add(staffButton);
            staffPanel.add(visitorButton);

            //arrange inner panel
            constr.gridx=0;
            constr.gridy=0;
            panel.add(carLabel, constr);
            constr.gridx=1;
            panel.add(carText, constr);
            
            constr.gridx=0; constr.gridy=1;             
            panel.add(ownerLabel, constr);
            constr.gridx=1;
            panel.add(ownerText, constr);
            
            constr.gridx=0; constr.gridy=2;
            panel.add(slotLabel, constr);
            constr.gridx=1;
            panel.add(slotText, constr);
            
            constr.gridx=0; constr.gridy=3;
            panel.add(staffLabel, constr);
            constr.gridx=1;
            panel.add(staffPanel, constr);
             
            //confirm button
            constr.gridy=4;
            constr.gridwidth = 2;
            constr.anchor = GridBagConstraints.CENTER;            
            JButton confirm = new JButton("Confirm");
            panel.add(confirm, constr);            
            confirm.addActionListener(new ActionListener() {
                                  public void actionPerformed(ActionEvent e) { 
                                       String carID=carText.getText();
                                       String owner=ownerText.getText(); 
                                       String slotID=slotText.getText(); 
                                       char type=group.getSelection().getActionCommand().charAt(0);
                                       if (checkFormat(carID, regNumRegex, regNumPrompt) && checkFormat(owner, "^[a-zA-Z ,.'-]+$", "Please enter correct owner name.") && checkFormat(slotID, slotIDRegex, slotIDPrompt))
                                       {
                                           Car newCar = new Car (carID, owner, type);
                                           Pair<Integer, String> result = carPark.parkCar(newCar, slotID);
                                           JOptionPane.showMessageDialog(frame, result.getValue());
                                           int index = result.getKey();
                                           if ( index >= 0 ){
                                               //update the Jtable
                                               model.setValueAt("occupied",index,2);
                                               model.setValueAt(carID+" / "+owner,index,3);
                                               model.fireTableRowsUpdated(index, index);
                                            }
                                        }                                   
                                 }
                           }); 
            p.add(panel, BorderLayout.NORTH);
            return p;
        }
        
        /**
         * Create Find car panel
         * 
         * * @return    JPanel
         */
        private JPanel fcarPanel() 
        {
            JPanel p = new JPanel();
            JLabel carLabel  = new JLabel("Enter car registration number:   ");                
            JTextField carText  = new JTextField( 10 );        
            JButton confirm = new JButton("Confirm");  
            confirm.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { 
                                           String carID=carText.getText();                                        
                                           if (checkFormat(carID, regNumRegex, regNumPrompt) )
                                           {
                                               String result = carPark.findCar(carID);
                                               JOptionPane.showMessageDialog(frame, result);                                          
                                            }
                                       
                                }
                           });            
            p.setLayout( new FlowLayout());
            p.add(carLabel);
            p.add(carText);
            p.add(confirm);
            return p;
        }
        
        /**
         * Create remove car panel
         * 
         * * @return    JPanel
         */
        private JPanel rcarPanel() 
        {
            JPanel p = new JPanel();
            JLabel carLabel  = new JLabel("Enter car registration number:   ");                
            JTextField carText  = new JTextField( 10 );        
            JButton confirm = new JButton("Confirm");  
            confirm.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { 
                                       String carID=carText.getText(); 
                                       if (checkFormat(carID, regNumRegex, regNumPrompt) )
                                       {
                                           Pair<Integer, String> result = carPark.delCar(carID);
                                           JOptionPane.showMessageDialog(frame, result.getValue());
                                          int index = result.getKey();
                                           if ( index >= 0 ){
                                               model.setValueAt("not occupied",index,2);
                                               model.setValueAt("N/A",index,3);
                                               model.fireTableRowsUpdated(index, index);
                                            }
                                        }                                   
                                }
                           });                        
            
            p.setLayout( new FlowLayout());
            p.add(carLabel);
            p.add(carText);
            p.add(confirm);
            return p;
        }
        
        /**
         * Create add slot panel
         * 
         * @return    JPanel
         */
        private JPanel aslotPanel() 
        {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());     
            JPanel panel = new JPanel(new GridBagLayout());
            
            GridBagConstraints constr = new GridBagConstraints();
            constr.insets = new Insets(5, 5, 5, 5);     
            constr.anchor = GridBagConstraints.WEST;
     
            constr.gridx=0;
            constr.gridy=0;
      
            JLabel slotLabel = new JLabel("Enter the slot ID: ");
            JLabel staffLabel = new JLabel("Staff or visitor: ");
             
            JTextField slotText = new JTextField( 10 );
            
            JRadioButton staffButton = new JRadioButton("Staff");            
            JRadioButton visitorButton = new JRadioButton("Visitor");
            staffButton.setActionCommand("Y");
            visitorButton.setActionCommand("N");            
            ButtonGroup group = new ButtonGroup();
            staffButton.setSelected(true);
            group.add(staffButton);
            group.add(visitorButton);
            JPanel staffPanel = new JPanel();
            staffPanel.add(staffButton);
            staffPanel.add(visitorButton);
            
            panel.add(slotLabel, constr);
            constr.gridx=1;
            panel.add(slotText, constr);
            constr.gridx=0; constr.gridy=1;             
            panel.add(staffLabel, constr);
            constr.gridx=1;
            panel.add(staffPanel, constr);
            constr.gridx=1; constr.gridy=2;             
            
            JButton confirm = new JButton("Confirm");
            confirm.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { 
                                       String slotID=slotText.getText(); 
                                       char type=group.getSelection().getActionCommand().charAt(0);
                                       if (checkFormat(slotID, slotIDRegex, slotIDPrompt))
                                       {
                                           Pair<Integer, String> result = carPark.addSlot(slotID, type);
                                           JOptionPane.showMessageDialog(frame, result.getValue());
                                           int index = result.getKey();
                                           if ( index >= 0 ){
                                               model.addRow(carPark.toRow(index));
                                               model.fireTableRowsInserted(index, index);
                                            }
                                        }                                   
                                }
                           });

            panel.add(confirm, constr);
            p.add(panel, BorderLayout.NORTH);
            return p;
        }
        
        /**
         * Create remove slot panel
         * 
         *  @return    JPanel
         */
        private JPanel rslotPanel() 
        {
            JPanel p = new JPanel();        
            JLabel slotLabel = new JLabel("Enter the slot ID: ");
            JTextField slotText = new JTextField(10);        
            JButton confirm = new JButton("Confirm");
            confirm.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { 
                                       String slotID=slotText.getText(); 
                                       if (checkFormat(slotID, slotIDRegex, slotIDPrompt))
                                       {
                                           Pair<Integer, String> result = carPark.delSlot(slotID);
                                           JOptionPane.showMessageDialog(frame, result.getValue());
                                           int index = result.getKey();
                                           if ( index >= 0 ){
                                               model.removeRow(index);
                                               model.fireTableRowsDeleted(index, index);
                                            }
                                        }                                   
                                }
                           });
        
            p.setLayout( new FlowLayout());
            p.add(slotLabel);
            p.add(slotText);
            p.add(confirm);
            return p;
        }
    
    
        /**
         * Quit function: quit the application.
         */
        private void quit()
        {
            System.exit(0);
        }
        
        
        /**
         * Create the main frame's menu bar.
         * 
         */
        private void makeMenuBar()
        {
            JMenuBar menubar = new JMenuBar();
            frame.setJMenuBar(menubar);
    
            JMenu fileMenu = new JMenu("Car Park");
            menubar.add(fileMenu);
    
            JMenuItem openItem = new JMenuItem("Create");
            openItem.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { createSlots(); }
                               });
            fileMenu.add(openItem);
    
            JMenuItem quitItem = new JMenuItem("Quit");
            quitItem.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) { quit(); }
                               });
            fileMenu.add(quitItem);
        }
        
        /**
         * Check input for matching required format
         * 
         * @return    JPanel
         */
        public Boolean checkFormat(String input, String format, String formatPrompt)
        {   
            Boolean isMatch = false;
            
                if (Pattern.matches(format, input))
                    isMatch = true;
                else
                    JOptionPane.showMessageDialog(frame, formatPrompt);
     
            return isMatch;
        }
    
        public static void main(String[] args)
        {
            new GUI();        
        }
}
