
/* CST8221 – JAP, Midterm Test, Part 2
* Student Name and Student Number: Lucas Estienne 040 819 959
* Date: 2016-11-09 Time: 11:57
* Development platform: Win 10
* Development tool: Netbeans
* Java SDK version: 1.8_91
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;

public class TextEditor extends JFrame{
    private static final int TEXT_AREA_ROWS = 60;
    private static final int TEXT_AREA_COLUMNS = 50;
    private JTextArea textArea;
    private JLabel statusLine;
    
    public TextEditor() {
        setTitle("Lucas Estienne's Midterm Test - F16");
        textArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);  
        
        JToolBar editorToolbar = createEditorToolbar();
        JMenuBar editorMenubar = createEditorMenubar();
        statusLine = new JLabel();
        statusLine.setSize(600, 25);

        Container content = getContentPane();
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(editorToolbar, BorderLayout.NORTH);
        content.add(statusLine, BorderLayout.SOUTH);
        setJMenuBar(editorMenubar);
        setSize(600, 700);
        setResizable(true);
    }
    
    private JToolBar createEditorToolbar(){
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(true);
        
        JCheckBox check = createReadOnlyCheck();
        toolbar.add(check);
        toolbar.addSeparator();
        
        Action whiteAction = new ColorAction("W", "White", Color.WHITE, new Integer(KeyEvent.VK_W));
        toolbar.add(whiteAction);
        toolbar.addSeparator();
        
        Action blueAction = new ColorAction("B", "Blue", Color.BLUE, new Integer(KeyEvent.VK_B));
        toolbar.add(blueAction);
        toolbar.addSeparator();
        
        Action orangeAction = new ColorAction("O", "Orange", Color.ORANGE, new Integer(KeyEvent.VK_O));
        toolbar.add(orangeAction);
        toolbar.addSeparator();
        
        return toolbar;
    }
    
    private JCheckBox createReadOnlyCheck(){
        JCheckBox check = new JCheckBox("R");
        check.setToolTipText("Read Only");
        
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setEditable( ((JCheckBox) e.getSource()).isSelected() ? false : true);
            }
        });
        
        return check;
    }
    
    private JMenuBar createEditorMenubar(){
        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createEditMenu());
        
        menubar.add(Box.createHorizontalGlue());
        menubar.add(createHelpMenu());
        return menubar;
    }
    
    private JMenu createFileMenu(){
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        
        menu.add(new NewAction());
        menu.addSeparator();
        
        menu.add(new OpenAction());
        menu.add(new SaveAction());
        menu.addSeparator();
        
        menu.add(new ExitAction());
        
        return menu;
    }

    private JMenu createEditMenu(){
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        
        Action dekAction;
        
        dekAction = textArea.getActionMap().get(DefaultEditorKit.cutAction);
        dekAction.putValue(Action.NAME, "Cut");
        menu.add(textArea.getActionMap().get(DefaultEditorKit.cutAction));
        
        dekAction = textArea.getActionMap().get(DefaultEditorKit.copyAction);
        dekAction.putValue(Action.NAME, "Copy");
        menu.add(textArea.getActionMap().get(DefaultEditorKit.copyAction));
        
        dekAction = textArea.getActionMap().get(DefaultEditorKit.pasteAction);
        dekAction.putValue(Action.NAME, "Paste");
        menu.add(textArea.getActionMap().get(DefaultEditorKit.pasteAction));
        
        return menu;
    }
    
    private JMenu createHelpMenu(){
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        
        menu.add(new AboutAction());
        
        return menu;
    }
    
    private File getFileFromDialog(){
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files (.txt)", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java files (.class)", "class"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java files (.java)", "java"));
        if (fileChooser.showOpenDialog(TextEditor.this) != JFileChooser.APPROVE_OPTION){
            return null;
        }  
        
        File file = fileChooser.getSelectedFile();
        return file;
    }
    
    private class NewAction extends AbstractAction {
        public NewAction() { 
            putValue(Action.NAME, "New");//Displayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, "New"); //Action Command
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent ev) { 
            statusLine.setText("Status: New empty file.");
            textArea.setText("");
        }
    }
    
    private class AboutAction extends AbstractAction {
        private static final String ABOUT_MSG = "Lucas Estienne's Midterm\nVersion 1.0";
        private static final String ABOUT_TITLE = "About Midterm Test";
        
        public AboutAction() { 
            putValue(Action.NAME, "About");//Displayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, "About"); //Action Command
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        }
        public void actionPerformed(ActionEvent ev) { 
            JOptionPane.showMessageDialog(TextEditor.this, ABOUT_MSG, ABOUT_TITLE, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private class ExitAction extends AbstractAction {
        public ExitAction() { 
            putValue(Action.NAME, "Exit");//Displayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, "Exit"); //Action Command
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent ev) { 
            System.exit(0); 
        }
    }

    private class OpenAction extends AbstractAction {
        public OpenAction() { 
            putValue(Action.NAME, "Open");//Displayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, "Open"); //Action Command
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        }

        public void actionPerformed(ActionEvent ev) {
            File file = getFileFromDialog();
            if (file == null){
                statusLine.setText("Status: File does not exist.");
                return;
            }
            FileReader reader = null;
            
            try {
                reader = new FileReader(file);
                textArea.read(reader, null);
                statusLine.setText("Status: File " + file.getName() + " is open.");
            }
            catch (IOException ex) {
                statusLine.setText("Status: File " + file.getName() + " does not exist.");
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException x) {}
                }
            }
        }
    }

    private class SaveAction extends AbstractAction {
        public SaveAction() {
            putValue(Action.NAME, "Save");//Displayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, "Save"); //Action Command
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        }

        public void actionPerformed(ActionEvent ev) {
            File file = getFileFromDialog();
            if (file == null){
                statusLine.setText("Status: Could not save file.");
                return;
            }
            FileWriter writer = null;
            
            try {
                writer = new FileWriter(file);
                textArea.write(writer);
                statusLine.setText("Status: File " + file.getName() + " is saved.");
            }
            catch (IOException ex) {
                statusLine.setText("Status: Could not save file " + file.getName());
            }
            finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException x) {}
                }
            }
        }
    }
    
    private class ColorAction extends AbstractAction  {
     
	private static final long serialVersionUID = -372900583421753451L;

	public ColorAction(String name, String fullname, Color c, Integer mnemonic)
        {
            putValue(Action.NAME, name);//Di splayed Text and Action Command
            putValue(Action.ACTION_COMMAND_KEY, name); //Action Command
            putValue(Action.SHORT_DESCRIPTION,"Background "+ fullname);//ToolTip
            putValue(Action.MNEMONIC_KEY, mnemonic);
            putValue("Color", c);
        }

        public void actionPerformed(ActionEvent event)
        {
            Color c = (Color) getValue("Color");
            textArea.setBackground(c);
        }
   }
}


